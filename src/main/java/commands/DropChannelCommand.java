package commands;

import database.MSADBService;

public class DropChannelCommand implements ICommand{
    private String channelName;
    public DropChannelCommand(String channelName) {

    }

    @Override
    public String execute() throws CommandExecutionException {
        var result = MSADBService.instance.getChannels().stream().filter(c -> c.getName().equals(channelName));

        if (result.count() == 0){
            throw new CommandExecutionException(String.format("unknown channel %s", channelName));
        }


        return null;
    }
}
