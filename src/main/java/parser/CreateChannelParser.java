/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package parser;

import commands.CreateChannelCommand;
import commands.ICommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateChannelParser implements IParser{

    private static final String regex = "create channel (\\S+) from (\\S+) to (\\S+)";
    private Pattern pattern = null;

    @Override
    public ICommand parse(String input) throws ParserException {
        Matcher matcher = getPattern().matcher(input);

        if(!matcher.find()) {
            throw new ParserException();
        }
        String channelName = matcher.group(1);
        String name01 = matcher.group(2);
        String name02 = matcher.group(3);


        ICommand command = new CreateChannelCommand(channelName, name01, name02);

        return command;
    }

    private Pattern getPattern() {
        if(pattern == null)
            pattern = Pattern.compile(regex);
        return pattern;
    }
}
