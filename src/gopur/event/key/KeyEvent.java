package gopur.event.key;

import gopur.event.Cancellable;
import gopur.event.Event;
import gopur.event.HandlerList;

public class KeyEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    private final java.awt.event.KeyEvent keyEvent;

    public KeyEvent(java.awt.event.KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
    }

    public final java.awt.event.KeyEvent getKeyEvent() {
        return keyEvent;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
