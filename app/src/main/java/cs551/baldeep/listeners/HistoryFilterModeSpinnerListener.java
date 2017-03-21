package cs551.baldeep.listeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
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

public class HistoryFilterModeSpinnerListener implements AdapterView.OnItemSelectedListener {

    private SharedPreferences sharedPreferences;
    private Activity activity;
    private String filter_x_mode;

    public HistoryFilterModeSpinnerListener(Activity activity, SharedPreferences sharedPreferences, String filter_x_mode){
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
        this.filter_x_mode = filter_x_mode;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String historyFilterDateMode = parent.getItemAtPosition(position).toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(filter_x_mode, historyFilterDateMode);
        editor.apply();

        LinearLayout customBtns;
        if(filter_x_mode.equals(Constants.FILTER_HISTORY_MODE)) {
            customBtns = (LinearLayout) activity.findViewById(R.id.history_custom_filter_layout);
        } else {
            customBtns = (LinearLayout) activity.findViewById(R.id.stats_custom_filter_layout);
        }

        if(historyFilterDateMode == GoalUtils.HISTORY_CUSTOM){
            customBtns.setVisibility(View.VISIBLE);
        } else {
            customBtns.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        Spinner historyDateSpinner = (Spinner) activity.findViewById(R.id.history_date_filter_spinner);
        historyDateSpinner.setSelection(0);
    }
}
