/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import configuration.ParticipantType;
import database.DBService;
import models.Participant;

public class RegisterParticipantCommand implements ICommand{

    private String name;
    private ParticipantType type;

    public RegisterParticipantCommand(String name, ParticipantType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String execute() throws CommandExecutionException {

        if (DBService.instance.getOneParticipant(name) != null){
            throw new CommandExecutionException(String.format("participant %s already exists, using existing postbox_%s", name, name));
        }

        Participant participant = new Participant(name, type.toString());
        DBService.instance.insertParticipant(participant);

        return "participant "+name+" with type " + type.name() + " registered and postbox_" + name + " created";
    }
}
