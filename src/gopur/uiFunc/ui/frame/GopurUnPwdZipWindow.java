package gopur.uiFunc.ui.frame;

import gopur.Gopur;
import gopur.uiFunc.input.InputMode;
import gopur.uiFunc.zip.Zip;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GopurUnPwdZipWindow {
    private final JFrame jFrame;

    public GopurUnPwdZipWindow(String filename, String path, String destino) {
        Gopur.receive.setMode(InputMode.DONT);
        jFrame = new JFrame();
        jFrame.setLayout(null);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jFrame.setIconImage(new ImageIcon("resource/gopur.png").getImage());
        jFrame.setTitle("");
        jFrame.setSize(150, 100);
        jFrame.setLocationRelativeTo(null);
        jFrame.setResizable(false);

        JTextField textField = new JTextField();
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textField.getText() == null || textField.getText().equalsIgnoreCase("") || textField.getText().replace(" ", "  ").length() == 0) {
                    Gopur.getInstance().commandWindow.GopurPrintln("请输入".concat(filename).concat("的解压密码"));
                    return;
                }
                jFrame.dispose();
                Gopur.receive.setMode(InputMode.CMD);
                long start, second;
                boolean bo_result;
                start = System.currentTimeMillis();
                bo_result = Zip.unzip(path, destino, textField.getText());
                second = System.currentTimeMillis() - start;
                Gopur.getInstance().commandWindow.GopurPrintln(bo_result ? ""
                        .concat("解压成功")
                        .concat("\n\t解压至路径: ".concat(destino))
                        .concat("\n\t用时: ".concat(second + "ms"))
                        :
                        "解压失败, 密码错误"
                );
            }
        });

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                textField.requestFocus();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                Gopur.getInstance().commandWindow.GopurPrintln("已取消对".concat(filename).concat("的解压"));
                Gopur.receive.setMode(InputMode.CMD);
            }
        });

        jFrame.setContentPane(textField);
    }

    public final JFrame getFrame() {
        return jFrame;
    }
}
