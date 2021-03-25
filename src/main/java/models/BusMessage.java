/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

import configuration.AlgorithmType;

public class BusMessage {

    private String message;
    private String sender;
    public BusMessage(Message message){
        this.message = message.getEncryptedMessage();
        this.sender = message.getParticipantSender().getName();
    }
}
