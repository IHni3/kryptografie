/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import configuration.Configuration;
import database.DBService;

public class DropChannelCommand implements ICommand{
    private String channelName;
    public DropChannelCommand(String channelName) {

    }

    @Override
    public String execute() throws CommandExecutionException {
        var channel = DBService.instance.getChannel(channelName);

        if (channel == null){
            Configuration.instance.textAreaLogger.info(String.format("unknown channel %s", channelName));
        }

        if (!DBService.instance.removeChannel(channelName)){
            Configuration.instance.textAreaLogger.info("Something went wrong");
        }

        return null;
    }
}
