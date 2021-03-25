/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import database.DBService;

public class DropChannelCommand implements ICommand{
    private String channelName;
    public DropChannelCommand(String channelName) {

    }

    @Override
    public String execute() throws CommandExecutionException {
        var channel = DBService.instance.getChannel(channelName);

        if (channel == null){
            throw new CommandExecutionException(String.format("unknown channel %s", channelName));
        }

        if (!DBService.instance.removeChannel(channelName)){
            throw new CommandExecutionException("Something went wrong");
        }

        return null;
    }
}
