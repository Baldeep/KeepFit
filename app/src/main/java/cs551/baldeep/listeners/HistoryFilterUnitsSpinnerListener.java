package cs551.baldeep.listeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import cs551.baldeep.keepfit.R;
import cs551.baldeep.utils.Constants;
import cs551.baldeep.utils.GoalUtils;

/**
 * Created by balde on 20/03/2017.
 */

public class HistoryFilterUnitsSpinnerListener implements AdapterView.OnItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private Activity activity;
    private String historyOrGoals;

    public HistoryFilterUnitsSpinnerListener(Activity activity, SharedPreferences sharedPreferences, String historyOrGoals){
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
        this.historyOrGoals = historyOrGoals;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(historyOrGoals.equals(Constants.FILTER_HISTORY_MODE)) {
            editor.putString(Constants.FILTER_HISTORY_UNITS, parent.getItemAtPosition(position).toString());
        } else {
            editor.putString(Constants.FILTER_STATS_UNITS, parent.getItemAtPosition(position).toString());
        }
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Spinner filterUnitsSpinner;
        if(historyOrGoals.equals(Constants.FILTER_HISTORY_MODE)) {
            filterUnitsSpinner = (Spinner) activity.findViewById(R.id.history_units_filter_spinner);
        } else {
            filterUnitsSpinner = (Spinner) activity.findViewById(R.id.stats_units_filter_spinner);
        }
        filterUnitsSpinner.setSelection(0);
    }
}
