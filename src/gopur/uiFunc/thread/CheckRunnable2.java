package gopur.uiFunc.thread;

import gopur.uiFunc.input.InputMode;
import gopur.Gopur;

/**
 * Created by TPride on 2020/2/14.
 */
public class CheckRunnable2 implements Runnable {
    @Override
    public void run() {
        try {
            while (true) {
                if (Gopur.count.getCount() == 0) {
                    if (Gopur.receive.getMode() == InputMode.DONT)
                        Gopur.receive.setMode(InputMode.CMD);
                    if (Gopur.count.getHave() == 0)
                        Gopur.getInstance().commandWindow.GopurPrintln("未扫描到已开放的端口");
                    else
                        Gopur.getInstance().commandWindow.GopurPrintln("扫描完毕");
                    break;
                }
                Thread.sleep(500); //每一秒执行一次
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
