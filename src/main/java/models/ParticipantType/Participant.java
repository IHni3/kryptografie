package models.ParticipantType;

import commands.CommandUtils;
import configuration.Configuration;
import database.DBService;
import models.BusMessage;
import models.PostboxMessage;

import java.util.Date;

public class Participant implements IParticipantType {
    private models.Participant participant;
    public Participant (models.Participant participant){
        this.participant = participant;
    }

    @Override
    public void receiveMessage(BusMessage message) {
        CommandUtils utils = new CommandUtils();

        try {
            String decrypted = utils.decrypt(message.getMessage(), message.getAlgorithm(), message.getKeyfile());
            String timestamp = String.valueOf(new Date().getTime()/1000);
            DBService.instance.insertPostboxMessage(new PostboxMessage(message.getSender(), message.getRecipient(), decrypted, timestamp));
            Configuration.instance.textAreaLogger.info(String.format("%s received new message", participant.getName()));
        } catch (Exception e) {
            Configuration.instance.textAreaLogger.info("Decryption timed out!");
        }

    }

    @Override
    public String toString(){
        return "PARTICIPANT";
    }
}
