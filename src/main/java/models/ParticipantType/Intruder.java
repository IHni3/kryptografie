package models.ParticipantType;

import commands.*;
import configuration.AlgorithmType;
import configuration.Configuration;
import database.DBService;
import models.BusMessage;
import models.PostboxMessage;

import java.util.Date;

public class Intruder implements IParticipantType {
    private models.Participant participant;
    public Intruder (models.Participant participant){
        this.participant = participant;
    }

    @Override
    public void receiveMessage(BusMessage message) {
        CommandUtils utils = new CommandUtils();

        String timestamp = String.valueOf(new Date().getTime()/1000);
        DBService.instance.insertPostboxMessage(new PostboxMessage(message.getSender(), message.getRecipient(), "unknown", timestamp));
        try{
            String cracked = message.getAlgorithm().equals(AlgorithmType.RSA) ? utils.crackRSA(message.getMessage(), message.getKeyfile()) : utils.crackShift(message.getMessage());
            Configuration.instance.textAreaLogger.info(String.format("cracked message: %s", cracked));
        } catch (Exception e){
            Configuration.instance.textAreaLogger.info(String.format("cracking encrypted message \"%s\"", message.getMessage()));
        }

    }

}
