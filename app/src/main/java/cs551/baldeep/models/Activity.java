package cs551.baldeep.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;
import java.util.UUID;

/**
 * Created by balde on 06/02/2017.
 */

@DatabaseTable(tableName = "activity")
public class Activity {

    @DatabaseField(id = true, columnName = "pk_activity_id")
    private String activityUUID;

    @DatabaseField(columnName = "activity_date")
    private Date date;

    @DatabaseField(columnName = "activity_value")
    private double progress;



    public Activity(Date date, int progress) {
        this.activityUUID = UUID.randomUUID().toString();
        this.date = date;
        this.progress = progress;
    }

    public String getActivityUUID(){
        return activityUUID;
    }

    public void setActivityUUID(String activityUUID){
        this.activityUUID = activityUUID;
    }

    public double getStepsDone() {
        return progress;
    }

    public void setStepsDone(double stepsDone) {
        this.progress = stepsDone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
