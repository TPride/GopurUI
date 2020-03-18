package gopur.plugin;

import gopur.Gopur;

public class MainLogger implements Logger {
    public MainLogger() {

    }

    @Override
    public void info(String msg, int times) {
        String next = "";
        if (times > 0)
            for (int i = 0; i < times; i++)
                next += "\n";
        Gopur.getInstance().getCommandWindow().GopurPrint(msg + next);
    }

    @Override
    public void info(String msg) {
        info(msg, 1);
    }
}
