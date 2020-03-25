package gopur.event.key;

import gopur.GopurTool;
import gopur.event.Cancellable;
import gopur.event.Event;
import gopur.event.HandlerList;

public class InputTabEvent extends Event implements Cancellable {
    private static final HandlerList handlers = new HandlerList();
    public boolean isEnableTab = true;
    private String fullCmd;
    private String cmd = null;
    private String[] args = new String[0];

    public InputTabEvent(String fullCmd) {
        this.fullCmd = fullCmd;
        cmd = GopurTool.getCmd(fullCmd);
        args = GopurTool.getArgs(fullCmd);
    }

    public InputTabEvent(String fullCmd, boolean isEnableTab) {
        this.isEnableTab = isEnableTab;
        this.fullCmd = fullCmd;
        cmd = GopurTool.getCmd(fullCmd);
        args = GopurTool.getArgs(fullCmd);
    }

    public static final HandlerList getHandlers() {
        return handlers;
    }

    public final String getFullCmd() {
        return fullCmd;
    }

    public final String getCmd() {
        return cmd;
    }

    public final String[] getArgs() {
        return args;
    }

    public final InputTabEvent setArg(int index, String node) {
        if (args.length > index)
            args[index] = node;
        return this;
    }

    public final InputTabEvent setFullCmd(String fullCmd) {
        if (fullCmd != null)
            this.fullCmd = fullCmd;
        return this;
    }

    public final InputTabEvent setCmd(String cmd) {
        this.cmd = cmd;
        return this;
    }

    public final InputTabEvent setEnableTab(boolean enableTab) {
        isEnableTab = enableTab;
        return this;
    }

    public String toFullCmd() {
        String cmd = getCmd();
        if (args.length > 0) {
            cmd += " ";
            for (int i = 0; i < args.length; i++) {
                String arg = args[i];
                if (arg == null)
                    arg = "null";
                cmd += arg;
                if (i + 1 < args.length)
                    cmd += " ";
            }
        }
        return cmd;
    }

    public final boolean isEnableTab() {
        return isEnableTab;
    }
}
