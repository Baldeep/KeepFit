package cs551.baldeep.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import cs551.baldeep.utils.Constants;
import cs551.baldeep.keepfit.R;

/**
 * Created by balde on 13/02/2017.
 */

public class InformationDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getActivity());

        confirmDialog.setTitle(getArguments().getString(Constants.TITLE));
        confirmDialog.setMessage(getArguments().getString(Constants.MESSAGE));
        confirmDialog.setIcon(R.drawable.ic_dialog_alert);
        confirmDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dismiss();

            }
        });
        return confirmDialog.create();
    }
}
