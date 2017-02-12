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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;

import java.sql.SQLException;
import java.util.List;

import cs551.baldeep.adapters.GoalListAdapter;
import cs551.baldeep.constants.BundleConstants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.models.Goal;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Goal currentGoal;
    private List<Goal> goalList;

    private TextView dailyGoalName;
    private CircleProgressView mCircleProgressView;
    private TextView progressText;

    private ListView listOfGoals;

    private GoalDAO goalDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        try {
            goalDAO = new GoalDAO(getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
            Thread.UncaughtExceptionHandler ueh = Thread.getDefaultUncaughtExceptionHandler();
            ueh.uncaughtException(Thread.currentThread(), e);
        }

        goalList = goalDAO.findAll();
        currentGoal = goalDAO.findCurrentGoal();

        setUpTabs();

        dailyGoalName = (TextView) findViewById(R.id.currentGoalName);
        mCircleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view);
        mCircleProgressView.setInterpolator(new AccelerateDecelerateInterpolator());
        progressText = (TextView) findViewById(R.id.txt_goalprogress);


        // Goals List View
        if(goalList.size()>0){
            TextView listReplacementText = (TextView) findViewById(R.id.txt_no_goals_list_replacement);
            listReplacementText.setVisibility(View.GONE);
        }

        listOfGoals = (ListView) findViewById(R.id.listview_goals);
        listOfGoals.setFocusable(false);


        // Clicking progressBar
        mCircleProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked Progress circle", Toast.LENGTH_SHORT).show();
            }
        });

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

        updateUI();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("HomePage.OnResult", "requestCode: " + requestCode + ", resultCode: " + resultCode);
        if(resultCode == 1){
            String goalName = data.getStringExtra(BundleConstants.goalName);
            int goalValue = data.getIntExtra(BundleConstants.goalValue, 0);
            String goalUnits = data.getStringExtra(BundleConstants.goalUnits);

            Log.d("Return from Add", "goalval: " + goalValue);

            Goal newGoal = new Goal(goalName, goalValue, goalUnits);

            if(!goalDAO.saveOrUpdate(newGoal)){
                Toast.makeText(this, "Failed to add Goal", Toast.LENGTH_SHORT).show();
            }

            if(currentGoal == null){
                currentGoal = newGoal;
                Toast.makeText(this, "Current Goal set", Toast.LENGTH_SHORT).show();
            }

            updateUI();
        }
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

        if(currentGoal != null) {
            Log.d("Home", currentGoal.getName() + ": " + currentGoal.getGoalValue()
                    + " " + currentGoal.getGoalUnits());
        }

        // Goal name heading
        if(currentGoal != null){
            dailyGoalName.setText(currentGoal.getName());
        } else {
            dailyGoalName.setText(R.string.no_goal_message);
        }

        // ProgressBar
        if(currentGoal != null){
            mCircleProgressView.setTextString("Add Activity");
        } else {
            mCircleProgressView.setTextString("No goal");
        }

        // Progress Text
        if(currentGoal != null){
            progressText.setText(currentGoal.getGoalCompleted()+"/"+currentGoal.getGoalValue()
                    + " " + currentGoal.getGoalUnits());
        } else {
            progressText.setText("0/0 Steps");
        }

        // ListView HomePage
        goalList = goalDAO.findAll();
        ListAdapter goalListAdapter = new GoalListAdapter(this, goalList);
        listOfGoals.setAdapter(goalListAdapter);
        setListViewHeightBasedOnItems(listOfGoals);
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
}
