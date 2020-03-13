package gopur.uiFunc.execute;
import gopur.uiFunc.thread.CheckRunnable;
import gopur.uiFunc.thread.CheckRunnable2;
import gopur.uiFunc.zip.Zip;
import gopur.Gopur;
import gopur.uiFunc.GopurTool;
import gopur.uiFunc.input.InputMode;
import gopur.uiFunc.interfaces.CmdKey;
import gopur.uiFunc.interfaces.Information;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

/**
 * Created by TPride on 2020/2/14.
 */
public class GopurExecute {
    public GopurExecute() {

    }

    public boolean execute(final String cmd, final String[] args) {
        if (Gopur.receive.getMode() == InputMode.CMD) {
            switch (cmd) {
                case "exit":
                    System.exit(0);
                    return false;
                case "help":
                    if (args.length == 0) {
                        LinkedHashMap<String, LinkedHashMap<String, String>> cmds = Gopur.getInstance().cmds;
                        int i = 0;
                        String result = "\n\t";
                        for (Iterator<Map.Entry<String, LinkedHashMap<String, String>>> c = cmds.entrySet().iterator(); c.hasNext();) {
                            i++;
                            Map.Entry<String, LinkedHashMap<String, String>> entry = c.next();
                            result += entry.getKey() + " - " + entry.getValue().get(CmdKey.DESCRIPTION);
                            if (i < cmds.size())
                                result += ((i % 2 == 0) ? "\n" : "\t") + "\t";
                        }
                        Gopur.getInstance().commandWindow.GopurPrintln(result);
                        break;
                    }
                    String arg_cmd = args[0];
                    if (arg_cmd == null || arg_cmd.length() == 0) {
                        Gopur.getInstance().commandWindow.GopurPrintln("所查看的指令有误");
                        break;
                    }
                    if (!Gopur.getInstance().cmds.containsKey(arg_cmd)) {
                        Gopur.getInstance().commandWindow.GopurPrintln("不存在该指令的帮助信息");
                        break;
                    }
                    Gopur.getInstance().commandWindow.GopurPrintln("\n\t功能: ".concat(Gopur.getInstance().cmds.get(arg_cmd).get(CmdKey.DESCRIPTION)).concat("\n\t用法: ").concat(Gopur.getInstance().cmds.get(arg_cmd).getOrDefault(CmdKey.USAGE, "无")));
                    break;
                case "ifm":
                    Gopur.getInstance().commandWindow.GopurPrintln(""
                            .concat("\n\t版本号: ").concat(Information.VERSION)
                            .concat("\n\t作者: ").concat(GopurTool.getAuthors())
                            .concat("\n\t制作采用: Java")
                            .concat("\n\t当前运行系统: ").concat(System.getProperty("os.name"))
                            .concat("\n\t所在路径: ").concat(Information.NOWPATH)
                            .concat("\n\tJava路径: ").concat(System.getProperty("java.home").concat(File.separator))
                    );
                    break;
                case "parse":
                    if (args.length < 1) {
                        Gopur.getInstance().commandWindow.GopurPrintln(Gopur.getInstance().cmds.get(cmd).get(CmdKey.USAGE));
                        break;
                    }
                    String domainName = args[0];
                    if (domainName == null || domainName.length() == 0) {
                        Gopur.getInstance().commandWindow.GopurPrintln("未知错误");
                        break;
                    }
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
                    Gopur.getInstance().commandWindow.GopurPrintln(result);
                    break;
                case "port":
                    if (args.length < 2) {
                        Gopur.getInstance().commandWindow.GopurPrintln(Gopur.getInstance().cmds.get(cmd).get(CmdKey.USAGE));
                        break;
                    }
                    GopurTool.PortCheck portCheck = Gopur.gopurTool.getPortCheck(Gopur.gopurTool.getDomainName(args[0]));
                    if (portCheck.getDomainName().parse().equalsIgnoreCase("~")) {
                        Gopur.getInstance().commandWindow.GopurPrintln("无效IP");
                        break;
                    }
                    Pattern pattern = Pattern.compile("[0-9]*");
                    Gopur.receive.setMode(InputMode.DONT);
                    ExecutorService executorService = null;
                    if (args[1].length() == 0) {
                        Gopur.getInstance().commandWindow.GopurPrintln("扫描失败, 端口出现错误");
                        Gopur.receive.setMode(InputMode.CMD);
                        break;
                    }
                    if (args[1].contains(",") && args[1].contains("-")) {
                        Gopur.getInstance().commandWindow.GopurPrintln("扫描失败, 格式出现错误");
                        Gopur.receive.setMode(InputMode.CMD);
                        break;
                    }
                    if (args[1].contains(",")) {
                        String[] ports = args[1].split(",");
                        if (ports.length > 100) {
                            Gopur.getInstance().commandWindow.GopurPrintln("每次只能扫描100个端口");
                            Gopur.receive.setMode(InputMode.CMD);
                            break;
                        }
                        Gopur.getInstance().commandWindow.Print("Gopur > 请耐心等待, 正在扫描...\n");
                        Gopur.count.setCount(ports.length);
                        executorService = Executors.newFixedThreadPool(Gopur.count.getCount());
                        for (int i = 0; i < Gopur.count.getCount(); i++)
                            executorService.execute(new CheckRunnable(portCheck, ports[i]));
                        new Thread(new CheckRunnable2()).start();
                    } else if (args[1].contains("-")) {
                        String[] ports = args[1].split("-");
                        if (ports.length < 2) {
                            Gopur.getInstance().commandWindow.GopurPrintln("格式错误");
                            Gopur.receive.setMode(InputMode.CMD);
                            break;
                        }
                        if (!pattern.matcher(ports[0]).matches() || !pattern.matcher(ports[1]).matches()) {
                            Gopur.getInstance().commandWindow.GopurPrintln("无效端口");
                            Gopur.receive.setMode(InputMode.CMD);
                            break;
                        }
                        int begin, end;
                        begin = Integer.parseInt(ports[0]);
                        end = Integer.parseInt(ports[1]);
                        if ((begin > end && begin - end > 100) || (end > begin && end - begin > 100)) {
                            Gopur.getInstance().commandWindow.GopurPrintln("每次只能扫描100个端口");
                            Gopur.receive.setMode(InputMode.CMD);
                            break;
                        }
                        if (begin == end) {
                            Gopur.getInstance().commandWindow.GopurPrintln("端口不能相同");
                            Gopur.receive.setMode(InputMode.CMD);
                            break;
                        }
                        Gopur.getInstance().commandWindow.Print("Gopur > 请耐心等待, 正在扫描...\n");
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
                            Gopur.getInstance().commandWindow.GopurPrintln("无效端口");
                            Gopur.receive.setMode(InputMode.CMD);
                            break;
                        }
                        if (Integer.parseInt(args[1]) < 1 || Integer.parseInt(args[1]) > 65535) {
                            Gopur.getInstance().commandWindow.GopurPrintln("无效端口");
                            Gopur.receive.setMode(InputMode.CMD);
                            break;
                        }
                        Gopur.getInstance().commandWindow.Print("Gopur > 请耐心等待, 正在扫描...\n");
                        if (!portCheck.checkPrint(args[1]))
                            Gopur.getInstance().commandWindow.GopurPrintln(args[1] + " 端口超时");
                        else
                            Gopur.getInstance().commandWindow.GopurPrintln("扫描完毕");
                    }
                    if (executorService != null) executorService.shutdown();
                    if (!args[1].contains(",") && !args[1].contains("-")) {
                        Gopur.receive.setMode(InputMode.CMD);
                    }
                    break;
                case "ping":
                    if (args.length < 1) {
                        Gopur.getInstance().commandWindow.GopurPrintln(Gopur.getInstance().cmds.get(cmd).get(CmdKey.USAGE));
                        break;
                    }
                    if (!args[0].contains(".")) {
                        Gopur.getInstance().commandWindow.GopurPrintln("IP不合法");
                        break;
                    }
                    domainNames = Gopur.gopurTool.getDomainName(args[0]);
                    if (domainNames.parse().equalsIgnoreCase("~")) {
                        Gopur.getInstance().commandWindow.GopurPrintln("不存在该IP");
                        break;
                    }
                    try {
                        Runtime.getRuntime().exec("cmd /k start ping " + domainNames.domainName + " -t");
                    } catch (Exception e) {
                        Gopur.getInstance().commandWindow.GopurPrintln("执行Ping失败");
                    }
                    break;
                case "zip":
                    if (args.length == 0) {
                        execute(cmd, new String[]{"help"});
                    } else if (args.length == 1) {
                        if (args[0] != null && args[0].equals("help")) {
                            Gopur.getInstance().commandWindow.GopurPrintln(""
                                    .concat("\n\tzip en <filepath> <destino || *this* || -p> <password>")
                                    .concat("\n\tzip un <filepath> <destino || *this*>")
                            );
                        } else execute("help", new String[]{cmd});
                    } else if (args.length >= 2) {
                        for (int i = 0; i < args.length; i++) {
                            if (i < 4) {
                                if (args[i] == null || args[i].length() == 0) {
                                    Gopur.getInstance().commandWindow.GopurPrintln("ZIP指令存在空指针风险");
                                    return true;
                                }
                            } else break;
                        }
                        File file = new File(args[1]);
                        if (!file.exists()) {
                            Gopur.getInstance().commandWindow.GopurPrintln("不存在目标的路径");
                            break;
                        }
                        String pwd = null;
                        String dest = "";
                        switch (args[0]) {
                            case "en":
                                if (file.isDirectory())
                                    dest = new File(file.getParent(), file.getName().concat(".zip")).getAbsolutePath();
                                else
                                    dest = new File(file.getParent(), file.getName().substring(0, file.getName().lastIndexOf(".")).concat(".zip")).getAbsolutePath();
                                if (args.length >= 3) {
                                    if (!args[2].equalsIgnoreCase("-p")) {
                                        File d = new File(args[2].replace("*this*", file.getParent()));
                                        if (d.isDirectory())
                                            dest = new File(d.getAbsolutePath(), file.getName().substring(0, file.getName().lastIndexOf(".")).concat(".zip")).getAbsolutePath();
                                        else
                                            if (!d.getName().contains("."))
                                                dest = new File(d.getParentFile() + "/" + d.getName(), file.getName().substring(0, file.getName().lastIndexOf(".")).concat(".zip")).getAbsolutePath();
                                            else
                                                if (!d.getName().substring(d.getName().lastIndexOf("."), d.getName().length()).equals(".zip"))
                                                    dest = new File(d.getParent(), d.getName().substring(0, d.getName().lastIndexOf(".")).concat(".zip")).getAbsolutePath();
                                                else
                                                    dest = d.getAbsolutePath();
                                    }
                                    if (args.length >= 4) {
                                        if (args[3].length() < 1) {
                                            Gopur.getInstance().commandWindow.GopurPrintln("密码必须要在一个字符以上");
                                            break;
                                        }
                                        pwd = args[3];
                                    }
                                }
                                long start = System.currentTimeMillis();
                                boolean bo_result = Zip.zip(args[1], dest, pwd);
                                long second = System.currentTimeMillis() - start;
                                Gopur.getInstance().commandWindow.GopurPrintln(bo_result ? ""
                                        .concat("压缩成功")
                                        .concat("\n\t压缩目标路径: ".concat(args[1]))
                                        .concat("\n\t压缩文件路径: ".concat(dest))
                                        .concat("\n\t压缩文件大小: ".concat((Math.ceil(new File(dest).length() / 1024) + 1) + "KB"))
                                        .concat("\n\t是否已加密: ".concat(pwd == null ? "否" : "是"))
                                        .concat("\n\t用时: ".concat(second + "ms"))
                                        :
                                        "压缩失败"
                                );
                                break;
                            case "un":
                                if (!Zip.isZip(file)) {
                                    Gopur.getInstance().commandWindow.GopurPrintln("该路径获得的File不属于zip压缩文件");
                                    break;
                                }
                                if (!Zip.isValidZip(file)) {
                                    Gopur.getInstance().commandWindow.GopurPrintln("zip文件异常");
                                    break;
                                }
                                dest = new File(file.getParent() + File.separator + file.getName().substring(0, file.getName().lastIndexOf("."))).getAbsolutePath();
                                if (args.length >= 3) {
                                    File d = new File(args[2].replace("*this*", file.getParent()));
                                    if (d.isDirectory()) {
                                        dest = d.getAbsolutePath();
                                    } else {
                                        if (d.getName().contains(".")) {
                                            Gopur.getInstance().commandWindow.GopurPrintln("储存路径必须是文件夹");
                                            break;
                                        }
                                        dest = d.getAbsolutePath();
                                    }
                                }
                                if (Zip.isEncrypted(file.getAbsolutePath())) {
                                    Gopur.getInstance().gopurUI.unPwdZipWindow(file.getName(), args[1], dest);
                                    Gopur.getInstance().commandWindow.GopurPrintln("请输入".concat(file.getName()).concat("的解压密码"));
                                    break;
                                }
                                start = System.currentTimeMillis();
                                bo_result = Zip.unzip(args[1], dest, pwd);
                                second = System.currentTimeMillis() - start;
                                Gopur.getInstance().commandWindow.GopurPrintln(bo_result ? ""
                                        .concat("解压成功")
                                        .concat("\n\t解压至路径: ".concat(dest))
                                        .concat("\n\t用时: ".concat(second + "ms"))
                                        :
                                        "解压失败, 密码错误"
                                );
                                break;
                            default:
                                Gopur.getInstance().commandWindow.GopurPrintln("未知参数 '".concat(args[0]).concat("'"));
                                break;
                        }
                    }
                    break;
                case "cls":
                    GopurTool.clearConsole();
                    break;
                case "~":
                    System.out.print("Input > ");
                    break;
                default:
                    Gopur.getInstance().commandWindow.GopurPrintln("未知指令 '" + cmd + "'");
                    break;
            }
        }
        return true;
    }
}