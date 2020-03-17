package gopur.plugin;

import gopur.Gopur;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PluginManager {
    protected final Map<String, Plugin> plugins = new LinkedHashMap<>();
    protected final Map<String, PluginLoader> fileAssociations = new HashMap<>();

    public PluginManager() {

    }

    public Plugin getPlugin(String name) {
        return plugins.containsKey(name) ? plugins.get(name) : null;
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
                            Gopur.getInstance().commandWindow.GopurPrint("无法加载插件" + file.getName() + "'\n");
                            return null;
                        }
                    }
                }
            }
        }
        return null;
    }

    public void enablePlugin(Plugin plugin) {
        try {
            plugin.getPluginLoader().enablePlugin(plugin);
        } catch (Exception e) {
            plugin.getPluginLoader().disablePlugin(plugin);
        }
    }
}
