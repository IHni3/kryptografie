/*
 * Copyright (c) 2021.
 * Author 6143217
 * All rights reserved
 */

package commands;

public class CommandExecutionException extends Exception{
    public CommandExecutionException(String message, Throwable cause) {
        super(message, cause);
    }
    public CommandExecutionException(String message) {
        super(message);
    }
}
