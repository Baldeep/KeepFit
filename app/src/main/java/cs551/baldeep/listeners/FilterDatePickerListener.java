package cs551.baldeep.listeners;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Date;
import java.util.Calendar;

import cs551.baldeep.utils.Constants;

/**
 * Created by balde on 19/03/2017.
 */

public class FilterDatePickerListener implements DatePickerDialog.OnDateSetListener  {

    private SharedPreferences sharedPreferences;
    private String prefix;

    public FilterDatePickerListener(SharedPreferences sharedPreferences, String prefix){
        this.sharedPreferences = sharedPreferences;
        this.prefix = prefix;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 1);

        Date selectedDate = calendar.getTime();

        Log.i("DatePickerListener", "Date: " + selectedDate.toString() + sharedPreferences.toString());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(prefix + Constants.DATE_YEAR, year);
        editor.putInt(prefix + Constants.DATE_MONTH, month);
        editor.putInt(prefix + Constants.DATE_DAY, dayOfMonth);
        editor.apply();

    }

}

