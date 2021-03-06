package cs551.baldeep.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Baldeep on 06/02/2017.
 */

@DatabaseTable(tableName = "goal")
public class Goal {

    public static String GOALUUID = "pk_goal_id";
    @DatabaseField(id = true, columnName = "pk_goal_id")
    private String goalUUID;

    public static String GOAL_NAME = "name";
    @DatabaseField(columnName = "name")
    private String name;

    public static String GOAL_VALUE = "goal_value";
    @DatabaseField(columnName = "goal_value")
    private double goalTarget;

    public static String GOAL_COMPLETED = "goal_completed_value";
    @DatabaseField(columnName = "goal_completed_value")
    private double goalCompleted;

    public static String PERCENTAGE_COMPLETED = "goal_completed_percentage";
    @DatabaseField(columnName = "goal_completed_percentage")
    private int percentageCompleted;

    public static String GOAL_UNITS = "goal_units";
    @DatabaseField(columnName = "goal_units")
    private String goalUnits;

    public static String CURRENT_GOAL = "goal_is_curent";
    @DatabaseField(columnName = "goal_is_curent")
    private boolean currentGoal;

    public static String GOAL_DATE = "goal_date";
    @DatabaseField(columnName = "goal_date")
    private Date dateOfGoal;

    public static String GOAL_DONE = "goal_done";
    @DatabaseField(columnName = "goal_done")
    private boolean done;


    public Goal(String name, double goalTarget, String goalUnits) {
        this.goalUUID = UUID.randomUUID().toString();
        this.name = name;
        this.goalTarget = goalTarget;
        this.goalCompleted = 0;
        this.goalUnits = goalUnits;
        this.currentGoal = false;
    }

    public Goal() {
    }

    public String getGoalUUID() {
        return goalUUID;
    }

    public void setGoalUUID(String goalUUID) {
        this.goalUUID = goalUUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGoalTarget() {
        return goalTarget;
    }

    public void setGoalTarget(double goalTarget) {
        this.goalTarget = goalTarget;
    }

    public double getGoalCompleted() {
        return goalCompleted;
    }

    public void setGoalCompleted(double goalCompleted) {
        this.goalCompleted = goalCompleted;
        this.percentageCompleted = (int) ((goalCompleted/ goalTarget)*100);
    }

    public String getGoalUnits() {
        return goalUnits;
    }

    public void setGoalUnits(String goalUnits) {
        this.goalUnits = goalUnits;
    }

    public boolean isCurrentGoal() {
        return currentGoal;
    }

    public void setCurrentGoal(boolean currentGoal) {
        this.currentGoal = currentGoal;
    }

    public Date getDateOfGoal() {
        return dateOfGoal;
    }

    public void setDateOfGoal(Date dateOfGoal) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOfGoal);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 1);

        this.dateOfGoal = calendar.getTime();
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getPercentageCompleted() {
        this.percentageCompleted = (int) ((goalCompleted/ goalTarget)*100);
        return percentageCompleted;
    }

    public void setPercentageCompleted(int percentageCompleted) {
        this.percentageCompleted = percentageCompleted;
        if(percentageCompleted<=100){
            goalCompleted = (goalTarget /percentageCompleted)*100;
        }
    }
}