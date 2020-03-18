package gopur.ui;

import gopur.ui.frame.GopurCommandWindow;
import gopur.ui.frame.GopurUnPwdZipWindow;

public class GopurUI {

    public GopurUI() {
    }

    public final GopurCommandWindow commandWindows() {
        GopurCommandWindow gopurCommandWindow = new GopurCommandWindow();
        gopurCommandWindow.getFrame().setVisible(true);
        return gopurCommandWindow;
    }

    public final GopurUnPwdZipWindow unPwdZipWindow(String filename, String path, String destino) {
        GopurUnPwdZipWindow gopurUnPwdZipWindow = new GopurUnPwdZipWindow(filename, path, destino);
        gopurUnPwdZipWindow.getFrame().setVisible(true);
        return gopurUnPwdZipWindow;
    }
}
