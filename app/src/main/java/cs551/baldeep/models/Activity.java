package cs551.baldeep.models;

import java.util.Date;

/**
 * Created by balde on 06/02/2017.
 */

public class Activity {

    private Date date;

    private int stepsDone;

    public Activity(Date date, int stepsDone) {
        this.date = date;
        this.stepsDone = stepsDone;
    }

    public int getStepsDone() {
        return stepsDone;
    }

    public void setStepsDone(int stepsDone) {
        this.stepsDone = stepsDone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
