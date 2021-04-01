/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

import com.google.common.eventbus.Subscribe;
import commands.CommandExecutionException;
import commands.DecryptMessageCommand;
import commands.ICommand;
import configuration.Configuration;
import database.DBService;

import java.util.Date;

public class Participant {
    private final String name;

    public Participant(String name) {
        this.name = name;
    }

    @Subscribe
    public void receiveMessage(BusMessage message) {
        ICommand decrypter = new DecryptMessageCommand(message.getMessage(), message.getAlgorithm(), message.getKeyfile());

        try {
            String decrypted = decrypter.execute();
            String timestamp = String.valueOf(new Date().getTime() / 1000);
            DBService.instance.insertPostboxMessage(new PostboxMessage(message.getSender(), message.getRecipient(), decrypted, timestamp));
            Configuration.instance.getGUILogger().printInfo(String.format("%s received new message", getName()));
        } catch (CommandExecutionException e) {
            Configuration.instance.getGUILogger().printInfo(String.format("something went wrong while receiving message! cause: %s", e.getMessage()));
        }
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return "normal";
    }
}
