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
    private final Participant sender;
    private final Participant receiver;
    private final EventBus eventBus;

    public Channel(String name, Participant sender, Participant receiver) {
        this.name = name;
        this.sender = sender;
        this.receiver = receiver;
        this.eventBus = new EventBus();

        //eventBus.register(sender);
        eventBus.register(receiver);

        if (Configuration.instance.intrudedChannels.containsKey(name))
            for (var intruderName : Configuration.instance.intrudedChannels.get(name)) {
                var intruder = DBService.instance.getOneParticipant(intruderName);
                eventBus.register(intruder);
            }
    }

    public String getName(){
        return this.name;
    }

    public Participant getSender(){
        return this.sender;
    }

    public Participant getReceiver(){
        return this.receiver;
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
//send message "test" from branch_hkg to branch_wuh using shift and keyfile shift6.json