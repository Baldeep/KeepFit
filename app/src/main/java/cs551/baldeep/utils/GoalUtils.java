package cs551.baldeep.utils;

import android.content.Context;
import android.util.Log;

import java.sql.SQLException;
import java.text.DecimalFormat;

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

            double steps = Units.getUnitsInSteps(units, value);

            if(g.getGoalUnits().equals(Units.STEPS)){
                progress += steps;
            } else {
                progress += Units.getStepsInUnits(g.getGoalUnits(), steps);
            }

            g.setGoalCompleted(progress);

            goalDAO.saveOrUpdate(g);
        }
        return g;
    }

    public static String getFormattedProgressString(Goal g){
        String formatted = "";
        if(g.getGoalUnits().equals(Units.STEPS)){
            DecimalFormat df = new DecimalFormat("0");
            formatted += df.format(g.getGoalCompleted()) + "/" + (int)g.getGoalValue()
                    + " " + g.getGoalUnits();
        } else {
            DecimalFormat df = new DecimalFormat("#.000");
            formatted += df.format(g.getGoalCompleted()) + "/" + g.getGoalValue()
                    + " " + g.getGoalUnits();
        }

        return formatted;
    }
}
