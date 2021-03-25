package parser;

import commands.CrackMessageShiftCommand;
import commands.ICommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrackShiftParser implements IParser{

    private static final String regex = "crack encrypted message \"(.+)\" using shift";
    private Pattern pattern = null;

    @Override
    public ICommand parse(String input) throws ParserException {
        Matcher matcher = getPattern().matcher(input);

        if(!matcher.find()) {
            throw new ParserException();
        }
        String messageStr = matcher.group(1);


        ICommand command = new CrackMessageShiftCommand(messageStr);

        return command;
    }

    private Pattern getPattern() {
        if(pattern == null)
            pattern = Pattern.compile(regex);
        return pattern;
    }
}
