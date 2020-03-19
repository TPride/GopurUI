package gopur.thread;

import gopur.ui.input.InputMode;
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
                        Gopur.getLogger().info("未扫描到已开放的端口", 2);
                    else
                        Gopur.getLogger().info("扫描完毕", 2);
                    break;
                }
                Thread.sleep(500); //每一秒执行一次
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
