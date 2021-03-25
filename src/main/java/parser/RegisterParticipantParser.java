
package parser;

import commands.ICommand;
import commands.RegisterParticipantCommand;
import configuration.ParticipantType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterParticipantParser implements IParser{

    private static final String regex = "register participant (\\S+) with type (normal|intruder)";
    private Pattern pattern = null;

    @Override
    public ICommand parse(String input) throws ParserException {
        Matcher matcher = getPattern().matcher(input);

        if(!matcher.find()) {
            throw new ParserException();
        }
        String nameStr = matcher.group(1);
        String typeStr = matcher.group(2);

        ParticipantType type = parseType(typeStr);

        ICommand command = new RegisterParticipantCommand(nameStr, type);

        return command;
    }

    private Pattern getPattern() {
        if(pattern == null)
            pattern = Pattern.compile(regex);
        return pattern;
    }

    private ParticipantType parseType(final String typeStr) throws ParserException {
        for ( ParticipantType value :ParticipantType.values()) {
            if(value.name().equalsIgnoreCase(typeStr)) {
                return value;
            }
        }
        throw new ParserException();
    }
}
