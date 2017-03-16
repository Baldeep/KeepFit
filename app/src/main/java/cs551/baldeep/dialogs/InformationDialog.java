package cs551.baldeep.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.sql.SQLException;

import cs551.baldeep.constants.Constants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.keepfit.R;

/**
 * Created by balde on 13/02/2017.
 */

public class InformationDialog extends DialogFragment{

    /*protected boolean confirmed = false;

    public boolean showConfirmDialog(final Activity activity){*/


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getActivity());

        confirmDialog.setTitle(getArguments().getString(Constants.TITLE));
        confirmDialog.setMessage(getArguments().getString(Constants.MESSAGE));
        confirmDialog.setIcon(R.drawable.ic_menu_share);
        confirmDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dismiss();

            }
        });
        return confirmDialog.create();
    }
}
