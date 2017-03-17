package cs551.baldeep.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.sql.SQLException;

import cs551.baldeep.constants.Constants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.keepfit.R;

/**
 * Created by balde on 13/02/2017.
 */

public class DeleteHistoryDialog extends DialogFragment{

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getActivity());

        confirmDialog.setTitle("Delete History");
        confirmDialog.setMessage("Are you sure you want to delete this your history?");
        confirmDialog.setIcon(R.drawable.ic_dialog_alert);
        confirmDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    GoalDAO goalDAO = new GoalDAO(getActivity().getApplicationContext());
                    goalDAO.deleteAllFinished();
                    Toast.makeText(getActivity().getApplicationContext(), "Cleared History", Toast.LENGTH_LONG).show();
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
