/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import database.DBService;

public class IntrudeChannelCommand implements ICommand {

    private final String channelName;
    private final String intruderName;

    public IntrudeChannelCommand(String channelName, String intruderName) {
        this.channelName = channelName;
        this.intruderName = intruderName;
    }

    @Override
    public String execute() throws CommandExecutionException {

        var intruder = DBService.instance.getOneParticipant(intruderName);

        if (intruder == null) {
            return String.format("intruder %s could not be found", intruderName);
        }

        if (!intruder.getType().equals("intruder")) {
            return String.format("%s is no intruder", intruderName);
        }

        var channel = DBService.instance.getChannel(channelName);
        if (channel == null) {
            return String.format("channel %s could not be found", channelName);
        }

        channel.intrude(intruder);

        return String.format("intruder %s registered for channel %s", intruderName, channelName);
    }
}
