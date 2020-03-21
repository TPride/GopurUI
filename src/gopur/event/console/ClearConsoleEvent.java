package gopur.event.console;

import gopur.event.Cancellable;
import gopur.event.Event;
import gopur.event.HandlerList;

public class ClearConsoleEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    public ClearConsoleEvent() {

    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
