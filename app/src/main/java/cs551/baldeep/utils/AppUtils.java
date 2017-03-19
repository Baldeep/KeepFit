package cs551.baldeep.utils;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

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

    public static Date getTestModeDate(SharedPreferences sharedPreferences){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));

        int year = sharedPreferences.getInt(Constants.TEST_MODE_YEAR, calendar.get(Calendar.YEAR));
        int month = sharedPreferences.getInt(Constants.TEST_MODE_MONTH, calendar.get(Calendar.MONTH));
        int dayOfMonth = sharedPreferences.getInt(Constants.TEST_MODE_DAY, calendar.get(Calendar.DAY_OF_MONTH));

        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 1);

        return calendar.getTime();
    }

}
