/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

import configuration.Configuration;
import database.DBService;

import java.util.stream.Collectors;

public class IntrudeChannelCommand implements ICommand {

    private String channelName;
    private String participant;

    public IntrudeChannelCommand(String channelName, String participant) {
        this.channelName = channelName;
        this.participant = participant;
    }

    @Override
    public String execute() throws CommandExecutionException {

        var intruder = DBService.instance.getOneParticipant(participant);

        if (intruder == null){
            Configuration.instance.textAreaLogger.info(String.format("intruder %s could not be found", participant));
        }

        var channel = DBService.instance.getChannel(channelName);

        if (channel == null){
            Configuration.instance.textAreaLogger.info(String.format("channel %s could not be found", channelName));
        }

        channel.intrude(intruder);

        return null;
    }
}
