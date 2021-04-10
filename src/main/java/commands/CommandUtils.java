/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import configuration.AlgorithmType;
import configuration.Configuration;
import database.DBService;
import models.BusMessage;
import models.Channel;
import models.Message;
import models.Participant;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class CommandUtils {

    public CommandUtils(){}

    public String crackRSA(String message, String keyfile){
        String cracked;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new CrackRSATask(keyfile, message));
        try {
            cracked = future.get(30, TimeUnit.SECONDS);
            return cracked;
        } catch (Exception e) {
            Configuration.instance.textAreaLogger.info(String.format("cracking encrypted message \"%s\" failed", message));
        }
        return null;
    }

    public String crackShift(String message){
        String cracked;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new CrackShiftTask(message));
        try {
            cracked = future.get(30, TimeUnit.SECONDS);
            return cracked;
        } catch (Exception e) {
            Configuration.instance.textAreaLogger.info(String.format("cracking encrypted message \"%s\" failed", message));
        }
        return null;
    }

    public void showChannel(){
        var channelList = DBService.instance.getChannels();

        StringBuilder returnString = new StringBuilder();

        for (var channel : channelList) {
            Configuration.instance.textAreaLogger.info(String.format("%s | %s and %s\n", channel.getName(), channel.getParticipantA().getName(), channel.getParticipantB().getName()));
        }

        if(channelList.isEmpty()){
            Configuration.instance.textAreaLogger.info("channel list is empty!");
        }
    }

    public void dropChannel(String channelName){
        var channel = DBService.instance.getChannel(channelName);

        if (channel == null){
            Configuration.instance.textAreaLogger.info(String.format("unknown channel %s", channelName));
        }

        if (!DBService.instance.removeChannel(channelName)){
            Configuration.instance.textAreaLogger.info("Something went wrong");
        }
    }

    public  void createChannel(String channelName, String part1name, String part2name){
        if (DBService.instance.getChannel(channelName) != null){
            Configuration.instance.textAreaLogger.info(String.format("channel %s already exists", channelName));
        }

        if (DBService.instance.getChannel(part1name, part2name) != null){
            Configuration.instance.textAreaLogger.info(String.format("communication channel between %s and %s already exists", part1name, part2name));
        }

        if (part1name.equals(part2name)){
            Configuration.instance.textAreaLogger.info(String.format("%s and %s identical - cannot create channel on itself", part1name, part2name));
        }

        var part1 = DBService.instance.getOneParticipant(part1name);
        var part2 = DBService.instance.getOneParticipant(part2name);

        Channel channel = new Channel(channelName, part1, part2);

        DBService.instance.insertChannel(channel);
    }

    public void intrudeChannelCommand(String channelName, String participant){
        var intruder = DBService.instance.getOneParticipant(participant);

        if (intruder == null){
            Configuration.instance.textAreaLogger.info(String.format("intruder %s could not be found", participant));
        }

        var channel = DBService.instance.getChannel(channelName);

        if (channel == null){
            Configuration.instance.textAreaLogger.info(String.format("channel %s could not be found", channelName));
        }

        channel.intrude(intruder);
    }

    public void registerParticipant(String name, String type){
        if (DBService.instance.getOneParticipant(name) != null){
            Configuration.instance.textAreaLogger.info(String.format("participant %s already exists, using existing postbox_%s", name));
        }

        Participant participant = new Participant(name, type);
        DBService.instance.insertParticipant(participant);
    }

    public void sendMessage(String message, String sender, String recipient, AlgorithmType algorithmType, String keyfile){
        var channel = DBService.instance.getChannel(sender, recipient);
        if (channel == null){
            Configuration.instance.textAreaLogger.info(String.format("no valid channel from %s to %s", sender, recipient));
        }

        var senderPart = DBService.instance.getOneParticipant(sender);
        var recieverPart = DBService.instance.getOneParticipant(recipient);

        Date now = new Date();
        long timestampLong = now.getTime()/1000;
        String timestamp = String.valueOf(timestampLong);

        String encrypted = encrypt(message, algorithmType, keyfile);

        Message dbMessage = new Message(senderPart, recieverPart, algorithmType.toString().toLowerCase(Locale.ROOT), keyfile, timestamp, message, encrypted);
        channel.send(new BusMessage(encrypted, senderPart, recieverPart, algorithmType, keyfile));

        DBService.instance.insertMessage(dbMessage);
    }

    public String encrypt(String message, AlgorithmType algorithmType, String keyfile){
        File keyfileFile = new File(Configuration.instance.keyFiles + keyfile);
        if (!keyfileFile.exists()){
            Configuration.instance.textAreaLogger.info("Keyfile " + keyfile + " not existing");
            return null;
        }
        Configuration.instance.loggingHandler.newLogfile(algorithmType.toString(), "encrypt");
        String jarName = (algorithmType.equals(AlgorithmType.RSA) ? "rsa" : "shift") + ".jar";
        String className = algorithmType.equals(AlgorithmType.RSA) ? "RSA" : "Shift";
        if (!JarVerifier.verifie(jarName)){
            Configuration.instance.textAreaLogger.info("jar could not be verified - not loading corrupted jar");
            return null;
        }
        try {
            var port = Loader.getPort(Configuration.instance.jarPath + jarName, className);
            var method = port.getClass().getDeclaredMethod("encrypt", File.class, String.class, Logger.class);
            var answer = method.invoke(port, keyfileFile, message, Configuration.instance.loggingHandler.getLogger());
            if (answer == null){
                Configuration.instance.textAreaLogger.info("Problems encrypting message, keyfile might be invalid");
                return null;
            }
            return answer.toString();
        } catch (IOException e) {
            Configuration.instance.textAreaLogger.info("Invalid Keyfile Provided");
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String message, AlgorithmType algorithmType, String keyfile){
        File keyfileFile = new File(Configuration.instance.keyFiles  + keyfile);
        if (!keyfileFile.exists()){
            Configuration.instance.textAreaLogger.info("Keyfile " + keyfile + " not existing");
            return null;
        }
        Configuration.instance.loggingHandler.newLogfile(algorithmType.toString(), "decrypt");
        String jarName = (algorithmType.equals(AlgorithmType.RSA) ? "rsa" : "shift") + ".jar";
        String className = algorithmType.equals(AlgorithmType.RSA) ? "RSA" : "Shift";
        if (!JarVerifier.verifie(jarName)){
            Configuration.instance.textAreaLogger.info("jar could not be verified - not loading corrupted jar");
            return null;
        }
        try {
            var port = Loader.getPort(Configuration.instance.jarPath + jarName, className);
            var method = port.getClass().getDeclaredMethod("decrypt", File.class, String.class, Logger.class);
            var answer = method.invoke(port, keyfileFile, message, Configuration.instance.loggingHandler.getLogger());
            if (answer == null){
                Configuration.instance.textAreaLogger.info("Problems decrypting message, keyfile might be invalid");
                return null;
            }
            return answer.toString();
        } catch (IOException e) {
            Configuration.instance.textAreaLogger.info("Invalid Keyfile Provided");
            return null;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
