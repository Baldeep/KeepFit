package cs551.baldeep.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cs551.baldeep.keepfit.R;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.Constants;
import cs551.baldeep.utils.Units;

/**
 * Created by balde on 11/02/2017.
 */

public class GoalListAdapter extends ArrayAdapter<Goal> {

    private List<Goal> goals;
    private SharedPreferences sharedPreferences;

    public GoalListAdapter(Context context, List<Goal> goals) {
        super(context, R.layout.goal_list_row_layout, goals);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.goals = goals;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View goalRow = inflater.inflate(R.layout.goal_list_row_layout, parent, false);

/*        if(sharedPreferences.getBoolean("lock_goals", false)){
            TextView editTextLabel = (TextView) goalRow.findViewById(R.id.txt_edit_goal_label);
            editTextLabel.setVisibility(View.INVISIBLE);
        }*/

        TextView goalName = (TextView) goalRow.findViewById(R.id.txt_goalname_row);
        TextView goalMax = (TextView) goalRow.findViewById(R.id.txt_goalmax_row);

        goalName.setText(getItem(position).getName());

        if(getItem(position).getGoalUnits() != null) {
            if (getItem(position).getGoalUnits().equals(Units.STEPS)) {
                goalMax.setText(((int) getItem(position).getGoalTarget()) + " " + getItem(position).getGoalUnits());
            } else {
                goalMax.setText(getItem(position).getGoalTarget() + " " + getItem(position).getGoalUnits());
            }
        }
        return goalRow;
    }

    public void setData(List<Goal> goals){
        this.goals = goals;
    }
}
