package cs551.baldeep.listeners;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.Preference;

import cs551.baldeep.dialogs.AddActivityDialog;
import cs551.baldeep.dialogs.StrideLengthDialog;
import cs551.baldeep.utils.Constants;

/**
 * Created by balde on 20/03/2017.
 */

public class StrideLenghtPreferenceOnClickListener implements Preference.OnPreferenceClickListener {

    private FragmentManager fragmentManager;

    public StrideLenghtPreferenceOnClickListener(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        DialogFragment addActivityDialog = new StrideLengthDialog();
        addActivityDialog.show(fragmentManager, "Set Stride Length");
        return true;
    }
}
