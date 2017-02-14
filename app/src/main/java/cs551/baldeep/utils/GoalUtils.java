package cs551.baldeep.utils;

import android.content.Context;
import android.util.Log;

import java.sql.SQLException;

import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.Units;

/**
 * Created by balde on 14/02/2017.
 */

public class GoalUtils {

    private GoalDAO goalDAO;

    public GoalUtils(Context context) {
        try {
            this.goalDAO = new GoalDAO(context);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Goal addActivityToGoal(String goalUUID, double value, String units){
        Log.d("GoalUtils: 29 - ", goalUUID);
        Goal g = goalDAO.findById(goalUUID);
        if(g != null){

            double progress = g.getGoalCompleted();

            if(g.getGoalUnits().equals(units)){
                progress += value;
            } else {
                double steps = Units.getUnitsInSteps(units, value);
                if(g.getGoalUnits().equals(Units.STEPS)){
                    progress += steps;
                } else {
                    double val = Units.getStepsInUnits(g.getGoalUnits(), steps);
                    progress += val;
                }
            }
            g.setGoalCompleted(progress);

            goalDAO.saveOrUpdate(g);
        }
        return g;
    }

    public boolean switchCurrentGoal(String newGoalUUID, String oldGoalUUID){
        Goal newGoal = goalDAO.findById(newGoalUUID);
        Goal oldGoal = goalDAO.findById(oldGoalUUID);

        newGoal.setCurrentGoal(true);
        oldGoal.setCurrentGoal(false);

        addActivityToGoal(newGoalUUID, oldGoal.getGoalCompleted(), oldGoal.getGoalUnits());
        oldGoal.setGoalCompleted(0);

        newGoal.setDateOfGoal(oldGoal.getDateOfGoal());

        return false;
    }
}
