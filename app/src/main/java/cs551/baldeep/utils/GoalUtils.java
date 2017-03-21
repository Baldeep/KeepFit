package cs551.baldeep.utils;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.models.Goal;

/**
 * Created by balde on 14/02/2017.
 */

public class GoalUtils {

    private GoalDAO goalDAO;
    private Units unitsUtils;

    public static String HISTORY_ALL = "All";
    public static String HISTORY_WEEK = "Week";
    public static String HISTORY_MONTH = "Month";
    public static String HISTORY_CUSTOM = "Custom";

    public GoalUtils(Context context) {
        try {
            this.goalDAO = new GoalDAO(context);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        unitsUtils = new Units(context);
    }

    public Goal addActivityToGoal(String goalUUID, double value, String units){
        Goal g = goalDAO.findById(goalUUID);
        if(g != null){
            double progress = g.getGoalCompleted();

            double steps = unitsUtils.getUnitsInSteps(units, value);

            if(g.getGoalUnits().equals(Units.STEPS)){
                progress += steps;
            } else {
                progress += unitsUtils.getStepsInUnits(g.getGoalUnits(), steps);
            }


            g.setGoalCompleted(progress);

            boolean saved = goalDAO.saveOrUpdate(g);

            Log.i("GOALUTILS ADDACTIVITY", "progress: " + progress + ", gUnit: " + g.getGoalUnits() + ", steps: " + steps);
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
            DecimalFormat df = new DecimalFormat("0.000");
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

        double tempSteps = unitsUtils.getUnitsInSteps(g.getGoalUnits(), g.getGoalCompleted());
        double convertedProgress = unitsUtils.getStepsInUnits(units, tempSteps);


        tempSteps = unitsUtils.getUnitsInSteps(g.getGoalUnits(), g.getGoalTarget());
        double convertedValue = unitsUtils.getStepsInUnits(units, tempSteps);

        if(units.equals(Units.STEPS)){
            DecimalFormat df = new DecimalFormat("0");
            formatted += (df.format(convertedProgress) + "/" + df.format(convertedValue)
                    + " " + units);
        } else {
            DecimalFormat df = new DecimalFormat("0.000");
            formatted += df.format(convertedProgress) + "/" + df.format(convertedValue)
                    + " " + units;
        }

        return formatted;
    }

    public List<String> getHistoryFilterStrings(){
        List<String> historyFilters = new ArrayList<String>();
        historyFilters.add(HISTORY_ALL);
        historyFilters.add(HISTORY_WEEK);
        historyFilters.add(HISTORY_MONTH);
        historyFilters.add(HISTORY_CUSTOM);
        return historyFilters;
    }

    public List<Goal> filterHistory(String dateFilter, Date startDate, Date endDate, int percentageStart, int percentageEnd){

        Date dateStart = null;
        Date dateEnd = null;

        int completedStart = 0;
        int completedEnd = 100;

        if(percentageStart > percentageEnd){
            completedEnd = percentageStart;
            completedStart = percentageEnd;
        } else {
            completedStart = percentageStart;
            completedEnd = percentageEnd;
        }

        Calendar cal = new GregorianCalendar();

        if(dateFilter == GoalUtils.HISTORY_ALL){
            dateStart = null;
            dateEnd = null;
        } else if(dateFilter == GoalUtils.HISTORY_WEEK){
            cal.setTime(new Date(System.currentTimeMillis()));

            dateEnd = cal.getTime();
            cal.add(Calendar.DAY_OF_YEAR, -7);
            dateStart = cal.getTime();
        } else if(dateFilter == GoalUtils.HISTORY_MONTH){
            cal.setTime(new Date(System.currentTimeMillis()));

            dateEnd = cal.getTime();
            cal.add(Calendar.MONTH, -1);
            dateStart = cal.getTime();
        } else if(dateFilter == GoalUtils.HISTORY_CUSTOM){

            if(startDate != null) {
                cal.setTime(startDate);
                if (cal.get(Calendar.YEAR) < 1970) {
                    startDate = null;
                }
            }
            if(endDate != null) {
                cal.setTime(endDate);
                if (cal.get(Calendar.YEAR) < 1970) {
                    endDate = null;
                }
            }

            if(startDate != null && endDate != null){
                if(startDate.compareTo(endDate) == 1){
                    dateStart = endDate;
                    dateEnd = startDate;
                } else {
                    dateStart = startDate;
                    dateEnd = endDate;
                }
            } else {
                dateStart = startDate;
                dateEnd = endDate;
            }

        }

        //Log.i("GOALUTILS, FILTERING", "Mode: " + dateFilter + "Date Start: " + dateStart + ", Date End: " + dateEnd +
        //", % start: " + completedStart + ", % end: " + completedEnd);

        return goalDAO.findAllFinishedByFilters(dateStart, dateEnd, completedStart, completedEnd);

    }

    public Goal getMaxActivity(String dateFilter, Date startDate, Date endDate, int percentageStart, int percentageEnd){
        List<Goal> filteredGoals = filterHistory(dateFilter, startDate, endDate, percentageStart, percentageEnd);

        Log.i("GUtilsMAX", "filtered: " + filteredGoals.size());
        if(filteredGoals.size() > 0) {
            double maxActivity = unitsUtils.getUnitsInSteps(filteredGoals.get(0).getGoalUnits(), filteredGoals.get(0).getGoalCompleted());
            Goal maxGoal = filteredGoals.get(0);

            for(int i = 1; i < filteredGoals.size(); i++){
                double thisActivty = unitsUtils.getUnitsInSteps(filteredGoals.get(i).getGoalUnits(), filteredGoals.get(0).getGoalCompleted());
                if(thisActivty > maxActivity){
                    Log.i("GUtilsMAX", "Swapped " + maxGoal.getName() + ": " + maxActivity + ", for " +
                            filteredGoals.get(i) + ": " + thisActivty);
                    maxActivity = thisActivty;
                    maxGoal = filteredGoals.get(i);
                }
            }

            return maxGoal;
        }
        return null;
    }

    public Goal getMinActivity(String dateFilter, Date startDate, Date endDate, int percentageStart, int percentageEnd){
        List<Goal> filteredGoals = filterHistory(dateFilter, startDate, endDate, percentageStart, percentageEnd);

        if(filteredGoals.size() > 0) {
            double minActivity = unitsUtils.getUnitsInSteps(filteredGoals.get(0).getGoalUnits(), filteredGoals.get(0).getGoalCompleted());
            Goal minGoal = filteredGoals.get(0);

            for(int i = 1; i < filteredGoals.size(); i++){
                double thisActivty = unitsUtils.getUnitsInSteps(filteredGoals.get(i).getGoalUnits(), filteredGoals.get(0).getGoalCompleted());
                if(thisActivty < minActivity){
                    Log.i("GUMIN", "Swapped " + minGoal.getName() + ": " + minActivity + ", for " +
                        filteredGoals.get(i) + ": " + thisActivty);
                    minActivity = thisActivty;
                    minGoal = filteredGoals.get(i);

                }
            }

            return minGoal;
        }
        return null;
    }





}
