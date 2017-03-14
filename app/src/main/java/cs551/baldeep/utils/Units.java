package cs551.baldeep.utils;

/**
 * Created by balde on 12/02/2017.
 */

public class Units {

    public static final String STEPS = "Steps";
    public static final String KM = "Km";
    public static final String M = "Meters";
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

    public static double getMInSteps(double meters){
        return meters/STEP_LENGHT_M;
    }

    public static double getKMInSteps(double kms){
        return ((kms*1000)/STEP_LENGHT_M);
    }

    public static double getMilesInSteps(double miles){
        return (miles*0.000621371)/STEP_LENGHT_M;
    }

    public static double getYardsInSteps(double yards){
        return (yards*1.09361)/STEP_LENGHT_M;
    }

    public static double getUnitsInSteps(String units, double value){
        switch(units){
            case Units.KM:
                return getKMInSteps(value);
            case Units.M:
                return getMInSteps(value);
            case Units.MILES:
                return getMilesInSteps(value);
            case Units.YARDS:
                return getYardsInSteps(value);
            default:
                return 0.0;
        }
    }

    public static double getStepsInUnits(String units, double steps){
        switch(units){
            case Units.KM:
                return getStepsInKM(steps);
            case Units.M:
                return getStepsInM(steps);
            case Units.MILES:
                return getStepsInMiles(steps);
            case Units.YARDS:
                return getStepsInYards(steps);
            default:
                return 0.0;
        }
    }
}
