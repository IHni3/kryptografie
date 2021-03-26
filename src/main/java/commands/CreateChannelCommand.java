/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import configuration.Configuration;
import database.DBService;
import models.Channel;

public class CreateChannelCommand implements ICommand{
    String part1name;
    String part2name;
    String channelName;

    public CreateChannelCommand(String channelName, String name01, String name02) {

    }

    @Override
    public String execute() throws CommandExecutionException {

        if (DBService.instance.getChannel(channelName) != null){
            Configuration.instance.textAreaLogger.info(String.format("channel %s already exists", channelName));
        }

        if (DBService.instance.getChannel(part1name, part2name) != null){
            Configuration.instance.textAreaLogger.info(String.format("communication channel between %s and %s already exists", part1name, part2name));
        }

        if (part1name.equals(part2name)){
            Configuration.instance.textAreaLogger.info(String.format("%s and %s identical - cannot create channel on itself", part1name, part2name));
        }

        var part1 = DBService.instance.getOneParticipant(part1name);
        var part2 = DBService.instance.getOneParticipant(part2name);

        Channel channel = new Channel(channelName, part1, part2);

        DBService.instance.insertChannel(channel);
        return null;
    }
}
