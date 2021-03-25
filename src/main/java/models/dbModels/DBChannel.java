package models.dbModels;


import com.google.common.eventbus.EventBus;

public class DBChannel {

    private final String name;
    private final DBParticipant participantA;
    private final DBParticipant participantB;
    private final EventBus eventBus = new EventBus();

    public DBChannel(String name, DBParticipant participantA, DBParticipant participantB){
        this.name = name;
        this.participantA = participantA;
        this.participantB = participantB;
        eventBus.register(participantA);
        eventBus.register(participantB);
    }

    public String getName(){
        return this.name;
    }

    public DBParticipant getParticipantA(){
        return this.participantA;
    }

    public DBParticipant getParticipantB(){
        return this.participantB;
    }

}
