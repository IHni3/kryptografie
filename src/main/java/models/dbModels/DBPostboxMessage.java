package models.dbModels;

import models.dbModels.DBParticipant;

public class DBPostboxMessage {
    private final DBParticipant participantSender;
    private final DBParticipant participantReceiver;
    private final String message;
    private final String timestamp;

    public DBPostboxMessage(DBParticipant participantSender, DBParticipant participantReceiver, String message, String timestamp){
        this.participantSender = participantSender;
        this.participantReceiver = participantReceiver;
        this.message = message;
        this.timestamp = timestamp;
    }

    public DBParticipant getParticipantSender() {
        return participantSender;
    }

    public DBParticipant getParticipantReceiver() {
        return participantReceiver;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}