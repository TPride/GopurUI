package gopur.command.gopur;

import gopur.Gopur;
import gopur.Information;
import gopur.command.Command;
import gopur.plugin.JarPluginLoader;
import gopur.ui.input.InputMode;

public class ReloadCommand extends Command {
    public ReloadCommand() {
        super("reload", "重载GopurUI", "reload");
    }

    @Override
    public boolean execute(String full_line, String[] args) {
        if (!Gopur.getInstance().isReloading) {
            Gopur.getInstance().isReloading = true;
            Gopur.receive.setMode(InputMode.CMD); //输入状态重置为CMD模式
            Gopur.getInstance().getCommandWindow().getTabInput().clear(); //清除Tab填充
            Gopur.getInstance().getCommandMap().clearCommands(); //清除指令
            Gopur.getInstance().getPluginManager().disablePlugins(); //关闭所有插件
            Gopur.getInstance().getPluginManager().clearPlugins(); //清空插件列表
            Gopur.getInstance().getCommandWindow().clear(); //清屏
            Gopur.getLogger().info("正在重载 GopurUI v".concat(Information.VERSION) + " ...", 1);
            Gopur.getInstance().getPluginManager().registerInterface(JarPluginLoader.class);
            Gopur.getInstance().getPluginManager().loadPlugins(); //加载所有插件
            Gopur.getInstance().getPluginManager().enablePlugins(); //开启所有插件
            Gopur.getLogger().info("重载 GopurUI v".concat(Information.VERSION) + " 完毕");
            Gopur.getInstance().isReloading = false;
        }
        return true;
    }
}
