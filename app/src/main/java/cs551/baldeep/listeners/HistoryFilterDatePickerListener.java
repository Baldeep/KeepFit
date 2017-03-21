package cs551.baldeep.listeners;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.view.View;

import java.util.Date;

import cs551.baldeep.dialogs.PastDatePickerDialog;
import cs551.baldeep.utils.AppUtils;
import cs551.baldeep.utils.Constants;

/**
 * Created by balde on 20/03/2017.
 */

public class HistoryFilterDatePickerListener implements View.OnClickListener {

    private Date today;
    private FragmentManager fragmentManager;
    private SharedPreferences sharedPreferences;
    private String startOrEnd;
    private String historyOrStats;

    public HistoryFilterDatePickerListener(FragmentManager fragmentManager,
                                           SharedPreferences sharedPreferences,
                                           String historyOrStats,
                                           String startOrEnd,
                                           Date today){
        this.fragmentManager = fragmentManager;
        this.sharedPreferences = sharedPreferences;
        this.historyOrStats = historyOrStats;
        this.startOrEnd = startOrEnd;
        this.today = today;
    }


    @Override
    public void onClick(View v) {

        String prefix = "filter_";
        if(historyOrStats.equals(Constants.FILTER_STATS_MODE)){
            prefix += "stats_";
        }

        Date startFilterDate = AppUtils.getSharedPreferecnceDate(sharedPreferences, prefix + Constants.START + "_");
        Date endFilterDate = endFilterDate = AppUtils.getSharedPreferecnceDate(sharedPreferences, prefix + Constants.END + "_");


        PastDatePickerDialog datePicker = new PastDatePickerDialog();
        datePicker.setInitialDate(today);

        if(startOrEnd.equals(Constants.START)) {

            datePicker.setListener(new FilterDatePickerListener(sharedPreferences, prefix + Constants.START + "_"));

            if (!AppUtils.dateIsOutOfBounds(endFilterDate)) {
                datePicker.setMaxDate(endFilterDate);
            }
            if (!AppUtils.dateIsOutOfBounds(startFilterDate)) {
                datePicker.setInitialDate(startFilterDate);
            } else {
                datePicker.setInitialDate(new Date(System.currentTimeMillis()));
            }
        } else {
            datePicker.setListener(new FilterDatePickerListener(sharedPreferences, prefix + Constants.END + "_"));

            if(!AppUtils.dateIsOutOfBounds(startFilterDate)){
                datePicker.setMinDate(startFilterDate);
            }
            if (!AppUtils.dateIsOutOfBounds(endFilterDate)){
                datePicker.setInitialDate(endFilterDate);
            } else {
                datePicker.setInitialDate(new Date(System.currentTimeMillis()));
            }
        }
        datePicker.show(fragmentManager, "Date Picker");
    }
}

