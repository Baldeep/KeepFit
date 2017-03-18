package cs551.baldeep.listeners;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.preference.Preference;

import cs551.baldeep.dialogs.DeleteHistoryDialog;

/**
 * Created by balde on 17/03/2017.
 */

public class DeleteAllGoalsPreferenceOnClickListener implements Preference.OnPreferenceClickListener {

    private FragmentManager fragmentManager;

    public DeleteAllGoalsPreferenceOnClickListener(FragmentManager fragmentManager){
        this.fragmentManager = fragmentManager;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        DialogFragment confirm = new DeleteHistoryDialog();
        confirm.show(fragmentManager , "Confirm Delete");

        return true;
    }
}
