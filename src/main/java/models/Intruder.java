/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

import commands.CommandExecutionException;
import commands.CrackMessageRsaCommand;
import commands.CrackMessageShiftCommand;
import commands.ICommand;
import configuration.AlgorithmType;
import configuration.Configuration;
import database.DBService;

public class Intruder extends Participant {
    public Intruder(String name) {
        super(name);
    }

    @Override
    public void receiveMessage(BusMessage message) {


        ICommand cracker;

        if (message.getAlgorithm() == AlgorithmType.RSA)
            cracker = new CrackMessageRsaCommand(message.getMessage(), message.getKeyfile());
        else if (message.getAlgorithm() == AlgorithmType.SHIFT)
            cracker = new CrackMessageShiftCommand(message.getMessage());
        else {
            Configuration.instance.getGUILogger().printWarning(String.format("intruder: algorithm %s not found! using shift", message.getAlgorithm().name()));
            cracker = new CrackMessageShiftCommand(message.getMessage());
        }

        DBService.instance.insertPostboxMessage(new PostboxMessage(message.getSender(), message.getRecipient(), "unknown"));
        try {
            String cracked = cracker.execute();
            Configuration.instance.getGUILogger().printInfo(String.format("cracked message: %s", cracked));
        } catch (CommandExecutionException e) {
            Configuration.instance.getGUILogger().printInfo(String.format("cracking encrypted message \"%s\" failed! cause: %s", message.getMessage(), e.getMessage()));
        }
    }

    @Override
    public String getType() {
        return "intruder";
    }
}
