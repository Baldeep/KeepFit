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
import android.widget.Toast;

import java.sql.SQLException;

import cs551.baldeep.constants.Constants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.keepfit.HomePage;
import cs551.baldeep.keepfit.R;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.GoalUtils;
import cs551.baldeep.utils.Units;

/**
 * Created by balde on 14/02/2017.
 */

public class AddActivityDialog extends DialogFragment {

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
                Spinner s = (Spinner) getDialog().findViewById(R.id.spinner_activity_entered_units);

                Log.i("ADD ACTIVITY DIALOG", activity.getText() + " " + s.getSelectedItem().toString() + " - " + goalUUID);
                GoalUtils goalUtils = new GoalUtils(getActivity().getApplicationContext());
                goalUtils.addActivityToGoal(goalUUID,
                        Double.valueOf(String.valueOf(activity.getText())),
                        s.getSelectedItem().toString());
                Toast.makeText(getActivity().getApplicationContext(), "Activity recorded", Toast.LENGTH_SHORT);
                //getTargetFragment().onActivityResult(getTargetRequestCode(), 0, getActivity().getIntent());

            }
        });

        return builder.create();
    }
}
