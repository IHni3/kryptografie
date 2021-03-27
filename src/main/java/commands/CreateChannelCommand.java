/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import database.DBService;
import models.Channel;

public class CreateChannelCommand implements ICommand{
    private final String name01;
    private final String name02;
    private final String channelName;

    public CreateChannelCommand(String channelName, String name01, String name02) {
        this.channelName = channelName;
        this.name01 = name01;
        this.name02 = name02;
    }

    @Override
    public String execute() throws CommandExecutionException {

        if (DBService.instance.getChannel(channelName) != null){
            throw new CommandExecutionException(String.format("Channel %s already exists", channelName));
        }

        if (DBService.instance.getChannel(name01, name02) != null){
            throw new CommandExecutionException(String.format("Communication channel between %s and %s already exists", name01, name02));
        }

        if (name01.equals(name02)){
            throw new CommandExecutionException(String.format("%s and %s identical - cannot create channel on itself", name01, name02));
        }

        var part1 = DBService.instance.getOneParticipant(name01);
        if(part1 == null)
            throw new CommandExecutionException(String.format("Participant %s does not exist", name01));

        var part2 = DBService.instance.getOneParticipant(name02);
        if(part2 == null)
            throw new CommandExecutionException(String.format("Participant %s does not exist", name02));

        Channel channel = new Channel(channelName, part1, part2);
        DBService.instance.insertChannel(channel);


        return String.format("channel %s from %s to %s successfully created", channelName, name01, name02);
    }
}
