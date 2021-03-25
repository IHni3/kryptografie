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
import java.util.stream.Collectors;

public class SendMessageCommand implements ICommand{

    private String sender;
    private String recipient;
    private String message;
    private AlgorithmType algorithmType;
    private String keyfile;

    public SendMessageCommand(String message, String sender, String recipient, AlgorithmType algorithm, String keyfile) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
        this.algorithmType = algorithm;
        this.keyfile = keyfile;
    }

    public String execute() throws CommandExecutionException {
        var channel = DBService.instance.getChannel(sender, recipient);
        if (channel == null){
            throw new CommandExecutionException(String.format("no valid channel from %s to %s", sender, recipient));
        }

        var senderPart = DBService.instance.getOneParticipant(sender);
        var recieverPart = DBService.instance.getOneParticipant(recipient);

        Date now = new Date();
        long timestampLong = now.getTime()/1000;
        String timestamp = String.valueOf(timestampLong);

        Message dbMessage = new Message(senderPart, recieverPart, algorithmType.toString(), keyfile, timestamp, message, message);
        channel.send(new BusMessage(dbMessage));

        DBService.instance.insertMessage(dbMessage);

        return null;
    }
}
