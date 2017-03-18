package cs551.baldeep.utils;

import android.content.Context;
import android.util.Log;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;

import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.models.Goal;

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
        return goalDAO.findById(goalUUID);
    }

    public String getFormattedProgressString(Goal g){
        String formatted = "";
        if(g.getGoalUnits().equals(Units.STEPS)){
            DecimalFormat df = new DecimalFormat("0");
            formatted += df.format(g.getGoalCompleted()) + "/" + (int)g.getGoalTarget()
                    + " " + g.getGoalUnits();
        } else {
            DecimalFormat df = new DecimalFormat("#.000");
            formatted += df.format(g.getGoalCompleted()) + "/" + g.getGoalTarget()
                    + " " + g.getGoalUnits();
        }

        return formatted;
    }

    public void endOfDay(){
        Goal currentGoal = goalDAO.findCurrentGoal();

        if(currentGoal != null) {

            Goal previous = goalDAO.findFinishedForDate(currentGoal.getDateOfGoal());
            if(previous != null){
                Log.i("GoalUtils, endOfDay", "found previous");
                goalDAO.deleteById(previous.getGoalUUID());
            }

            currentGoal.setDone(true);
            currentGoal.setCurrentGoal(false);

            goalDAO.saveOrUpdate(currentGoal);

            Goal newCurrentGoal = new Goal(currentGoal.getName(), currentGoal.getGoalTarget(), currentGoal.getGoalUnits());
            newCurrentGoal.setDateOfGoal(new Date(System.currentTimeMillis() + 200));
            newCurrentGoal.setCurrentGoal(true);
            newCurrentGoal.setDone(false);

            goalDAO.saveOrUpdate(newCurrentGoal);
        }
    }

    public String getFormattedProgressStringInUnits(Goal g, String units){
        String formatted = "";

        double tempSteps = Units.getUnitsInSteps(g.getGoalUnits(), g.getGoalCompleted());
        double convertedProgress = Units.getStepsInUnits(units, tempSteps);

        tempSteps = Units.getUnitsInSteps(g.getGoalUnits(), g.getGoalTarget());
        double convertedValue = Units.getStepsInUnits(units, tempSteps);

        if(units.equals(Units.STEPS)){
            DecimalFormat df = new DecimalFormat("0");
            formatted += df.format((int)convertedProgress + "/" + (int)convertedProgress
                    + " " + units);
        } else {
            DecimalFormat df = new DecimalFormat("0.000");
            formatted += df.format(convertedProgress) + "/" + df.format(convertedValue)
                    + " " + units;
        }

        return formatted;
    }


}
