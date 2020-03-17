package gopur.plugin;

import java.io.File;
import java.util.regex.Pattern;

public interface PluginLoader {
    /**
     * 通过插件名称加载插件
     * @param filename
     * @return 加载后的插件对象
     * @throws Exception
     */
    Plugin loadPlugin(String filename) throws Exception;

    /**
     * 通过File对象加载插件
     * @param file
     * @return 加载后的插件对象
     * @throws Exception
     */
    Plugin loadPlugin(File file) throws Exception;

    /**
     * 通过插件名称获取插件的描述
     * @param filename
     * @return 插件的描述
     */
    PluginDescription getPluginDescription(String filename);

    /**
     * 通过File对象获取插件的描述
     * @param file
     * @return 插件的描述
     */
    PluginDescription getPluginDescription(File file);

    /**
     * 启用一个插件
     * @param plugin
     */
    void enablePlugin(Plugin plugin);

    /**
     * 停用一个插件
     * @param plugin
     */
    void disablePlugin(Plugin plugin);

    Pattern[] getPluginFilters();
}
