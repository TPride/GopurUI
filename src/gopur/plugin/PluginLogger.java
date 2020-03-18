package gopur.plugin;

import gopur.Gopur;

public class PluginLogger implements Logger {
    private final Plugin plugin;

    public PluginLogger(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void info(String msg, int times) {
        String next = "";
        if (times > 0)
            for (int i = 0; i < times; i++)
                next += "\n";
        Gopur.getInstance().getCommandWindow().print("Gopur[" + plugin.getPluginDescription().getName() + "] > " + msg + next);
    }

    @Override
    public void info(String msg) {
        info(msg, 1);
    }
}
