package gopur.command.gopur;

import gopur.Gopur;
import gopur.command.Command;
import gopur.event.console.ClearConsoleEvent;

public class ClsCommand extends Command {
    public ClsCommand() {
        super("cls", "清空消息", "cls");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        ClearConsoleEvent event;
        Gopur.getInstance().getPluginManager().callEvent(event = new ClearConsoleEvent());
        if (!event.isCancelled())
            Gopur.getInstance().getCommandWindow().clear();
        return true;
    }
}
