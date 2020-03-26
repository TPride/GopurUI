package gopur.plugin;

import gopur.Gopur;
import gopur.utils.PluginException;
import gopur.utils.Utils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

public class JarPluginLoader implements PluginLoader {
    private final Map<String, Class> classMap = new HashMap<>();
    private final Map<String, PluginClassLoader> classLoaderMap = new HashMap<>();

    public JarPluginLoader() {

    }

    @Override
    public Plugin loadPlugin(File file) throws Exception {
        PluginDescription description = getPluginDescription(file);
        if (description != null) {
            Gopur.getLogger().info("正在加载 " + description.getFullName() + "...");
            File dataFolder = new File(file.getParentFile(), description.getName());
            if (dataFolder.exists() && !dataFolder.isDirectory())
                throw new IllegalStateException(description.getName() + "的数据文件夹" + dataFolder.toString() + "存在, 但不是一个目录");
            String className = description.getMain();
            PluginClassLoader classLoader = new PluginClassLoader(this, getClass().getClassLoader(), file);
            classLoaderMap.put(description.getName(), classLoader);
            PluginBase pluginBase;
            try {
                Class javaClass = classLoader.findClass(className);
                if (!PluginBase.class.isAssignableFrom(javaClass))
                    throw new PluginException("主类 `" + description.getMain() + "` 没有继承PluginBase类");
                try {
                    Class<PluginBase> pluginBaseClass = (Class<PluginBase>) javaClass.asSubclass(PluginBase.class);
                    pluginBase = pluginBaseClass.newInstance();
                    initPlugin(pluginBase, description, dataFolder);
                    return pluginBase;
                } catch (ClassCastException e) {
                    throw new PluginException("初始化主类" + description.getMain() + "时出错");
                } catch (InstantiationException | IllegalAccessException e) {
                    Gopur.getLogger().info(e.getMessage());
                }
            } catch (ClassNotFoundException e) {
                throw new PluginException("无法加载插件" + description.getMain() + ", 因为身为主类的它不存在");
            }
        }
        return null;
    }

    @Override
    public Plugin loadPlugin(String filename) throws Exception {
        return loadPlugin(new File(filename));
    }

    @Override
    public PluginDescription getPluginDescription(File file) {
        try (JarFile jar = new JarFile(file)){
            JarEntry entry = jar.getJarEntry("plugin.yml");
            if (entry == null)
                return null;
            try (InputStream stream = jar.getInputStream(entry)) {
                return new PluginDescription(Utils.readFile(stream));
            }
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public PluginDescription getPluginDescription(String filename) {
        return getPluginDescription(new File(filename));
    }

    @Override
    public Pattern[] getPluginFilters() {
        return new Pattern[]{Pattern.compile("^.+\\.jar$")};
    }

    @Override
    public void enablePlugin(Plugin plugin) {
        if (plugin instanceof PluginBase && !plugin.isEnabled()) {
            Gopur.getLogger().info("正在启用 " + ((PluginBase) plugin).getPluginDescription().getFullName() + "...");
            ((PluginBase) plugin).setEnabled(true);
        }
    }

    private void initPlugin(PluginBase pluginBase, PluginDescription description, File dataFolder) {
        pluginBase.init(this, description, dataFolder);
        dataFolder.mkdirs();
        pluginBase.onLoad();
    }

    @Override
    public void disablePlugin(Plugin plugin) {
        if (plugin instanceof PluginBase && plugin.isEnabled()) {
            Gopur.getLogger().info("正在停用 " + ((PluginBase) plugin).getPluginDescription().getFullName() + "...");
            ((PluginBase) plugin).setEnabled(false);
            removeClass(plugin.getPluginDescription().getName());
        }
    }

    public Class<?> getClassByName(String name) {
        Class<?> cachedClass = classMap.get(name);
        if (cachedClass != null)
            return cachedClass;
        for (PluginClassLoader loader : classLoaderMap.values()) {
            try {
                cachedClass = loader.findClass(name, false);
            } catch (ClassNotFoundException e) {
                //no-code
            }
            if (cachedClass != null)
                return cachedClass;
        }
        return null;
    }

    public void setClass(final String name, final Class<?> clazz) {
        if (!classMap.containsKey(name))
            classMap.put(name, clazz);
    }

    private void removeClass(String name) {
        classMap.remove(name);
    }
}
