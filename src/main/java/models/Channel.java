/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

import com.google.common.eventbus.EventBus;
import configuration.Configuration;
import database.DBService;

import java.util.Arrays;

public class Channel {

    private final String name;
    private final Participant participantA;
    private final Participant participantB;
    private final EventBus eventBus;

    public Channel(String name, Participant participantA, Participant participantB) {
        this.name = name;
        this.participantA = participantA;
        this.participantB = participantB;
        this.eventBus = new EventBus();

        eventBus.register(participantA);
        eventBus.register(participantB);

        if (Configuration.instance.intrudedChannels.containsKey(name))
            for (var intruderName : Configuration.instance.intrudedChannels.get(name)) {
                var intruder = DBService.instance.getOneParticipant(intruderName);
                eventBus.register(intruder);
            }
    }

    public String getName(){
        return this.name;
    }

    public Participant getParticipantA(){
        return this.participantA;
    }

    public Participant getParticipantB(){
        return this.participantB;
    }

    public void send(BusMessage message){
        eventBus.post(message);
    }

    public void intrude(Participant intruder) {
        eventBus.register(intruder);

        if (Configuration.instance.intrudedChannels.containsKey(this.name)) {
            var list = Configuration.instance.intrudedChannels.get(this.name);
            if (!list.contains(intruder.getName()))
                list.add(intruder.getName());
        } else {
            Configuration.instance.intrudedChannels.put(this.name, Arrays.asList(intruder.getName()));
        }
    }

}
