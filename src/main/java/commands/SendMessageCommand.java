/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import configuration.AlgorithmType;
import database.DBService;
import models.BusMessage;
import models.Message;

import java.util.Date;

public class SendMessageCommand implements ICommand{

    private final String sender;
    private final String receiver;
    private final String message;
    private final AlgorithmType algorithmType;
    private final String keyfile;

    public SendMessageCommand(String message, String sender, String receiver, AlgorithmType algorithm, String keyfile) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.algorithmType = algorithm;
        this.keyfile = keyfile;
    }

    public String execute() throws CommandExecutionException {
        var channel = DBService.instance.getChannel(sender, receiver);
        if (channel == null){
            return String.format("no valid channel from %s to %s", sender, receiver);
        }

        var senderPart = DBService.instance.getOneParticipant(sender);
        var receiverPart = DBService.instance.getOneParticipant(receiver);



        ICommand encryption = new EncryptMessageCommand(message, algorithmType, keyfile);
        String encrypted = encryption.execute();

        //save message
        Message dbMessage = new Message(senderPart, receiverPart, algorithmType, keyfile, message, encrypted);
        DBService.instance.insertMessage(dbMessage);

        //send message without plain text
        channel.send(new BusMessage(dbMessage));

        return null;
    }
}
