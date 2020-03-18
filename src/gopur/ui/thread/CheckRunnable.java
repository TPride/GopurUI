package gopur.ui.thread;

import gopur.GopurTool;
import gopur.Gopur;

/**
 * Created by TPride on 2020/2/14.
 */
public class CheckRunnable implements Runnable {
    private GopurTool.PortCheck portCheck;
    private String port;

    public CheckRunnable(GopurTool.PortCheck portCheck, String port) {
        this.portCheck = portCheck;
        this.port = port;
    }

    @Override
    public void run() {
        if (portCheck.checkPrint(port) && Gopur.count.getHave() == 0)
            Gopur.count.have();
        Gopur.count.jian();
    }
}
