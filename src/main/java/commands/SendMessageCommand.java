/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import configuration.AlgorithmType;
import database.DBService;
import models.ModelStorage;
import models.Transmission;
import models.dbModels.DBMessage;

import java.util.Date;
import java.util.stream.Collectors;

public class SendMessageCommand implements ICommand{

    private String sender;
    private String recipient;
    private String message;
    private AlgorithmType algorithmType;
    private String keyfile;

    public SendMessageCommand(String message, String sender, String recipient, AlgorithmType algorithm, String keyfile) {
        this.sender = sender;
        this.recipient = this.recipient;
        this.message = message;
        this.algorithmType = algorithm;
        this.keyfile = keyfile;
    }

    public String execute() throws CommandExecutionException {
        var channels = ModelStorage.instance.getChannels();
        var channelResults = channels.stream().filter(n -> (n.getParticipantA().getName().equals(sender) && n.getParticipantB().getName().equals(recipient)) ||
                                      (n.getParticipantA().getName().equals(recipient) && n.getParticipantB().getName().equals(sender)));
        if (channelResults.count() == 0){
            throw new CommandExecutionException(String.format("no valid channel from %s to %s", sender, recipient));
        }

        var participants = DBService.instance.getParticipants();
        var senderPart = participants.stream().filter(p -> p.getName().equals(sender)).collect(Collectors.toList()).get(0);
        var recieverPart = participants.stream().filter(p -> p.getName().equals(recipient)).collect(Collectors.toList()).get(0);

        Date now = new Date();
        long timestampLong = now.getTime()/1000;
        String timestamp = String.valueOf(timestampLong);

        DBMessage dbMessage = new DBMessage(senderPart, recieverPart, algorithmType.toString(), keyfile, timestamp, message, message);
        var channel = channelResults.collect(Collectors.toList()).get(0);
        channel.send(new Transmission(dbMessage));

        DBService.instance.insertMessage(dbMessage);

        return null;
    }
}
