package gopur.event;

import gopur.plugin.Plugin;
import gopur.plugin.RegisterdListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class HandlerList {
    private volatile RegisterdListener[] hanlders = null;
    private final ArrayList<RegisterdListener> handlerslots = new ArrayList<>();
    private static final ArrayList<HandlerList> all = new ArrayList<>();

    public static void unregisterAll() {
        synchronized (all) {
            for (HandlerList handlerList : all) {
                synchronized (handlerList) {
                    handlerList.handlerslots.clear();
                    handlerList.hanlders = null;
                }
            }
        }
    }

    public static void unregisterAll(Plugin plugin) {
        synchronized (all) {
            for (HandlerList handlerList : all)
                handlerList.unregister(plugin);
        }
    }

    public static void unregisterAll(Listener listener) {
        synchronized (all) {
            for (HandlerList h : all)
                h.unregister(listener);
        }
    }

    public static void bakeAll() {
        synchronized (all) {
            for (HandlerList handlerList : all)
                handlerList.bake();
        }
    }

    public HandlerList() {
        synchronized (all) {
            all.add(this);
        }
    }

   public synchronized void register(RegisterdListener listener) {
        if (handlerslots.contains(listener))
            return;
        hanlders = null;
        handlerslots.add(listener);
   }

   public void registerAll(Collection<RegisterdListener> listeners) {
        for (RegisterdListener listener : listeners)
            register(listener);
   }

   public synchronized void unregister(RegisterdListener listener) {
        if (handlerslots.remove(listener))
            hanlders = null;
   }

   public synchronized void unregister(Plugin plugin) {
        boolean changed = false;
       for (Iterator<RegisterdListener> iterator = handlerslots.iterator(); iterator.hasNext();) {
           if (iterator.next().getPlugin().equals(plugin)) {
               iterator.remove();
               changed = true;
           }
       }
       if (changed)
           hanlders = null;
   }

   public synchronized void unregister(Listener listener) {
       boolean changed = false;
       for (Iterator<RegisterdListener> iterator = handlerslots.iterator(); iterator.hasNext();) {
           if (iterator.next().getListener().equals(listener)) {
               iterator.remove();
               changed = true;
           }
       }
       if (changed)
           hanlders = null;
   }

   public synchronized void bake() {
        if (hanlders != null)
            return;
       List<RegisterdListener> registerdListeners = new ArrayList<>();
       registerdListeners.addAll(handlerslots);
       hanlders = registerdListeners.toArray(new RegisterdListener[registerdListeners.size()]);
   }

   public RegisterdListener[] getRegitserListeners() {
        RegisterdListener[] handlers;
        while ((handlers = this.hanlders) == null)
            bake();
        return handlers;
   }

   public static ArrayList<RegisterdListener> getRegisterListeners(Plugin plugin) {
        ArrayList<RegisterdListener> result = new ArrayList<>();
        synchronized (all) {
            for (HandlerList h : all) {
                synchronized (h) {
                    for (RegisterdListener listener : h.handlerslots)
                        if (listener.getPlugin().equals(plugin))
                            result.add(listener);
                }
            }
        }
        return result;
   }

   public static ArrayList<HandlerList> getHandlerLists() {
        synchronized (all) {
            return all;
        }
   }
}
