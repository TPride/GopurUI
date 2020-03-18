package gopur.command.gopur;

import gopur.Gopur;
import gopur.command.Command;
import gopur.plugin.Plugin;

import java.util.Iterator;
import java.util.Map;

public class PlCommand extends Command {
    public PlCommand() {
        super("pl", "查看已加载的插件", "pl");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        String result = "";
        Map<String, Plugin> plugins = Gopur.getInstance().getPluginManager().getPlugins();
        int i = 0;
        for (Iterator<Plugin> iterator = plugins.values().iterator(); iterator.hasNext();) {
            i++;
            Plugin plugin = iterator.next();
            result += plugin.getPluginDescription().getFullName() + (i + 1 >= plugins.size() ? "" : ",");
        }
        Gopur.getLogger().info("已加载插件(" + i + "): " + result, 2);
        return true;
    }
}
