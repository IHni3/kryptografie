package models.ParticipantType;

import models.BusMessage;

public interface IParticipantType {
    public void receiveMessage(BusMessage message);
    public String toString();
}
