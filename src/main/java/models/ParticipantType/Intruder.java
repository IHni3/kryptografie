package models.ParticipantType;

import commands.*;
import configuration.AlgorithmType;
import configuration.Configuration;
import database.DBService;
import models.BusMessage;
import models.PostboxMessage;

import java.util.Date;

public class Intruder implements IParticipantType {

    @Override
    public void receiveMessage(BusMessage message) {
        ICommand cracker = message.getAlgorithm().equals(AlgorithmType.RSA) ? new CrackMessageRsaCommand(message.getMessage(), message.getKeyfile()) :
                                                                              new CrackMessageShiftCommand(message.getMessage());
        String timestamp = String.valueOf(new Date().getTime()/1000);
        DBService.instance.insertPostboxMessage(new PostboxMessage(message.getSender(), message.getRecipient(), "unknown", timestamp));
        try{
            String cracked = cracker.execute();
            Configuration.instance.textAreaLogger.info(String.format("cracked message: %s", cracked));
        } catch (CommandExecutionException e){
            Configuration.instance.textAreaLogger.info(String.format("cracking encrypted message \"%s\"", message.getMessage()));
        }

    }
}
