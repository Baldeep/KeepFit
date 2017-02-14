package cs551.baldeep.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.List;

import cs551.baldeep.keepfit.R;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.Units;

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

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd (EEE)");

        activityDate.setText(sdf.format(getItem(position).getDateOfGoal()));
        Goal g = getItem(position);
        goalName.setText(g.getName());
        if(g.getGoalUnits().equals(Units.STEPS)) {
            activityValue.setText((int) getItem(position).getGoalCompleted()
                    + "/" + (int) getItem(position).getGoalValue()
                    + " " + g.getGoalUnits());
        } else {
            activityValue.setText(getItem(position).getGoalCompleted()
                    + "/" + getItem(position).getGoalValue()
                    + " " + g.getGoalUnits());
        }

        goalProgress.setProgress((int)g.getPercentageCompleted());

        return activtyRow;
    }
}
