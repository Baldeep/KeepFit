package cs551.baldeep.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import cs551.baldeep.keepfit.R;
import cs551.baldeep.models.Goal;

/**
 * Created by balde on 11/02/2017.
 */

public class ActivityListAdapter extends ArrayAdapter<Goal> {

    public ActivityListAdapter(Context context, List<Goal> history) {
        super(context, R.layout.history_list_row_layout, history);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View activtyRow = inflater.inflate(R.layout.history_list_row_layout, parent, false);

        TextView activityDate = (TextView) activtyRow.findViewById(R.id.txt_history_activitydate_row);
        TextView goalName = (TextView) activtyRow.findViewById(R.id.txt_history_goalname_row);
        TextView activityValue = (TextView) activtyRow.findViewById(R.id.txt_history_value_row);

        ProgressBar goalProgress = (ProgressBar)  activtyRow.findViewById(R.id.progressBar_history_steps);

        activityDate.setText(getItem(position).getDateOfGoal().toString());

        // TODO: get goal by date
        Goal g = getItem(position);
        g.setPercentageCompleted((int)((double)g.getGoalCompleted()/(double)g.getGoalValue())*100);
        goalName.setText(g.getName());

        activityValue.setText((int) getItem(position).getGoalCompleted() + " " + g.getGoalUnits());


        goalProgress.setProgress(g.getPercentageCompleted());



        return activtyRow;
    }
}
