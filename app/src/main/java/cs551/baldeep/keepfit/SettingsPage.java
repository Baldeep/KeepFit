package cs551.baldeep.keepfit;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class SettingsPage extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.settings);
    }

}