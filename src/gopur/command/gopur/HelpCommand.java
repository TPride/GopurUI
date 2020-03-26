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
                int k = 0;
                int longest = 0;
                ArrayList<Command> commands = new ArrayList<>(Gopur.getInstance().getCommandMap().getCommands().values());
                String[] result = new String[commands.size()];
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("\n\t");
                for (Command command : commands) {
                    String help_string = command.getName() + " - " + command.getDescription();
                    if (longest < help_string.length())
                        longest = help_string.length();
                    result[i] = help_string;
                    i++;
                }
                for (int ii = 1; ii <= result.length; ii++) {
                    if (ii % 2 != 0) {
                        if (result[ii-1].length() < longest) {
                            int reduce = longest - result[ii-1].length() - ((result.length) - ii == 1 ? 2 : 0);
                            for (int j = 0; j < reduce + 1; j++)
                                result[ii-1] += " ";
                        }
                    }
                }
                for (String command : result) {
                    k++;
                    stringBuffer.append(command).append(k % 2 == 0 ? "\n\t" : "\t");
                }
                Gopur.getLogger().info(stringBuffer.toString());
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
