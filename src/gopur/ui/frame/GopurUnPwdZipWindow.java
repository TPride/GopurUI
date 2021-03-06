package gopur.ui.frame;

import gopur.Gopur;
import gopur.thread.ZipUnThread;
import gopur.ui.input.InputMode;
import gopur.utils.Zip;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GopurUnPwdZipWindow {
    private final JFrame jFrame;

    public GopurUnPwdZipWindow(String filename, String path, String destino) {
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
                    Gopur.getLogger().info("请输入".concat(filename).concat("的解压密码"), 2);
                    return;
                }
                jFrame.dispose();
                new ZipUnThread(path, destino, textField.getText());
            }
        });

        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                textField.requestFocus();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                Gopur.getLogger().info("已取消对".concat(filename).concat("的解压"), 2);
            }
        });

        jFrame.setContentPane(textField);
    }

    public final JFrame getFrame() {
        return jFrame;
    }
}
