/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

import com.google.common.eventbus.EventBus;

public class Channel {

    private EventBus eventBus = new EventBus();
    private Participant participantA;
    private Participant participantB;
    private String name;


    public  Channel(Participant participantA, Participant participantB, String name){
        eventBus.register(participantA);
        eventBus.register(participantB);
        this.name = name;
    }

    public void send(Transmission transmission){
        eventBus.post(transmission);
    }

    public void intrude(Participant participant){
        eventBus.register(participant);
    }

    public Participant getParticipantA() {
        return participantA;
    }

    public Participant getParticipantB() {
        return participantB;
    }

    public String getName() {
        return name;
    }

}
