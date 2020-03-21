package gopur.event.command;

import gopur.event.Cancellable;
import gopur.event.Event;
import gopur.event.HandlerList;

public class InputCommandEvent extends Event implements Cancellable {
    private final String fullLine, commandName;
    private final String[] args;
    private static final HandlerList handlers = new HandlerList();

    public InputCommandEvent(final String fullLine, final String commandName, final String[] args) {
        this.fullLine = fullLine;
        this.commandName = commandName;
        this.args = args;
    }

    public final String getFullLine() {
        return fullLine;
    }

    public final String getCommandName() {
        return commandName;
    }

    public final String[] getArgs() {
        return args;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
