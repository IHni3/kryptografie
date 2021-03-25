
package parser;


import commands.ICommand;
import commands.IntrudeChannelCommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntrudeChannelParser implements IParser{

    private static final String regex = "intrude channel (\\S+) by (\\S+)";
    private Pattern pattern = null;

    @Override
    public ICommand parse(String input) throws ParserException {
        Matcher matcher = getPattern().matcher(input);

        if(!matcher.find()) {
            throw new ParserException();
        }
        String channelName = matcher.group(1);
        String participant = matcher.group(2);


        ICommand command = new IntrudeChannelCommand(channelName, participant);

        return command;
    }

    private Pattern getPattern() {
        if(pattern == null)
            pattern = Pattern.compile(regex);
        return pattern;
    }
}
