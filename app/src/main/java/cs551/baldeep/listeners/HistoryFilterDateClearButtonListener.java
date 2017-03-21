package cs551.baldeep.listeners;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;

import cs551.baldeep.utils.Constants;

/**
 * Created by balde on 20/03/2017.
 */

public class HistoryFilterDateClearButtonListener implements View.OnClickListener {

    private SharedPreferences sharedPreferences;
    private String histryOrStats;
    private String startOrEnd;

    public HistoryFilterDateClearButtonListener(SharedPreferences sharedPreferences, String historyOrStats, String startOrEnd){
        this.sharedPreferences = sharedPreferences;
        this.startOrEnd = startOrEnd;
        this.histryOrStats = historyOrStats;
    }

    @Override
    public void onClick(View v) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        String prefix = "filter_";
        if(histryOrStats.equals(Constants.FILTER_STATS_MODE)){
            prefix += "stats_";
        }
        prefix += startOrEnd + "_";

        editor.putInt(prefix + Constants.DATE_YEAR, 0);
        editor.putInt(prefix + Constants.DATE_MONTH, 0);
        editor.putInt(prefix + Constants.DATE_DAY, 0);
        editor.apply();
    }
}
