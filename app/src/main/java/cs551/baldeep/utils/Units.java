package cs551.baldeep.utils;

/**
 * Created by balde on 12/02/2017.
 */

public class Units {

    public static final String STEPS = "Steps";
    public static final String KM = "Km";
    public static final String M = "m";
    public static final String MILES = "Miles";
    public static final String YARDS = "Yards";

    public static double STEP_LENGHT_M = 1.0;

    public static double getStepsInM(double steps){
        return steps * STEP_LENGHT_M;
    }

    public static double getStepsInKM(double steps){
        return (steps * STEP_LENGHT_M)/1000;
    }

    public static double getStepsInMiles(double steps){
        return (steps * STEP_LENGHT_M) * 0.000621371;
    }

    public static double getStepsInYards(double steps){
        return (steps * STEP_LENGHT_M) * 1.09361;
    }

    public static String[] getUnitStrings(){
        String [] units = {STEPS, KM, M, MILES, YARDS};
        return units;
    }
}
