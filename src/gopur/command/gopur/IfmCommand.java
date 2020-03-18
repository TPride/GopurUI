package gopur.command.gopur;

import gopur.Gopur;
import gopur.GopurTool;
import gopur.command.Command;
import gopur.Information;

import java.io.File;

public class IfmCommand extends Command {
    public IfmCommand() {
        super("ifm", "查看基础信息", "ifm");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        Gopur.getLogger().info(""
                .concat("\n\t版本号: ").concat(Information.VERSION)
                .concat("\n\t作者: ").concat(GopurTool.getAuthors())
                .concat("\n\t制作采用: Java")
                .concat("\n\t当前运行系统: ").concat(System.getProperty("os.name"))
                .concat("\n\t所在路径: ").concat(Information.NOWPATH)
                .concat("\n\tJava路径: ").concat(System.getProperty("java.home").concat(File.separator))
        , 2);
        return true;
    }
}
