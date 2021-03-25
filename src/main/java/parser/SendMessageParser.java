
package parser;


import commands.ICommand;
import commands.SendMessageCommand;
import configuration.AlgorithmType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendMessageParser implements IParser{

    private static final String regex = "send message \"(.+)\" from (\\S+) to (\\S+) using (\\S+) and keyfile (\\S+)";
    private Pattern pattern = null;

    @Override
    public ICommand parse(String input) throws ParserException {
        Matcher matcher = getPattern().matcher(input);

        if(!matcher.find()) {
            throw new ParserException();
        }
        String message = matcher.group(1);
        String name01 = matcher.group(2);
        String name02 = matcher.group(3);
        String algorithmStr = matcher.group(4);
        String keyfile = matcher.group(5);

        AlgorithmType algorithm = ParserUtils.parseAlgorithm(algorithmStr);


        ICommand command = new SendMessageCommand(message, name01,name02,algorithm,keyfile);

        return command;
    }

    private Pattern getPattern() {
        if(pattern == null)
            pattern = Pattern.compile(regex);
        return pattern;
    }
}
