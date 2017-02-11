package cs551.baldeep.keepfit;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cs551.baldeep.models.Goal;

/**
 * Created by balde on 11/02/2017.
 */

public class GoalListAdapter extends ArrayAdapter<Goal> {

    public GoalListAdapter(Context context, List<Goal> goals) {
        super(context, R.layout.goal_list_row_layout, goals);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View goalRow = inflater.inflate(R.layout.goal_list_row_layout, parent, false);

        TextView goalName = (TextView) goalRow.findViewById(R.id.txt_goalname_row);
        TextView goalMax = (TextView) goalRow.findViewById(R.id.txt_goalmax_row);

        goalName.setText(getItem(position).getName());

        goalMax.setText(getItem(position).getGoalMax() + " " + getItem(position).getGoalUnits());

        return goalRow;
    }
}
