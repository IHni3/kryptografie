package commands;

import models.ModelStorage;

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

        var resultParticipants = ModelStorage.instance.getParticipants().stream().filter(p -> p.getName().equals(participant));

        if (resultParticipants.count() == 0){
            throw new CommandExecutionException(String.format("intruder %s could not be found", participant));
        }

        var resultChannels = ModelStorage.instance.getChannels().stream().filter(c -> c.getName().equals(channelName));

        if (resultChannels.count() == 0){
            throw new CommandExecutionException(String.format("channel %s could not be found", channelName));
        }

        var channel = resultChannels.collect(Collectors.toList()).get(0);

        var intruder = resultParticipants.collect(Collectors.toList()).get(0);

        channel.intrude(intruder);

        return null;
    }
}
