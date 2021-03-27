/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package parser;

import commands.ICommand;
import configuration.Configuration;

import java.util.Arrays;
import java.util.List;

public class Parser implements IParser {

    private List<IParser> parsers = null;

    private List<IParser> getParsers() {
        if(parsers == null) {
            parsers = Arrays.asList(new CrackRsaParser(),
                    new CrackShiftParser(),
                    new ShowChannelParser(),
                    new SendMessageParser(),
                    new RegisterParticipantParser(),
                    new IntrudeChannelParser(),
                    new CreateChannelParser(),
                    new DecryptParser(),
                    new EncryptParser(),
                    new DropChannelParser());
        }
       return parsers;
    }

    @Override
    public ICommand parse(String input) throws ParserException {
        ICommand command;
        for (var curParser : getParsers()) {
            try {
                command = curParser.parse(input);
                return command;
            } catch (ParserException exception) {
                Configuration.instance.getLogger().printDebug(curParser.getClass().getName() + " failed!");
            }
        }
        throw new ParserException();
    }
}
