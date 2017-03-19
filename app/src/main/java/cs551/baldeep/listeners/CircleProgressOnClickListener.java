package cs551.baldeep.listeners;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import cs551.baldeep.utils.Constants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.dialogs.AddActivityDialog;
import cs551.baldeep.dialogs.InformationDialog;
import cs551.baldeep.models.Goal;

/**
 * Created by balde on 16/03/2017.
 */

public class CircleProgressOnClickListener implements View.OnClickListener {

    private FragmentManager fragmentManager;
    private GoalDAO goalDAO;

    public CircleProgressOnClickListener(FragmentManager fragmentManager, GoalDAO goalDAO){
        this.fragmentManager = fragmentManager;
        this.goalDAO = goalDAO;
    }


    @Override
    public void onClick(View v) {
        Goal currentGoal = goalDAO.findCurrentGoal();

        if(currentGoal != null) {
            DialogFragment addActivityDialog = new AddActivityDialog();
            Bundle addActivityBundle = new Bundle();
            addActivityBundle.putString(Constants.GOAL_ID, currentGoal.getGoalUUID());
            addActivityDialog.setArguments(addActivityBundle);

            addActivityDialog.show(fragmentManager, "Add Activity");
        } else {
            Toast.makeText(v.getContext(), "No current", Toast.LENGTH_SHORT).show();
            DialogFragment infoDialog = new InformationDialog();
            Bundle infoBundle = new Bundle();
            infoBundle.putString(Constants.TITLE, "Error Adding Activity");
            infoBundle.putString(Constants.MESSAGE, "Can't add activity until you create a goal.");
            infoBundle.putString(Constants.MODE, Constants.ERROR);

            infoDialog.setArguments(infoBundle);
            infoDialog.show(fragmentManager, "Error");
        }
    }

}
