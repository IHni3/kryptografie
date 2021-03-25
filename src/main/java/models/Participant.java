package models;

import com.google.common.eventbus.Subscribe;
import models.dbModels.DBParticipant;

public class Participant extends DBParticipant {
    public  Participant(DBParticipant participant){
        super(participant.getName(), participant.getName());
    }

    @Subscribe
    public void receive(){

    }
}