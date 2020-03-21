package gopur.plugin;

import gopur.event.Cancellable;
import gopur.event.Event;
import gopur.event.Listener;
import gopur.utils.EventException;

public class RegisterdListener {
    private final Listener listener;
    private final Plugin plugin;
    private final EventExecutor executor;

    public RegisterdListener(Listener listener, Plugin plugin, EventExecutor executor) {
        this.listener = listener;
        this.plugin = plugin;
        this.executor = executor;
    }

    public Listener getListener() {
        return listener;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public EventExecutor getExecutor() {
        return executor;
    }

    public void callEvent(Event event) throws EventException {
        if (event instanceof Cancellable) {
            if (event.isCancelled())
                return;
        }
        executor.execute(listener, event);
    }
}
