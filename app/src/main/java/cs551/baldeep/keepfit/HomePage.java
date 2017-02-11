package cs551.baldeep.keepfit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.eralp.circleprogressview.CircleProgressView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import cs551.baldeep.constants.BundleConstants;
import cs551.baldeep.models.Goal;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Goal currentGoal;
    private List<Goal> goalList;

    private TextView dailyGoalName;
    private CircleProgressView mCircleProgressView;
    private ListView listOfGoals;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // TODO: get this list from the db
        goalList = new ArrayList<Goal>();
        for(int i = 0; i < 10; i++) {
            goalList.add(new Goal("Goal " + i, i * 100, "Steps"));
        }

        setUpTabs();

        dailyGoalName = (TextView) findViewById(R.id.currentGoalName);
        mCircleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view);
        mCircleProgressView.setTextEnabled(true);
        mCircleProgressView.setInterpolator(new AccelerateDecelerateInterpolator());
        mCircleProgressView.setStartAngle(270);
        mCircleProgressView.setUsePercentage(false);

        updateUI();

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

        // Goals List View
        if(goalList.size()>0){
            TextView listReplacementText = (TextView) findViewById(R.id.txt_no_goals_list_replacement);
            listReplacementText.setVisibility(View.GONE);
        }
        String[] goalNames = new String[goalList.size()];
        for(int i = 0; i < goalList.size(); i++){
            goalNames[i] = goalList.get(i).getName();
        }


        listOfGoals = (ListView) findViewById(R.id.listview_goals);

        //ListAdapter theAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goalNames);
        //listOfGoals.setAdapter(theAdapter);
        ListAdapter listViewAdapter = new GoalListAdapter(this, goalList);
        listOfGoals.setAdapter(listViewAdapter);
        setListViewHeightBasedOnItems(listOfGoals);


        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_addgoal);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addGoalScreen = new Intent(view.getContext(),  AddGoal.class);
                final int result = 1;
                startActivityForResult(addGoalScreen, result);
            }
        });



        // Hamburger menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        TextView currentGoalName = (TextView) findViewById(R.id.currentGoalName);

        String goalName = data.getStringExtra(BundleConstants.goalName);
        int goalValue = data.getIntExtra(BundleConstants.goalValue, 0);
        String goalUnits = data.getStringExtra(BundleConstants.goalUnits);

        currentGoal = new Goal(goalName, goalValue, goalUnits);
        currentGoal.setGoalDone(6000);
        if(currentGoal == null){
            Log.d("Home", "Current goal is null after creating it");
        } else {
            Log.d("Home", "Current goal has been created successfully.");
        }


        updateUI();
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

    private void updateUI(){

        if(currentGoal != null)
            Log.d("Home", currentGoal.getName() + ": " + currentGoal.getGoalMax() + " " + currentGoal.getGoalUnits());

        // Goal name heading
        if(currentGoal != null){
            dailyGoalName.setText(currentGoal.getName());
        } else {
            dailyGoalName.setText(R.string.no_goal_message);
        }

        // ProgressBar
        if(currentGoal != null){
            double progress =
                    ((double) currentGoal.getGoalDone()/(double)currentGoal.getGoalMax())*100;
            mCircleProgressView.setProgressWithAnimation((int) progress, 2000);
            mCircleProgressView.setTextString(currentGoal.getGoalDone()+"");
            mCircleProgressView.setTextSuffix("/" + currentGoal.getGoalMax()
                    + "\n" + currentGoal.getGoalUnits());
        } else {
            mCircleProgressView.setTextSuffix("\t");
            mCircleProgressView.setTextString("0");
            mCircleProgressView.setTextSuffix("/0\nSteps");
        }
    }

    /*
     * From:
     * http://stackoverflow.com/questions/1778485/android-listview-display-all-available-items-without-scroll-with-static-header
     */
    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }
/*
    private String getProgressBarString(){
        String s = "";
        if(currentGoal == null){
            return "0/0 Steps";
        } else {
            if(currentGoal.getGoalUnits() == "Steps"){
                if(currentGoal.getGoalMax() % 1000 == 0){
                    s = currentGoal.getGoalDone() + "/" + (currentGoal.getGoalMax()/1000) + "k";
                } else {
                    s = currentGoal.getGoalDone() + "/" + currentGoal.getGoalMax();
                    for(i = 0; i < )
                }
            }
        }

        return s;
    }*/
}
