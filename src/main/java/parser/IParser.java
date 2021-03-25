/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package parser;

import commands.ICommand;

public interface IParser {
    ICommand parse(final String input) throws ParserException;
}
