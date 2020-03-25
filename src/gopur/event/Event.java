package gopur.event;

import gopur.utils.EventException;

public class Event {
    protected String eventName = null;
    private boolean isCancelled = false;

    public final String getEventName() {
        return eventName == null ? getClass().getName() : eventName;
    }

    public boolean isCancelled() {
        if (!(this instanceof Cancellable))
            throw new EventException(getEventName() + "事件不可取消");
        return isCancelled;
    }

    public void setCancelled() {
        setCancelled(true);
    }

    public void setCancelled(boolean cancelled) {
        if (!(this instanceof Cancellable))
            throw new EventException(getEventName() + "事件不可取消");
        isCancelled = cancelled;
    }
}
