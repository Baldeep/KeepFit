package cs551.baldeep.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.SQLException;

import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.keepfit.R;

/**
 * Created by balde on 13/02/2017.
 */

public class DeleteAllGoalsDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getActivity());

        confirmDialog.setTitle("Delete Goals");
        confirmDialog.setMessage("Are you sure you want to delete all goals? \n\nThis includes your " +
                        " history, your current goal and any other goals you may have created");
        confirmDialog.setIcon(R.drawable.ic_dialog_alert);
        confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    GoalDAO goalDAO = new GoalDAO(getActivity().getApplicationContext());
                    goalDAO.deleteAll();
                    Toast.makeText(getActivity().getApplicationContext(), "Deleted All Goals", Toast.LENGTH_LONG).show();
                    dismiss();

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        confirmDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        return confirmDialog.create();
    }
}
