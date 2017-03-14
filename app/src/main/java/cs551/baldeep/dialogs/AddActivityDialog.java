package cs551.baldeep.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;

import java.sql.SQLException;

import cs551.baldeep.constants.Constants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.keepfit.R;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.GoalUtils;
import cs551.baldeep.utils.Units;


public class AddActivityDialog extends DialogFragment{

    protected String goalUUID;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoalDAO goalDAO = null;
        try {
            goalDAO = new GoalDAO(getActivity().getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        goalUUID = getArguments().getString(Constants.GOAL_ID);
        Goal goal = goalDAO.findById(goalUUID);


        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialog_layout = inflater.inflate(R.layout.dialog_add_activity, null);

        Spinner addActivityUnitsSpinner = (Spinner) dialog_layout.findViewById(R.id.spinner_activity_entered_units);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, Units.getUnitStrings());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        addActivityUnitsSpinner.setAdapter(adapter);
        addActivityUnitsSpinner.setSelection(adapter.getPosition(goal.getGoalUnits()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialog_layout);
        builder.setTitle("Add Goal");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText activity = (EditText) getDialog().findViewById(R.id.txt_activity_entered);
                Spinner unitsSpinner = (Spinner) getDialog().findViewById(R.id.spinner_activity_entered_units);

                String activityToAdd = String.valueOf(activity.getText());
                if(activityToAdd.trim().isEmpty()){
                    activityToAdd = "0";
                }

                GoalUtils goalUtils = new GoalUtils(getActivity().getApplicationContext());
                Goal currentGoal = goalUtils.addActivityToGoal(goalUUID,
                        Double.valueOf(activityToAdd),
                        unitsSpinner.getSelectedItem().toString());

                TextView progressText = (TextView) getActivity().findViewById(R.id.txt_goalprogress);
                if(unitsSpinner.getSelectedItem().toString().equals(Units.STEPS)){
                    progressText.setText((int)currentGoal.getGoalCompleted() + "/" + (int)currentGoal.getGoalValue()
                            + " " + currentGoal.getGoalUnits());
                } else {
                    progressText.setText(currentGoal.getGoalCompleted() + "/" + currentGoal.getGoalValue()
                            + " " + currentGoal.getGoalUnits());
                }

                CircleProgressView mCircleProgressView = (CircleProgressView) getActivity().findViewById(R.id.circle_progress_view);
                mCircleProgressView.setProgress(currentGoal.getPercentageCompleted());

                Toast.makeText(getActivity().getApplicationContext(), "Activity recorded", Toast.LENGTH_SHORT);
                //getTargetFragment().onActivityResult(getTargetRequestCode(), 0, getActivity().getIntent());
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
