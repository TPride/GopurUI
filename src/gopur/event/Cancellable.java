package gopur.event;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled();
    void setCancelled(boolean cancel);
}
