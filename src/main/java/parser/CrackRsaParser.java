/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package parser;

import commands.CrackMessageRsaCommand;
import commands.ICommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrackRsaParser implements IParser{

    private static final String regex = "crack encrypted message \"(.+)\" using rsa and keyfile (\\S+)";
    private Pattern pattern = null;

    @Override
    public ICommand parse(String input) throws ParserException {
        Matcher matcher = getPattern().matcher(input);

        if(!matcher.find()) {
            throw new ParserException();
        }
        String messageStr = matcher.group(1);
        String keyfileStr = matcher.group(2);

        ICommand command = new CrackMessageRsaCommand(messageStr, keyfileStr);

        return command;
    }

    private Pattern getPattern() {
        if(pattern == null)
            pattern = Pattern.compile(regex);
        return pattern;
    }
}
