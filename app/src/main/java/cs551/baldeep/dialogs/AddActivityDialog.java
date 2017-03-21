package cs551.baldeep.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;

import java.sql.SQLException;

import cs551.baldeep.utils.Constants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.keepfit.R;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.GoalUtils;
import cs551.baldeep.utils.Units;


public class AddActivityDialog extends DialogFragment{

    protected String goalUUID;

    protected GoalDAO goalDAO;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            goalDAO = new GoalDAO(getActivity().getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        goalUUID = getArguments().getString(Constants.GOAL_ID);


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialog_layout = inflater.inflate(R.layout.dialog_add_activity, null);


        Spinner addActivityUnitsSpinner = (Spinner) dialog_layout.findViewById(R.id.spinner_activity_entered_units);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, Units.getUnitStrings());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addActivityUnitsSpinner.setAdapter(adapter);
        addActivityUnitsSpinner.setSelection(adapter.getPosition(goalDAO.findById(goalUUID).getGoalUnits()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialog_layout);
        builder.setTitle("Add Goal");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText activity = (EditText) getDialog().findViewById(R.id.txt_activity_entered);
                Spinner unitsSpinner = (Spinner) getDialog().findViewById(R.id.spinner_activity_entered_units);

                String activityToAddString = String.valueOf(activity.getText());

                double activityToAdd = 0.0;

                if(!activityToAddString.trim().isEmpty()){
                    activityToAdd = Double.valueOf(activityToAddString);
                }

                Log.i("ADDACTIVITYDIALOG", "activity to add: " + activityToAddString);

                GoalUtils goalUtils = new GoalUtils(getActivity().getApplicationContext());
                Goal currentGoal = goalUtils.addActivityToGoal(goalUUID, activityToAdd, unitsSpinner.getSelectedItem().toString());

                TextView progressText = (TextView) getActivity().findViewById(R.id.txt_goalprogress);
                progressText.setText(goalUtils.getFormattedProgressString(goalDAO.findById(goalUUID)));

                CircleProgressView mCircleProgressView = (CircleProgressView) getActivity().findViewById(R.id.circle_progress_view);
                mCircleProgressView.setProgress(currentGoal.getPercentageCompleted());

                Toast.makeText(getActivity().getApplicationContext(), "Activity recorded", Toast.LENGTH_SHORT);

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
