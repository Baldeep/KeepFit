package cs551.baldeep.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.sql.SQLException;

import cs551.baldeep.utils.Constants;
import cs551.baldeep.dao.GoalDAO;

/**
 * Created by balde on 13/02/2017.
 */

public class DeleteGoalDialog extends DialogFragment{

    /*protected boolean confirmed = false;

    public boolean showConfirmDialog(final Activity activity){*/


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getActivity());

        confirmDialog.setTitle("Delete Goal");
        confirmDialog.setMessage("Are you sure you want to delete this goal?");
        confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    GoalDAO goalDAO = new GoalDAO(getActivity().getApplicationContext());
                    Log.d("Confirm", getArguments().getString(Constants.GOAL_ID));
                    goalDAO.deleteById(getArguments().getString(Constants.GOAL_ID));
                    getActivity().finish();
                    dismiss();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        confirmDialog.setNegativeButton("No", null);
        return confirmDialog.create();
    }
}
