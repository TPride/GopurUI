package gopur.command.gopur;

import gopur.Gopur;
import gopur.command.Command;

public class ExitCommand extends Command {
    public ExitCommand() {
        super("exit", "退出Gopur进程", "exit");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        Gopur.getInstance().getPluginManager().disablePlugins();
        System.exit(0);
        return true;
    }
}
