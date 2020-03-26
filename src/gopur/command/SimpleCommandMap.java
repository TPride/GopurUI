package gopur.command;

import gopur.GopurTool;
import gopur.command.gopur.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleCommandMap implements CommandMap {
    protected final LinkedHashMap<String, Command> commands = new LinkedHashMap<>();

    public SimpleCommandMap() {
        setGopurDefaultCommands();
    }

    private void setGopurDefaultCommands() {
        register(new HelpCommand()); //help
        register(new IfmCommand()); //ifm
        register(new ExitCommand()); //exit
        register(new ClsCommand()); //cls
        register(new ParseCommand()); //parse
        register(new PortCommand()); //port
        register(new PingCommand()); //ping
        register(new ZipCommand()); //zip
        register(new PlCommand()); //pl
        register(new ReloadCommand()); //reload
    }

    @Override
    public boolean register(Command command) {
        if (command != null && !command.isRegistered()) {
            command.register(this);
            commands.put(command.getName(), command);
            return true;
        }
        return false;
    }

    @Override
    public boolean unregister(String commndName) {
        if (!getCommands().containsKey(commndName))
            return false;
        getCommands().remove(commndName);
        return true;
    }

    @Override
    public boolean unregister(Command command) {
        if (getCommands().containsValue(command)) {
            getCommands().values().removeIf(command1 -> command1.equals(command));
            return true;
        }
        return false;
    }

    @Override
    public Command getCommand(String name) {
        return commands.getOrDefault(name, null);
    }

    @Override
    public void clearCommands() {
        for (Command command : this.commands.values())
            command.unregister(this);
        commands.clear();
        setGopurDefaultCommands();
    }

    @Override
    public boolean dispatch(String cmd_line) {
        Command command = getCommand(GopurTool.getCmd(cmd_line));
        if (command == null)
            return false;
        try {
            command.execute(cmd_line, GopurTool.getArgs(cmd_line));
        } catch (Exception e) {
            //no
        }
        return true;
    }

    public Map<String, Command> getCommands() {
        return commands;
    }
}
