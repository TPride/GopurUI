package gopur.command.gopur;

import gopur.Gopur;
import gopur.GopurTool;
import gopur.command.Command;

public class ClsCommand extends Command {
    public ClsCommand() {
        super("cls", "清空消息", "cls");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        Gopur.getInstance().getCommandWindow().clear();
        return true;
    }
}
