package models.ParticipantType;

import commands.CommandExecutionException;
import commands.DecryptMessageCommand;
import commands.ICommand;
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
        ICommand decrypter = new DecryptMessageCommand(message.getMessage(), message.getAlgorithm(), message.getKeyfile());

        try {
            String decrypted = decrypter.execute();
            String timestamp = String.valueOf(new Date().getTime()/1000);
            DBService.instance.insertPostboxMessage(new PostboxMessage(message.getSender(), message.getRecipient(), decrypted, timestamp));
            Configuration.instance.textAreaLogger.info(String.format("%s received new message", participant.getName()));
        } catch (CommandExecutionException e) {
            Configuration.instance.textAreaLogger.info("Decryption timed out!");
        }

    }
}
