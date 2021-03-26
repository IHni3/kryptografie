/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package models;

import com.google.common.eventbus.Subscribe;

public class Participant {
    private final String name;
    private final String type;

    public Participant(String name, String type){
        this.name = name;
        this.type = type;
    }

    @Subscribe
    public void receiveMessage(){

    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
