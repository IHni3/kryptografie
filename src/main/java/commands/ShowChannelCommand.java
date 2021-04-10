/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import database.DBService;

public class ShowChannelCommand implements ICommand{
    @Override
    public String execute() {
        var channelList = DBService.instance.getChannels();

        StringBuilder returnString = new StringBuilder();

        for (var channel : channelList) {
            returnString.append(String.format("%s | %s and %s\n", channel.getName(), channel.getSender().getName(), channel.getReceiver().getName()));
        }

        if(channelList.isEmpty())
            return "channel list empty!";

        return returnString.toString();
    }
}
