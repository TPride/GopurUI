package gopur.thread;

import gopur.Gopur;
import gopur.utils.Zip;

import java.io.File;

public class ZipEnThread extends Thread {
    private String path, dest, pwd;

    public ZipEnThread(String path, String dest, String pwd) {
        this.path = path;
        this.dest = dest;
        this.pwd = pwd;
    }

    @Override
    public void run() {
        Gopur.getLogger().info("正在压缩 " + new File(path).getName() + " ...");
        long start = System.currentTimeMillis();
        boolean bo_result = Zip.zip(path, dest, pwd);
        long second = System.currentTimeMillis() - start;
        Gopur.getLogger().info(bo_result ? ""
                        .concat("压缩成功")
                        .concat("\n\t压缩目标路径: ".concat(path))
                        .concat("\n\t压缩文件路径: ".concat(dest))
                        .concat("\n\t压缩文件大小: ".concat((Math.ceil(new File(dest).length() / 1024) + 1) + "KB"))
                        .concat("\n\t是否已加密: ".concat(pwd == null ? "否" : "是"))
                        .concat("\n\t用时: ".concat(second + "ms"))
                        :
                        "压缩失败"
                , 2);
    }
}
