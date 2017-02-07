package cs551.baldeep.keepfit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TabHost;
import android.widget.TextView;

import com.eralp.circleprogressview.CircleProgressView;

import java.util.List;

import cs551.baldeep.models.Goal;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Goal currentGoal;
    private List<Goal> goalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        setUpTabs();

        TextView dailyGoalName = (TextView) findViewById(R.id.currentGoalName);
        if(currentGoal != null){
            dailyGoalName.setText(currentGoal.getName());
        } else {
            dailyGoalName.setText(R.string.no_goal_message);
        }

        currentGoal = new Goal("New Goal", 10000, "Steps");
        currentGoal.setGoalDone(5000);

        // ProgressBar
        CircleProgressView mCircleProgressView =
                                (CircleProgressView) findViewById(R.id.circle_progress_view);
        mCircleProgressView.setTextEnabled(true);
        mCircleProgressView.setInterpolator(new AccelerateDecelerateInterpolator());
        mCircleProgressView.setStartAngle(270);
        mCircleProgressView.setUsePercentage(false);

        if(currentGoal != null){
            double progress = ((double) currentGoal.getGoalDone()/(double)currentGoal.getGoalMax())*100;
            mCircleProgressView.setProgressWithAnimation((int) progress, 2000);
            mCircleProgressView.setTextString(currentGoal.getGoalDone()+"");
            mCircleProgressView.setTextSuffix("/" + currentGoal.getGoalMax()
                                                    + "\n" + currentGoal.getGoalUnits());
        } else {
            mCircleProgressView.setTextSuffix("/0 Steps");
        }


        TextView progressDone = (TextView) findViewById(R.id.progressDone);
        TextView progressMax = (TextView) findViewById(R.id.progressMax);
        TextView progressUnits = (TextView) findViewById(R.id.progressUnits);
        if(currentGoal != null){
            progressDone.setText(currentGoal.getGoalDone() + "");
            progressMax.setText(currentGoal.getGoalMax() + "");
            progressUnits.setText(currentGoal.getGoalUnits() + "");
        } else {
            progressDone.setText("0");
            progressMax.setText("0");
            progressUnits.setText("Steps");
        }

        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setUpTabs(){
        // Tabs
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        //Home tab
        TabHost.TabSpec specs = tabHost.newTabSpec("Home");
        specs.setContent(R.id.tabhome);
        specs.setIndicator("Home");
        tabHost.addTab(specs);

        // History tab
        specs = tabHost.newTabSpec("History");
        specs.setContent(R.id.tabhistory);
        specs.setIndicator("History");
        tabHost.addTab(specs);

        // Stats tab
        specs = tabHost.newTabSpec("Statistics");
        specs.setContent(R.id.tabstats);
        specs.setIndicator("Statistics");
        tabHost.addTab(specs);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else */if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
