/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package parser;

import commands.ICommand;
import commands.ShowChannelCommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowChannelParser implements IParser{

    private static final String regex = "show channel";
    private Pattern pattern = null;

    @Override
    public ICommand parse(String input) throws ParserException {
        Matcher matcher = getPattern().matcher(input);

        if(!matcher.find()) {
            throw new ParserException();
        }

        ICommand command = new ShowChannelCommand();

        return command;
    }

    private Pattern getPattern() {
        if(pattern == null)
            pattern = Pattern.compile(regex);
        return pattern;
    }
}
