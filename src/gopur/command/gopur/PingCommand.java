package gopur.command.gopur;

import gopur.Gopur;
import gopur.GopurTool;
import gopur.command.Command;

public class PingCommand extends Command {
    public PingCommand() {
        super("ping", "发送Ping包", "ping <IpAddress>");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        if (args.length < 1) {
            Gopur.getInstance().getCommandMap().dispatch("help " + getName());
            return false;
        }
        if (!args[0].contains(".")) {
            Gopur.getLogger().info("IP不合法", 2);
            return false;
        }
        GopurTool.DomainName domainNames = Gopur.gopurTool.getDomainName(args[0]);
        if (domainNames.parse().equalsIgnoreCase("~")) {
            Gopur.getLogger().info("不存在该IP", 2);
            return false;
        }
        try {
            Runtime.getRuntime().exec("cmd /k start ping " + domainNames.domainName + " -t");
        } catch (Exception e) {
            Gopur.getLogger().info("执行Ping失败", 2);
        }
        return true;
    }
}
