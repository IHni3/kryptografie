/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

public interface ICommand {
    String execute() throws CommandExecutionException;
}
