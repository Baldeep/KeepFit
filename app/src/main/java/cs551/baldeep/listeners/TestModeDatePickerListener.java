package cs551.baldeep.listeners;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Date;
import java.util.Calendar;

/**
 * Created by balde on 19/03/2017.
 */

public class TestModeDatePickerListener extends Activity implements DatePickerDialog.OnDateSetListener  {

    private Activity activity;
    private SharedPreferences sharedPreferences;

    public TestModeDatePickerListener(Activity activity, SharedPreferences sharedPreferences){
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 1);

        Date selectedDate = calendar.getTime();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("test_mode_date_year", year);
        editor.putInt("test_mode_date_month", month);
        editor.putInt("test_mode_date_day", dayOfMonth);
        editor.commit();
    }

}
