package cs551.baldeep.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by balde on 12/02/2017.
 */

public class Units {

    public static final String STEPS = "Steps";
    public static final String KM = "Km";
    public static final String M = "Meters";
    public static final String MILES = "Miles";
    public static final String YARDS = "Yards";

    public static final String ORIGINAL = "Original";

    public double STEP_LENGHT_M;

    public Units(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        STEP_LENGHT_M = (double) sharedPreferences.getFloat(Constants.STRIDE_LENGTH, (float) 0.7);
    }

    public double getStepsInM(double steps){
        return steps * STEP_LENGHT_M;
    }

    public double getStepsInKM(double steps){
        return (steps * STEP_LENGHT_M)/1000;
    }

    public double getStepsInMiles(double steps){
        return (steps * STEP_LENGHT_M) * 0.000621371;
    }

    public double getStepsInYards(double steps){
        return (steps * STEP_LENGHT_M) * 1.09361;
    }

    public static String[] getUnitStrings(){
        String [] units = {STEPS, KM, M, MILES, YARDS};
        return units;
    }

    public static String[] getFilterUnitStrings(){
        String [] units = {ORIGINAL, STEPS, KM, M, MILES, YARDS};
        return units;
    }

    public double getMInSteps(double meters){
        return meters/STEP_LENGHT_M;
    }

    public double getKMInSteps(double kms){
        return ((kms*1000)/STEP_LENGHT_M);
    }

    public double getMilesInSteps(double miles){
        return (miles*0.000621371)/STEP_LENGHT_M;
    }

    public double getYardsInSteps(double yards){
        return (yards*1.09361)/STEP_LENGHT_M;
    }

    public final double getUnitsInSteps(String units, double value){
        if(units.equals(Units.KM)){
            return getKMInSteps(value);
        } else if(units.equals(Units.M)){
            return getMInSteps(value);
        } else if(units.equals(Units.MILES)){
            return getMilesInSteps(value);
        } else if(units.equals(Units.YARDS)){
            return getYardsInSteps(value);
        } else if(units.equals(Units.STEPS)) {
            return value;
        } else{
            return 0.0;
        }
    }

    public final double getStepsInUnits(String units, double steps){
        switch(units){
            case Units.KM:
                return getStepsInKM(steps);
            case Units.M:
                return getStepsInM(steps);
            case Units.MILES:
                return getStepsInMiles(steps);
            case Units.YARDS:
                return getStepsInYards(steps);
            case Units.STEPS:
                return steps;
            default:
                return 0.0;
        }
    }
}
