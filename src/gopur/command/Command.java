package gopur.command;

import gopur.plugin.Plugin;

public abstract class Command {
    private final String name;
    protected String description;
    protected String usage;
    private CommandMap commandMap = null;

    public Command(String name, String description, String usage) {
        this.name = name;
        this.description = (description == null ? "无" :description);
        this.usage = (usage == null ? name : usage);
    }

    public abstract boolean execute(final String full_line, String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = (description == null ? "无" :description);
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = (usage == null ? name : usage);
    }

    public boolean register(CommandMap commandMap) {
        if (commandMap != null && !commandMap.equals(this.commandMap)) {
            this.commandMap = commandMap;
            return true;
        }
        return false;
    }

    public boolean unregister(CommandMap commandMap) {
        if (commandMap != null && !commandMap.equals(this.commandMap)) {
            this.commandMap = null;
            return true;
        }
        return false;
    }

    public boolean isRegistered() {
        return commandMap != null;
    }

    @Override
    public String toString() {
        return name;
    }
}
