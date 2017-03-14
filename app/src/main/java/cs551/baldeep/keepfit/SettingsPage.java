package cs551.baldeep.keepfit;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

public class SettingsPage extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar bar;
        ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
        ListView content = (ListView) root.getChildAt(0);

        root.removeAllViews();

        bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);


        int height;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }else{
            height = bar.getHeight();
        }

        content.setPadding(0, height, 0, 0);

        root.addView(content);
        root.addView(bar);

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addPreferencesFromResource(R.xml.settings);
    }

    /*@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout)findViewById();
        Toolbar bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
        root.addView(bar, 0); // insert at top
        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }*/

}