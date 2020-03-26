package gopur.plugin;

import gopur.Gopur;
import gopur.Information;
import gopur.event.Event;
import gopur.event.EventHandler;
import gopur.event.HandlerList;
import gopur.event.Listener;
import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Pattern;

public class PluginManager {
    protected final Map<String, Plugin> plugins = new LinkedHashMap<>();
    protected final Map<String, PluginLoader> fileAssociations = new HashMap<>();

    public PluginManager() {

    }

    public Plugin getPlugin(String name) {
        return plugins.getOrDefault(name, null);
    }

    public Map<String, Plugin> getPlugins() {
        return plugins;
    }

    public boolean registerInterface(Class<? extends PluginLoader> loadedClass) {
        if (loadedClass != null) {
            try {
                Constructor constructor = loadedClass.getDeclaredConstructor();
                constructor.setAccessible(true);
                fileAssociations.put(loadedClass.getName(), (PluginLoader)constructor.newInstance());
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    public Plugin loadPlugin(File file, Map<String, PluginLoader> loaders) {
        for (PluginLoader loader : (loaders == null ? fileAssociations : loaders).values()) {
            for (Pattern pattern : loader.getPluginFilters()) {
                if (pattern.matcher(file.getName()).matches()) {
                    PluginDescription description = loader.getPluginDescription(file);
                    if (description != null) {
                        try {
                            Plugin plugin = loader.loadPlugin(file);
                            if (plugin != null) {
                                plugins.put(plugin.getPluginDescription().getName(), plugin);
                                return plugin;
                            }
                        } catch (Exception e) {
                            Gopur.getLogger().info("无法加载插件" + file.getName());
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void loadPlugins() {
        File[] files = new File(Information.NOWPATH + "plugins").listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory())
                continue;
            Plugin plugin = loadPlugin(files[i], null);
        }
    }

    public void enablePlugin(Plugin plugin) {
        try {
            plugin.getPluginLoader().enablePlugin(plugin);
        } catch (Exception e) {
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }

    public void enablePlugins(PluginLoadOrder pluginLoadOrder) {
        for (Plugin plugin : new ArrayList<>(plugins.values())) {
            if (!plugin.isEnabled() && plugin.getPluginDescription().getOrder() == pluginLoadOrder)
                enablePlugin(plugin);
        }
    }

    public void enablePlugins() {
        enablePlugins(PluginLoadOrder.STARTUP);
        enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    public void disablePlugin(Plugin plugin) {
        if (plugin.isEnabled())
            plugin.getPluginLoader().disablePlugin(plugin);
    }

    public void disablePlugins() {
        for (Iterator<Plugin> iterator = plugins.values().iterator(); iterator.hasNext();) {
            Plugin plugin = iterator.next();
            Gopur.getLogger().info("正在停用 " + plugin.getPluginDescription().getFullName() + "...");
            disablePlugin(plugin);
        }
    }

    public void callEvent(Event event) {
        try {
            for (RegisterdListener registration : getEventListeners(event.getClass()).getRegitserListeners()) {
                if (!registration.getPlugin().isEnabled())
                    continue;

                try {
                    registration.callEvent(event);
                } catch (Exception e) {
                    Gopur.getLogger().info("[ERROR] 事件错误: " + registration.getListener().getClass().getName());
                }
            }
        } catch (IllegalAccessException e) {
            Gopur.getLogger().info(e.toString());
        }
    }

    public void registerEvents(Listener listener, Plugin plugin) {
        if (!plugin.isEnabled())
            Gopur.getLogger().info("[WARNING] 插件试图注册" + listener.getClass().getName() + ", 但没有启用");

        Map<Class<? extends Event>, Set<RegisterdListener>> ret = new HashMap<>();
        Set<Method> methods;
        try {
            Method[] publicMethods = listener.getClass().getMethods();
            Method[] privateMethods = listener.getClass().getDeclaredMethods();
            methods = new HashSet<>(publicMethods.length + privateMethods.length, 1.0f);
            Collections.addAll(methods, publicMethods);
            Collections.addAll(methods, privateMethods);
        } catch (NoClassDefFoundError e) {
            plugin.getLogger().info("[ERROR] 插件 " + plugin.getPluginDescription().getFullName() + " 由于不存在 " + listener.getClass() + "而未能为注册事件: " + e.getMessage());
            return;
        }

        for (final Method method : methods) {
            final EventHandler eh = method.getAnnotation(EventHandler.class);
            if (eh == null) continue;
            if (method.isBridge() || method.isSynthetic()) {
                continue;
            }
            final Class<?> checkClass;

            if (method.getParameterTypes().length != 1 || !Event.class.isAssignableFrom(checkClass = method.getParameterTypes()[0]))
                continue;

            final Class<? extends Event> eventClass = checkClass.asSubclass(Event.class);
            method.setAccessible(true);
            registerEvent(eventClass, listener, plugin, new MethodEventExecutor(method));
        }
    }

    public void registerEvent(Class<? extends Event> event, Listener listener, Plugin plugin, EventExecutor executor) {
        if (!plugin.isEnabled())
            Gopur.getLogger().info("[WARNING] 插件试图注册" + event + ", 但没有启用");

        try {
            this.getEventListeners(event).register(new RegisterdListener(listener, plugin, executor));
        } catch (IllegalAccessException e) {
            Gopur.getLogger().info("[ERROR] " + e.toString());
        }
    }

    public void clearPlugins() {
        plugins.clear();
        fileAssociations.clear();
    }

    private HandlerList getEventListeners(Class<? extends Event> type) throws IllegalAccessException {
        try {
            Method method = getRegistrationClass(type).getDeclaredMethod("getHandlers");
            method.setAccessible(true);
            return (HandlerList) method.invoke(null);
        } catch (Exception e) {
            Gopur.getLogger().info("[ERROR] " + e.toString());
        }
        return null;
    }

    private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) throws IllegalAccessException {
        try {
            clazz.getDeclaredMethod("getHandlers");
            return clazz;
        } catch (NoSuchMethodException e) {
            if (clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass()))
                return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
            else
                throw new IllegalAccessException("无法找到事件 " + clazz.getName() + " 静态getHandlers方法所需的处理程序列表");
        }
    }
}
