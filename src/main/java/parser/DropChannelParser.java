/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package parser;

import commands.DropChannelCommand;
import commands.ICommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DropChannelParser implements IParser{

    private static final String regex = "drop channel (\\S+)";
    private Pattern pattern = null;

    @Override
    public ICommand parse(String input) throws ParserException {
        Matcher matcher = getPattern().matcher(input);

        if(!matcher.find()) {
            throw new ParserException();
        }
        String channelName = matcher.group(1);


        ICommand command = new DropChannelCommand(channelName);

        return command;
    }

    private Pattern getPattern() {
        if(pattern == null)
            pattern = Pattern.compile(regex);
        return pattern;
    }
}
