package parser;

import commands.ICommand;

public interface IParser {
    ICommand parse(final String input) throws ParserException;
}
