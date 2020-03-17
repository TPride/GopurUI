package gopur.uiFunc.input;

/**
 * Created by TPride on 2020/2/14.
 */
public enum InputMode {

    CMD(0),
    USER(1),
    PWD(2),
    OTHER(3),
    DONT(4);

    private int number;
    private InputMode(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
