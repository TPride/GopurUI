package gopur;

//                       _oo0oo_
//                      o8888888o
//                      88" . "88
//                      (| -_- |)
//                      0\  =  /0
//                    ___/`---'\___
//                  .' \\|     |// '.
//                 / \\|||  :  |||// \
//                / _||||| -:- |||||- \
//               |   | \\\  -  /// |   |
//               | \_|  ''\---/''  |_/ |
//               \  .-\__  '-'  ___/-. /
//             ___'. .'  /--.--\  `. .'___
//          ."" '<  `.___\_<|>_/___.' >' "".
//         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
//         \  \ `_.   \_ __\ /__ _/   .-` /  /
//     =====`-.____`.___ \_____/___.-`___.-'=====
//                       `=---='
//     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//               佛祖保佑         永无BUG

import gopur.uiFunc.GopurTool;
import gopur.uiFunc.execute.GopurExecute;
import gopur.uiFunc.input.InputReceive;
import gopur.uiFunc.interfaces.CmdKey;
import gopur.uiFunc.interfaces.Information;
import gopur.uiFunc.thread.ThreadCount;
import gopur.uiFunc.ui.GopurUI;
import gopur.uiFunc.ui.frame.GopurCommandWindow;
import java.util.LinkedHashMap;
import java.util.ArrayList;

public class Gopur {
    public final GopurUI gopurUI = new GopurUI();
    private static Gopur instance;
    public final LinkedHashMap<String, LinkedHashMap<String, String>> cmds;
    public final ArrayList<String> inputed = new ArrayList<>();
    public int index = inputed.size();
    public final GopurCommandWindow commandWindow;
    public static final GopurTool gopurTool = new GopurTool();
    public static final InputReceive receive = new InputReceive();
    public static final ThreadCount count = new ThreadCount(0);
    public static final GopurExecute gopurExecute = new GopurExecute();

    public Gopur() {
        instance = this;
        cmds = new LinkedHashMap<String, LinkedHashMap<String, String>>() {
            {
                put("help", new LinkedHashMap<String, String>() {
                    {
                        put(CmdKey.DESCRIPTION, "查看指令帮助");
                        put(CmdKey.USAGE, "help <cmd>");
                    }
                });
                put("ifm", new LinkedHashMap<String, String>() {
                    {
                        put(CmdKey.DESCRIPTION, "查看Gopur与本机信息");
                        put(CmdKey.USAGE, "ifm");
                    }
                });
                put("exit", new LinkedHashMap<String, String>() {
                    {
                        put(CmdKey.DESCRIPTION, "退出Gopur进程");
                        put(CmdKey.USAGE, "exit");
                    }
                });
                put("cls", new LinkedHashMap<String, String>() {
                    {
                        put(CmdKey.DESCRIPTION, "清空消息");
                        put(CmdKey.USAGE, "cls");
                    }
                });
                put("parse", new LinkedHashMap<String, String>() {
                    {
                        put(CmdKey.DESCRIPTION, "解析域名");
                        put(CmdKey.USAGE, "parse <DomainName1,DomainName2...>");
                    }
                });
                put("port", new LinkedHashMap<String, String>() {
                    {
                        put(CmdKey.DESCRIPTION, "端口扫描");
                        put(CmdKey.USAGE, "port <IpAddress> [<port,port1...>||<port-port1>]");
                    }
                });
                put("ping", new LinkedHashMap<String, String>() {
                    {
                        put(CmdKey.DESCRIPTION, "发送Ping包");
                        put(CmdKey.USAGE, "ping <IpAddress>");
                    }
                });
                put("zip", new LinkedHashMap<String, String>() {
                    {
                        put(CmdKey.DESCRIPTION, "ZIP文件操作");
                        put(CmdKey.USAGE, "zip");
                    }
                });
            }
        };
        commandWindow = gopurUI.commandWindows();
        commandWindow.Print("Gopur > 欢迎使用GopurUI v".concat(Information.VERSION).concat("\n"));
    }

    public static void main(String[] args) {
        try {
            new Gopur();
        } catch (Exception ee) {

        }
    }

    public final static Gopur getInstance() {
        return instance;
    }
}
