package models;

import models.dbModels.DBMessage;

public class Transmission {

    private String message;
    private String sender;
    public  Transmission(DBMessage message){
        this.message = message.getEncryptedMessage();
        this.sender = message.getParticipantSender().getName();
    }
}
