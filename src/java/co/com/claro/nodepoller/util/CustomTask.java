package co.com.claro.nodepoller.util;

import java.io.Serializable;
import java.util.TimerTask;

/**
 *
 * @author juan.vega
 */
public class CustomTask extends TimerTask implements Serializable {

    private static final long serialVersionUID = 8164014625191574983L;

    private int diff;

    public CustomTask() {
    }

    public CustomTask(int diff) {
        this.diff = diff;
    }

    @Override
    public void run() {
        diff--;
    }

    public int getDiff() {
        return diff;
    }

    public void setDiff(int diff) {
        this.diff = diff;
    }
}
