package gopur.plugin;

public interface Plugin {
    void onLoad();
    void onEnable();
    void onDisable();
    boolean isEnabled();
    boolean isDisabled();
    PluginDescription getPluginDescription();
    PluginLoader getPluginLoader();
}
