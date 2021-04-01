/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

import configuration.AlgorithmType;

import java.util.Date;

public class Message {
    private final Participant participantSender;
    private final Participant participantReceiver;
    private final AlgorithmType algorithm;
    private final String keyfile;
    private final long timestamp;
    private final String plainMessage;
    private final String encryptedMessage;

    public Message(Participant participantSender, Participant participantReceiver, AlgorithmType algorithm, String keyfile, String plainMessage, String encryptedMessage){
        this.participantSender = participantSender;
        this.participantReceiver = participantReceiver;
        this.algorithm = algorithm;
        this.keyfile = keyfile;
        this.timestamp = unixTimestampNow();
        this.plainMessage = plainMessage;
        this.encryptedMessage = encryptedMessage;
    }

    private static long unixTimestampNow() {
        Date now = new Date();
        return now.getTime()/1000;
    }

    public Participant getParticipantSender(){
        return this.participantSender;
    }

    public Participant getParticipantReceiver(){
        return this.participantReceiver;
    }

    public AlgorithmType getAlgorithm() {
        return this.algorithm;
    }

    public String getKeyfile() {
        return this.keyfile;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public String getPlainMessage() {
        return this.plainMessage;
    }

    public String getEncryptedMessage() {
        return this.encryptedMessage;
    }
}
