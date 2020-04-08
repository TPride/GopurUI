package gopur.command.gopur;

import gopur.Gopur;
import gopur.GopurTool;
import gopur.command.Command;
import java.util.ArrayList;
import java.util.function.Consumer;

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
                final int[] i = {0};
                int k = 0;
                final int[] longest = {0};
                ArrayList<Command> commands = new ArrayList<>(Gopur.getInstance().getCommandMap().getCommands().values());
                String[] result = new String[commands.size()];
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("\n\t");
                commands.forEach(new Consumer<Command>() {
                    @Override
                    public void accept(Command command) {
                        String help_string = command.getName() + " - " + command.getDescription();
                        if (longest[0] < help_string.length() && (commands.size() - 1) - i[0] != 0)
                            longest[0] = help_string.length();
                        result[i[0]] = help_string;
                        i[0]++;
                    }
                });
                for (int ii = 0; ii < result.length; ii++) {
                    if ((ii + 1) % 2 != 0) {
                        if (result[ii].length() < longest[0]) {
                            int count = GopurTool.getChineseCount(result[ii]);
                            int reduce = longest[0] - result[ii].length() - (count >= 5 ? (count - 4) : 0);
                            for (int j = 0; j <= reduce; j++)
                                result[ii] += " ";
                        }
                    }
                }
                for (String command : result) {
                    k++;
                    stringBuffer.append(command).append(k % 2 == 0 ? "\n\t" : (commands.size() - k != 0 ? "\t" : ""));
                }
                Gopur.getLogger().info(stringBuffer.toString(), 2);
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