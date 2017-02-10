package cs551.baldeep.models;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Baldeep on 06/02/2017.
 */

public class Goal {

    private String goalUUID;

    private String name;

    private int goalMax;

    private int goalDone;

    private String goalUnits;

    private boolean currentGoal;

    private Date dateCreated;

    private Date dateSet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGoalMax() {
        return goalMax;
    }

    public void setGoalMax(int goalMax) {
        this.goalMax = goalMax;
    }

    public int getGoalDone() {
        return goalDone;
    }

    public void setGoalDone(int goalDone) {
        this.goalDone = goalDone;
    }

    public boolean isCurrentGoal() {
        return currentGoal;
    }

    public void setCurrentGoal(boolean currentGoal) {
        this.currentGoal = currentGoal;
    }

    public String getGoalUnits() {
        return goalUnits;
    }

    public void setGoalUnits(String goalUnits) {
        this.goalUnits = goalUnits;
    }

    public Date getDateSet() {
        return dateSet;
    }

    public void setDateSet(Date dateSet) {
        this.dateSet = dateSet;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getGoalUUID() {
        return goalUUID;
    }

    public void setGoalUUID(String goalUUID) {
        this.goalUUID = goalUUID;
    }

    public Goal(String name, int goalMax, String goalUnits) {
        this.goalUUID = UUID.randomUUID().toString();
        this.name = name;
        this.goalMax = goalMax;
        this.goalDone = 0;
        this.goalUnits = goalUnits;
        this.dateCreated = new Date(System.currentTimeMillis());
        this.currentGoal = false;
    }

}