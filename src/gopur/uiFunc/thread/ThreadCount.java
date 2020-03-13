package gopur.uiFunc.thread;

/**
 * Created by TPride on 2020/2/14.
 */
public class ThreadCount { //计数
    private int count;
    private int have = 0;
    private int max_count;

    public ThreadCount(int count) {
        this.count = count;
        this.max_count = count;
    }

    public int getMaxCount() {
        return max_count;
    }

    public void jian() {
        if (count > 0) count -= 1;
    }

    public int getCount() {
        return count;
    }

    public void have() {
        if (have == 0)
            have = 1;
    }

    public int getHave() {
        return have;
    }

    public ThreadCount setCount(int count) {
        if (this.count <= 0) {
            this.count = count;
            max_count = count;
            return this;
        }
        return this;
    }

    public void clear() {
        max_count = 0;
        count = 0;
        have = 0;
    }
}
