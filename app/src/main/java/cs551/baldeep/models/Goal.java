package cs551.baldeep.models;

import java.util.Date;

/**
 * Created by Baldeep on 06/02/2017.
 */

public class Goal {

    private String name;

    private int stepGoal;

    private int stepsCompleted;

    private boolean currentGoal;

    private Date dateCreated;

    private Date dateSet;

    public Goal(String name, int stepGoal){
        this.name = name;
        this.stepGoal = stepGoal;
        this.stepsCompleted = 0;
        this.dateCreated = new Date(System.currentTimeMillis());
        this.currentGoal = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStepsCompleted() {
        return stepsCompleted;
    }

    public void setStepsCompleted(int stepsCompleted) {
        this.stepsCompleted = stepsCompleted;
    }

    public int getStepGoal() {
        return stepGoal;
    }

    public void setStepGoal(int stepGoal) {
        this.stepGoal = stepGoal;
    }

    public boolean isCurrentGoal() {
        return currentGoal;
    }

    public void setCurrentGoal(boolean currentGoal) {
        this.currentGoal = currentGoal;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateSet() {
        return dateSet;
    }

    public void setDateSet(Date dateSet) {
        this.dateSet = dateSet;
    }
}
