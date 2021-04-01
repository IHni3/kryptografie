/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

import configuration.AlgorithmType;

public class BusMessage {

    private final String message;
    private final Participant sender;
    private final Participant recipient;
    private final AlgorithmType algorithm;
    private final String keyfile;

    public BusMessage(Message dbMessage) {
        this.message = dbMessage.getEncryptedMessage();
        this.sender = dbMessage.getParticipantSender();
        this.recipient = dbMessage.getParticipantReceiver();
        this.algorithm = dbMessage.getAlgorithm();
        this.keyfile = dbMessage.getKeyfile();
    }

    public BusMessage(String message, Participant sender, Participant recipient, AlgorithmType algorithm, String keyfile){
        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
    }

    public String getMessage() {
        return message;
    }

    public Participant getSender() {
        return sender;
    }

    public Participant getRecipient() {
        return recipient;
    }

    public AlgorithmType getAlgorithm() {
        return algorithm;
    }

    public String getKeyfile() {
        return keyfile;
    }
}
