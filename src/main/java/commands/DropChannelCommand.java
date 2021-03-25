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
        var result = DBService.instance.getChannels().stream().filter(c -> c.getName().equals(channelName));

        if (result.count() == 0){
            throw new CommandExecutionException(String.format("unknown channel %s", channelName));
        }


        return null;
    }
}
