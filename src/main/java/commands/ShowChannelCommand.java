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
            returnString.append(String.format("%1 | %2 and %3", channel.getName(), channel.getParticipantA(), channel.getParticipantB()));
        }

        return returnString.toString();
    }
}
