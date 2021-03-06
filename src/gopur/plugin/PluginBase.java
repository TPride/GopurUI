package gopur.plugin;

import gopur.Gopur;

import java.io.File;

public class PluginBase implements Plugin {
    private PluginLoader loader;
    private boolean isEnabled = false;
    private boolean isInitalized = false;
    private PluginDescription description;
    private File dataFolder;
    private Logger logger;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public final boolean isDisabled() {
        return !isEnabled;
    }

    @Override
    public final boolean isEnabled() {
        return isEnabled;
    }

    public final void setEnabled() {
        setEnabled(true);
    }

    public final void setEnabled(boolean s) {
        if (isEnabled != s) {
            isEnabled = s;
            if (isEnabled)
                onEnable();
            else {
                Gopur.getInstance().getPluginManager().disablePlugin(this);
                onDisable();
            }
        }
    }

    public final PluginLoader getPluginLoader() {
        return loader;
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    public final File getDataFolder() {
        return dataFolder;
    }

    public final boolean isInitalized() {
        return isInitalized;
    }

    public final void init(PluginLoader loader, PluginDescription description, File dataFolder) {
        if (!isInitalized) {
            isInitalized = true;
            this.loader = loader;
            this.description = description;
            this.dataFolder = dataFolder;
            logger = new PluginLogger(this);
        }
    }

    @Override
    public PluginDescription getPluginDescription() {
        return description;
    }
}