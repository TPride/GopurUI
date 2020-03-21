package gopur.ui.input;

/**
 * Created by TPride on 2020/2/14.
 */
public enum InputMode {

    CMD(0),
    DONT(1);

    private int number;
    private InputMode(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
