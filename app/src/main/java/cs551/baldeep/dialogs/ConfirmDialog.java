package cs551.baldeep.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.SQLException;

import cs551.baldeep.constants.Constants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.keepfit.AddGoalPage;

/**
 * Created by balde on 13/02/2017.
 */

public class ConfirmDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getActivity());

        confirmDialog.setTitle("Delete Goal");
        confirmDialog.setMessage("Are you sure you want to delete this goal?");
        confirmDialog.setIcon(android.R.drawable.ic_dialog_alert);
        confirmDialog.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    GoalDAO goalDAO = new GoalDAO(getActivity().getApplicationContext());
                    if(goalDAO.deleteById(getArguments().getString(Constants.GOAL_ID))){
                        Toast.makeText(getActivity(), "Deleted Goal", Toast.LENGTH_SHORT).show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                getActivity().finish();
                dismiss();
            }
        });
        confirmDialog.setNegativeButton(android.R.string.no, null);
        return confirmDialog.create();
    }
}
