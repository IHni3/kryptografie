package parser;

import commands.DecryptMessageCommand;
import commands.ICommand;
import configuration.AlgorithmType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecryptParser implements IParser{

    private static final String regex = "decrypt message \"(.+)\" using (\\S+) and keyfile (\\S+)";
    private Pattern pattern = null;

    @Override
    public ICommand parse(String input) throws ParserException {
        Matcher matcher = getPattern().matcher(input);

        if(matcher.find() == false) {
            throw new ParserException();
        }
        String messageStr = matcher.group(1);
        String algorithmStr = matcher.group(2);
        String keyfileStr = matcher.group(3);

        var algorithm = ParserUtils.parseAlgorithm(algorithmStr);


        ICommand command = new DecryptMessageCommand(messageStr, algorithm, keyfileStr);

        return command;
    }

    private Pattern getPattern() {
        if(pattern == null)
            pattern = Pattern.compile(regex);
        return pattern;
    }


}
