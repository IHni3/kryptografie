/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

import com.google.common.eventbus.Subscribe;
import models.ParticipantType.IParticipantType;
import models.ParticipantType.Intruder;

public class Participant {
    private final String name;
    private final IParticipantType type;

    public Participant(String name, String type){
        this.name = name;
        this.type = type.equals("intruder") ? new Intruder(this) : new models.ParticipantType.Participant(this);
    }

    @Subscribe
    public void receiveMessage(BusMessage message){
        type.receiveMessage(message);
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type.toString();
    }
}
