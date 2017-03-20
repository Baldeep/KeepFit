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

    public HistoryFilterModeSpinnerListener(Activity activity, SharedPreferences sharedPreferences){
        this.activity = activity;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String historyFilterDateMode = parent.getItemAtPosition(position).toString();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.FILTER_HISTORY_MODE, historyFilterDateMode);
        editor.apply();

        LinearLayout customBtns = (LinearLayout) activity.findViewById(R.id.history_custom_filter_layout);
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
