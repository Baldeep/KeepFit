package cs551.baldeep.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cs551.baldeep.keepfit.R;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.Units;

/**
 * Created by balde on 11/02/2017.
 */

public class HistoryListAdapter extends ArrayAdapter<Goal> {

    public HistoryListAdapter(Context context, List<Goal> history) {
        super(context, R.layout.history_list_row_layout, history);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        View activityRow = inflater.inflate(R.layout.history_list_row_layout, parent, false);

        TextView activityDate = (TextView) activityRow.findViewById(R.id.txt_history_activitydate_row);
        TextView goalName = (TextView) activityRow.findViewById(R.id.txt_history_goalname_row);
        TextView activityValue = (TextView) activityRow.findViewById(R.id.txt_history_value_row);

        ProgressBar goalProgress = (ProgressBar)  activityRow.findViewById(R.id.progressBar_history_steps);

        ImageView imageView = (ImageView) activityRow.findViewById(R.id.image_row_completed_icon);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd (EEE)");

        activityDate.setText(sdf.format(getItem(position).getDateOfGoal()));
        Goal g = getItem(position);
        goalName.setText(g.getName());
        if(g.getGoalUnits().equals(Units.STEPS)) {
            activityValue.setText((int) getItem(position).getGoalCompleted()
                    + "/" + (int) getItem(position).getGoalTarget()
                    + " " + g.getGoalUnits());
        } else {
            activityValue.setText(getItem(position).getGoalCompleted()
                    + "/" + getItem(position).getGoalTarget()
                    + " " + g.getGoalUnits());
        }
        if(g.getPercentageCompleted() >= 100){
            imageView.setImageResource(R.drawable.btn_star_big_on_pressed);;
        }


        goalProgress.setProgress((int)g.getPercentageCompleted());

        return activityRow;
    }
}
