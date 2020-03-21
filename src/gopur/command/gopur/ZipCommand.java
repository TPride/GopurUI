package gopur.command.gopur;

import gopur.Gopur;
import gopur.command.Command;
import gopur.event.zip.ZipEnEvent;
import gopur.event.zip.ZipUnEvent;
import gopur.thread.ZipEnThread;
import gopur.thread.ZipUnThread;
import gopur.utils.Zip;
import java.io.File;

public class ZipCommand extends Command {
    public ZipCommand() {
        super("zip", "ZIP文件操作", "zip <args...>");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        if (args.length == 0) {
            execute(null, new String[]{"help"});
        } else if (args.length == 1) {
            if (args[0] != null && args[0].equals("help")) {
                Gopur.getLogger().info(""
                        .concat("\n\tzip en <filepath> <destino || *this* || -p> <password>")
                        .concat("\n\tzip un <filepath> <destino || *this*>")
                , 2);
            } else
                execute(null, new String[]{"help"});
        } else if (args.length >= 2) {
            for (int i = 0; i < args.length; i++) {
                if (i < 4) {
                    if (args[i] == null || args[i].length() == 0) {
                        Gopur.getLogger().info("ZIP指令存在空指针风险", 2);
                        return true;
                    }
                } else break;
            }
            File file = new File(args[1]);
            if (!file.exists()) {
                Gopur.getLogger().info("不存在目标的路径", 2);
                return true;
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
                                Gopur.getLogger().info("密码必须要在一个字符以上", 2);
                                break;
                            }
                            pwd = args[3];
                        }
                    }
                    ZipEnEvent enEvent;
                    Gopur.getInstance().getPluginManager().callEvent(enEvent = new ZipEnEvent(args[1], dest, pwd));
                    if (!enEvent.isCancelled())
                        new ZipEnThread(enEvent.getFilePath(), enEvent.getDestion(), enEvent.getPassword()).start();
                    else
                        Gopur.getLogger().info("压缩 " + enEvent.getFilePath() + " 的线程被取消");
                    break;
                case "un":
                    if (!Zip.isZip(file)) {
                        Gopur.getLogger().info("该路径获得的File不属于zip压缩文件", 2);
                        break;
                    }
                    if (!Zip.isValidZip(file)) {
                        Gopur.getLogger().info("zip文件异常", 2);
                        break;
                    }
                    dest = new File(file.getParent() + File.separator + file.getName().substring(0, file.getName().lastIndexOf("."))).getAbsolutePath();
                    if (args.length >= 3) {
                        File d = new File(args[2].replace("*this*", file.getParent()));
                        if (d.isDirectory()) {
                            dest = d.getAbsolutePath();
                        } else {
                            if (d.getName().contains(".")) {
                                Gopur.getLogger().info("储存路径必须是文件夹", 2);
                                break;
                            }
                            dest = d.getAbsolutePath();
                        }
                    }
                    if (Zip.isEncrypted(file.getAbsolutePath())) {
                        Gopur.getInstance().gopurUI.unPwdZipWindow(file.getName(), args[1], dest);
                        Gopur.getLogger().info("请输入".concat(file.getName()).concat("的解压密码"), 2);
                        break;
                    }
                    ZipUnEvent unEvent;
                    Gopur.getInstance().getPluginManager().callEvent(unEvent = new ZipUnEvent(args[1], dest, pwd));
                    if (!unEvent.isCancelled())
                        new ZipUnThread(unEvent.getFilePath(), unEvent.getDestion(), unEvent.getPassword()).start();
                    else
                        Gopur.getLogger().info("解压 " + unEvent.getFilePath() + " 的线程被取消");
                    break;
                default:
                    Gopur.getLogger().info("未知参数 '".concat(args[0]).concat("'"), 2);
                    break;
            }
        }
        return true;
    }
}
