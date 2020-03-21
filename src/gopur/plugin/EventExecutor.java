package gopur.plugin;

import gopur.event.Event;
import gopur.utils.EventException;
import gopur.event.Listener;

public interface EventExecutor {
    void execute(Listener listener, Event event) throws EventException;
}
