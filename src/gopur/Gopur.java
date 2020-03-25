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

import gopur.command.SimpleCommandMap;
import gopur.event.Listener;
import gopur.plugin.*;
import gopur.ui.input.InputReceive;
import gopur.thread.ThreadCount;
import gopur.ui.GopurUI;
import gopur.ui.frame.GopurCommandWindow;
import java.io.File;
import java.util.ArrayList;

public class Gopur implements Listener {
    public final GopurUI gopurUI = new GopurUI();
    private static Gopur instance;
    public final ArrayList<String> inputed = new ArrayList<>();
    public int index = inputed.size();
    public static final GopurTool gopurTool = new GopurTool();
    public static final InputReceive receive = new InputReceive();
    public static final ThreadCount count = new ThreadCount(0);
    private static final Logger mainLogger = new MainLogger();
    private final GopurCommandWindow commandWindow;
    private final PluginManager pluginManager;
    private final SimpleCommandMap commandMap;

    public Gopur() {
        instance = this;
        initDir();
        commandMap = new SimpleCommandMap();
        commandWindow = gopurUI.commandWindows();
        /**
         * Plugins init
         */
        pluginManager = new PluginManager();
        pluginManager.registerInterface(JarPluginLoader.class);

        Gopur.getLogger().info("欢迎使用GopurUI v".concat(Information.VERSION));
    }

    public static void main(String[] args) {
        try {
            new Gopur();
            Gopur.getInstance().getPluginManager().enablePlugins();
        } catch (Exception ee) {
            Gopur.getLogger().info("[ERROR] " + ee.toString());
        }
    }

    public static Gopur getInstance() {
        return instance;
    }

    private void initDir() {
        if (!new File(Information.NOWPATH + "plugins").exists())
            new File(Information.NOWPATH + "plugins").mkdirs();
    }

    public GopurCommandWindow getCommandWindow() {
        return commandWindow;
    }

    public final PluginManager getPluginManager() {
        return pluginManager;
    }

    public final SimpleCommandMap getCommandMap() {
        return commandMap;
    }

    public static Logger getLogger() {
        return mainLogger;
    }
}
