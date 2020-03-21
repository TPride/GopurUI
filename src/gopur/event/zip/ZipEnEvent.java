package gopur.event.zip;

import gopur.event.Cancellable;
import gopur.event.Event;
import gopur.event.HandlerList;

public class ZipEnEvent extends Event implements Cancellable {
    private final String filePath, destion, password;
    private static final HandlerList handlers = new HandlerList();

    public ZipEnEvent(final String filePath, final String destion, final String password) {
        this.filePath = filePath;
        this.destion = destion;
        this.password = password;
    }

    public final String getFilePath() {
        return filePath;
    }

    public final String getDestion() {
        return destion;
    }

    public final String getPassword() {
        return password;
    }

    public final boolean isEmptyPassword() {
        return password == null;
    }

    public static HandlerList getHandlers() {
        return handlers;
    }
}
