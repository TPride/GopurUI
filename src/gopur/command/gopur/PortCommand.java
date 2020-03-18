package gopur.command.gopur;

import gopur.Gopur;
import gopur.GopurTool;
import gopur.command.Command;
import gopur.ui.input.InputMode;
import gopur.ui.thread.CheckRunnable;
import gopur.ui.thread.CheckRunnable2;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class PortCommand extends Command {
    public PortCommand() {
        super("port", "端口扫描", "port <IpAddress> [<port,port1...>||<port-port1>]");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        if (args.length < 2) {
            Gopur.getInstance().getCommandMap().dispatch("help " + getName());
            return true;
        }
        GopurTool.PortCheck portCheck = Gopur.gopurTool.getPortCheck(Gopur.gopurTool.getDomainName(args[0]));
        if (portCheck.getDomainName().parse().equalsIgnoreCase("~")) {
            Gopur.getLogger().info("无效IP", 2);
            return true;
        }
        Pattern pattern = Pattern.compile("[0-9]*");
        Gopur.receive.setMode(InputMode.DONT);
        ExecutorService executorService = null;
        if (args[1].length() == 0) {
            Gopur.getLogger().info("扫描失败, 端口出现错误", 2);
            Gopur.receive.setMode(InputMode.CMD);
            return true;
        }
        if (args[1].contains(",") && args[1].contains("-")) {
            Gopur.getLogger().info("扫描失败, 格式出现错误", 2);
            Gopur.receive.setMode(InputMode.CMD);
            return true;
        }
        if (args[1].contains(",")) {
            String[] ports = args[1].split(",");
            if (ports.length > 100) {
                Gopur.getLogger().info("每次只能扫描100个端口", 2);
                Gopur.receive.setMode(InputMode.CMD);
                return true;
            }
            Gopur.getLogger().info("Gopur > 请耐心等待, 正在扫描...");
            Gopur.count.setCount(ports.length);
            executorService = Executors.newFixedThreadPool(Gopur.count.getCount());
            for (int i = 0; i < Gopur.count.getCount(); i++)
                executorService.execute(new CheckRunnable(portCheck, ports[i]));
            new Thread(new CheckRunnable2()).start();
        } else if (args[1].contains("-")) {
            String[] ports = args[1].split("-");
            if (ports.length < 2) {
                Gopur.getLogger().info("格式错误", 2);
                Gopur.receive.setMode(InputMode.CMD);
                return true;
            }
            if (!pattern.matcher(ports[0]).matches() || !pattern.matcher(ports[1]).matches()) {
                Gopur.getLogger().info("无效端口", 2);
                Gopur.receive.setMode(InputMode.CMD);
                return true;
            }
            int begin, end;
            begin = Integer.parseInt(ports[0]);
            end = Integer.parseInt(ports[1]);
            if ((begin > end && begin - end > 100) || (end > begin && end - begin > 100)) {
                Gopur.getLogger().info("每次只能扫描100个端口", 2);
                Gopur.receive.setMode(InputMode.CMD);
                return true;
            }
            if (begin == end) {
                Gopur.getLogger().info("端口不能相同", 2);
                Gopur.receive.setMode(InputMode.CMD);
                return true;
            }
            Gopur.getLogger().info("Gopur > 请耐心等待, 正在扫描...");
            Gopur.count.setCount(((begin > end) ? begin - end : end - begin));
            executorService = Executors.newFixedThreadPool(Gopur.count.getCount());
            if (begin > end) {
                for (int i = begin; i >= end; i--)
                    executorService.execute(new CheckRunnable(portCheck, "" + i));
            } else if (end > begin) {
                for (int i = begin; i <= end; i++)
                    executorService.execute(new CheckRunnable(portCheck, "" + i));
            }
            new Thread(new CheckRunnable2()).start();
        } else {
            if (!pattern.matcher(args[1]).matches()) {
                Gopur.getLogger().info("无效端口", 2);
                Gopur.receive.setMode(InputMode.CMD);
                return true;
            }
            if (Integer.parseInt(args[1]) < 1 || Integer.parseInt(args[1]) > 65535) {
                Gopur.getLogger().info("无效端口", 2);
                Gopur.receive.setMode(InputMode.CMD);
                return true;
            }
            Gopur.getLogger().info("Gopur > 请耐心等待, 正在扫描...");
            if (!portCheck.checkPrint(args[1]))
                Gopur.getLogger().info(args[1] + " 端口超时", 2);
            else
                Gopur.getLogger().info("扫描完毕", 2);
        }
        if (executorService != null) executorService.shutdown();
        if (!args[1].contains(",") && !args[1].contains("-")) {
            Gopur.receive.setMode(InputMode.CMD);
        }
        return true;
    }
}
