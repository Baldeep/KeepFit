package cs551.baldeep.keepfit;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AppCompatActivity;

import java.sql.SQLException;

import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.listeners.DeleteAllGoalsPreferenceOnClickListener;
import cs551.baldeep.listeners.DeleteHistoryPreferenceOnClickListener;
import cs551.baldeep.listeners.StrideLenghtPreferenceOnClickListener;
import cs551.baldeep.utils.Constants;


public class SettingsPage extends AppCompatActivity {

    protected GoalDAO goalDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            goalDAO = new GoalDAO(getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
            Thread.UncaughtExceptionHandler ueh = Thread.getDefaultUncaughtExceptionHandler();
            ueh.uncaughtException(Thread.currentThread(), e);
        }


        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    public static class MyPreferenceFragment extends PreferenceFragment {

        private GoalDAO goalDAO;

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            try {
                goalDAO = new GoalDAO(this.getActivity().getApplicationContext());
            } catch (SQLException e) {
                e.printStackTrace();
                Thread.UncaughtExceptionHandler ueh = Thread.getDefaultUncaughtExceptionHandler();
                ueh.uncaughtException(Thread.currentThread(), e);
            }

            Preference setStrideLen = (Preference) findPreference(Constants.STRIDE_LENGTH);
            setStrideLen.setOnPreferenceClickListener(new StrideLenghtPreferenceOnClickListener(getFragmentManager()));

            Preference clearHistory = (Preference) findPreference("clear_db_history");
            clearHistory.setOnPreferenceClickListener(new DeleteHistoryPreferenceOnClickListener(getFragmentManager()));

            Preference clearAllData = (Preference) findPreference("clear_all_data");
            clearAllData.setOnPreferenceClickListener(new DeleteAllGoalsPreferenceOnClickListener(getFragmentManager()));
        }
    }
}