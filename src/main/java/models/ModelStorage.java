package models;

import java.util.ArrayList;
import java.util.List;

public enum ModelStorage {
    instance;

    private List<Channel> channels = new ArrayList<>();
    private List<Participant> participants = new ArrayList<>();

    public List<Channel> getChannels() {
        return channels;
    }

    public List<Participant> getParticipants() {
        return participants;
    }
}
