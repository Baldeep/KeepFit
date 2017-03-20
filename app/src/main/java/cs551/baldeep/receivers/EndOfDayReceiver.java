package cs551.baldeep.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.models.Goal;

/**
 * Created by balde on 18/03/2017.
 */

public class EndOfDayReceiver extends BroadcastReceiver {

    private GoalDAO goalDAO;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "KeepFit - ended day", Toast.LENGTH_LONG).show();
    }
}
