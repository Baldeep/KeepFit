package cs551.baldeep.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import cs551.baldeep.utils.Constants;
import cs551.baldeep.keepfit.AddGoalPage;
import cs551.baldeep.models.Goal;

/**
 * Created by balde on 14/03/2017.
 */

public class GoalListEditItemOnClickListener implements AdapterView.OnItemClickListener {

    private ListView listOfGoals;
    private Activity parentActivity;


    public GoalListEditItemOnClickListener(Activity parentActivity, ListView listOfGoals){
        this.listOfGoals = listOfGoals;
        this.parentActivity = parentActivity;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Goal selected = (Goal) listOfGoals.getItemAtPosition(position);

        Intent editGoal = new Intent(parentActivity, AddGoalPage.class);
        editGoal.putExtra(Constants.ADD_GOAL_MODE, "edit");
        editGoal.putExtra(Constants.GOAL_ID, selected.getGoalUUID());
        parentActivity.startActivityForResult(editGoal, 3);

    }
}
