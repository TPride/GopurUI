package gopur.ui.input;

/**
 * Created by TPride on 2020/2/14.
 */
public class InputReceive {
    private InputMode mode = InputMode.CMD;

    public InputReceive() {

    }

    public InputMode getMode() {
        return mode;
    }

    public boolean setMode(InputMode mode) {
        if (this.mode == mode)
            return false;
        this.mode = mode;
        return true;
    }

    public boolean setMode(int number) {
        switch (number) {
            case 0:
                mode = InputMode.CMD;
                break;
            case 1:
                mode = InputMode.DONT;
                break;
            default:
                return false;
        }
        return true;
    }
}
