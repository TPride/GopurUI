package gopur.command.gopur;

import gopur.Gopur;
import gopur.command.Command;

import java.util.ArrayList;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "查看帮助", "help <command>");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        if (args == null) {
            Gopur.getLogger().info("所查看的指令有误", 2);
        } else {
            if (args.length == 0) {
                int i = 0;
                String result = "\n\t";
                ArrayList<Command> commands = new ArrayList<>(Gopur.getInstance().getCommandMap().getCommands().values());
                for (Command command : commands) {
                    i++;
                    result += command.getName() + " - " + command.getDescription();
                    if (i < commands.size())
                        result += ((i % 2 == 0) ? "\n" : "\t") + "\t";
                }
                Gopur.getLogger().info(result, 2);
                return true;
            }
            if (args[0].length() == 0) {
                Gopur.getLogger().info("所查看的指令有误", 2);
            } else {
                String commandName = args[0];
                if (Gopur.getInstance().getCommandMap().getCommands().containsKey(commandName)) {
                    Command command = Gopur.getInstance().getCommandMap().getCommand(commandName);
                    Gopur.getLogger().info("\n\t介绍: ".concat(command.getDescription()).concat("\n\t用法: ").concat(command.getUsage()), 2);
                } else
                    Gopur.getLogger().info("不存在该指令的帮助信息", 2);
            }
        }
        return true;
    }
}
