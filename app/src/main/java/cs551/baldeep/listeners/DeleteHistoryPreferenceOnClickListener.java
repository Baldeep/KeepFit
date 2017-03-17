package cs551.baldeep.listeners;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.Preference;

import cs551.baldeep.constants.Constants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.dialogs.DeleteGoalDialog;
import cs551.baldeep.dialogs.DeleteHistoryDialog;
import cs551.baldeep.dialogs.InformationDialog;

/**
 * Created by balde on 17/03/2017.
 */

public class DeleteHistoryPreferenceOnClickListener implements Preference.OnPreferenceClickListener {

    private GoalDAO goalDAO;
    private FragmentManager fragmentManager;

    public DeleteHistoryPreferenceOnClickListener(FragmentManager fragmentManager, GoalDAO goalDAO){
        this.fragmentManager = fragmentManager;
        this.goalDAO = goalDAO;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        DialogFragment confirm = new DeleteHistoryDialog();
        confirm.show(fragmentManager , "Confirm Delete");

        return true;
    }
}
