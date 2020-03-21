package gopur.thread;

import gopur.Gopur;
import gopur.utils.Zip;

import java.io.File;

public class ZipUnThread extends Thread {
    private String path, dest, pwd;

    public ZipUnThread(String path, String dest, String pwd) {
        this.path = path;
        this.dest = dest;
        this.pwd = pwd;
    }

    @Override
    public void run() {
        Gopur.getLogger().info("正在解压 " + path + " ...");
        long start = System.currentTimeMillis();
        boolean bo_result = Zip.unzip(path, dest, pwd);
        long second = System.currentTimeMillis() - start;
        Gopur.getLogger().info(bo_result ? ""
                        .concat("解压成功")
                        .concat("\n\t解压至路径: ".concat(dest))
                        .concat("\n\t用时: ".concat(second + "ms"))
                        :
                        "解压失败, 密码错误"
                , 2);
    }
}
