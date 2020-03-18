package gopur.command.gopur;

import gopur.Gopur;
import gopur.GopurTool;
import gopur.command.Command;

public class ParseCommand extends Command {
    public ParseCommand() {
        super("parse", "解析域名", "parse <DomainName1,DomainName2...>");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        if (args.length < 1) {
            Gopur.getInstance().getCommandMap().dispatch("help " + getName());
        } else {
            String domainName = args[0];
            if (domainName == null || domainName.length() == 0) {
                Gopur.getLogger().info("未知错误", 2);
            } else {
                GopurTool.DomainName domainNames = Gopur.gopurTool.getDomainName(domainName);
                String result = "";
                if (domainName.contains(",")) {
                    String[] names = domainName.split(",");
                    for (int i = 0; i < names.length; i++) {
                        domainNames.domainName = names[i];
                        result += "\n\t[\n\t\t" + "域名: " + names[i] + "\n\t\t" + "解析IP: " + (domainNames.parse().equalsIgnoreCase("~") ? "无效IP" : domainNames.parse()) + "\n\t]" + ((i + 1) < names.length ? "," : "");
                    }
                } else
                    result += "\n\t[\n\t\t" + "域名: " + domainName + "\n\t\t" + "解析IP: " + (domainNames.parse().equalsIgnoreCase("~") ? "无效IP" : domainNames.parse()) + "\n\t]";
                Gopur.getLogger().info(result, 2);
            }
        }
        return true;
    }
}
