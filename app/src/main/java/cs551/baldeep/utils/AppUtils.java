package cs551.baldeep.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;
import java.util.Calendar;

import cs551.baldeep.receivers.EndOfDayReceiver;

/**
 * Created by balde on 18/03/2017.
 */

public class AppUtils {

    public static void setUpAlarm(Context context){
        Intent intent = new Intent(context, EndOfDayReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(alarmIntent);
        alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, alarmIntent);
    }

    public static Date getSharedPreferecnceDate(SharedPreferences sharedPreferences, String prefix){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        int year = sharedPreferences.getInt(prefix + Constants.DATE_YEAR, 0);
        int month = sharedPreferences.getInt(prefix + Constants.DATE_MONTH, 0);
        int dayOfMonth = sharedPreferences.getInt(prefix + Constants.DATE_DAY, 0);

        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 1);

        //Log.i("AppUtilsSharedPrefDate", "Date: " + dayOfMonth + "/" + month + "/" + year);

        return calendar.getTime();
    }

    public static boolean dateIsOutOfBounds(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        if(c.get(Calendar.YEAR) < 1970){
            return true;
        } else {
            return false;
        }
    }


}
