package cs551.baldeep.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import cs551.baldeep.constants.Constants;
import cs551.baldeep.keepfit.AddGoalPage;

/**
 * Created by balde on 16/03/2017.
 */

public class FABOnClickListener implements View.OnClickListener {

    private Activity activity;
    private int RESULT_ADD_GOAL;

    public FABOnClickListener(Activity activity, int addGoalCode){
        this.activity = activity;
        this.RESULT_ADD_GOAL = addGoalCode;
    }

    @Override
    public void onClick(View view) {
        Intent addGoalScreen = new Intent(view.getContext(),  AddGoalPage.class);
        addGoalScreen.putExtra(Constants.ADD_GOAL_MODE, Constants.ADD);
        activity.startActivityForResult(addGoalScreen, RESULT_ADD_GOAL);
    }
}
