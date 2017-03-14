package cs551.baldeep.keepfit;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cs551.baldeep.adapters.ActivityListAdapter;
import cs551.baldeep.adapters.GoalListAdapter;
import cs551.baldeep.constants.Constants;
import cs551.baldeep.controllers.GoalListEditItemOnClickListener;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.dialogs.AddActivityDialog;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.Units;

public class HomePage extends AppCompatActivity {

    private static final int RESULT_ADD_GOAL = 1;
    private static final int RESULT_SETTINGS = 2;
    private static final int RESULT_EDIT_GOAL = 3;
    private Goal currentGoal;
    private List<Goal> goalList;

    protected DrawerLayout drawer;

    private TextView dailyGoalName;
    private TextView progressBarText;
    private CircleProgressView mCircleProgressView;
    private TextView progressText;
    private Spinner addActivityUnitsSpinner;

    private ListView listOfGoals;
    private GoalListEditItemOnClickListener listOfGoalsItemClickListener;
    private ListView listOfHistory;

    private GoalDAO goalDAO;

    private Date today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        //TODO get date from test mode
        today = new Date(System.currentTimeMillis());

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

        progressBarText = (TextView) findViewById(R.id.txt_progress_view);
        dailyGoalName = (TextView) findViewById(R.id.currentGoalName);
        mCircleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view);
        mCircleProgressView.setInterpolator(new AccelerateDecelerateInterpolator());
        mCircleProgressView.setStartAngle(-90);


        progressText = (TextView) findViewById(R.id.txt_goalprogress);


        // Goals List View
        if(goalList.size()>0){
            TextView listReplacementText = (TextView) findViewById(R.id.txt_no_goals_list_replacement);
            listReplacementText.setVisibility(View.GONE);
        }




        listOfGoals = (ListView) findViewById(R.id.listview_goals);
        listOfGoals.setFocusable(false);

        listOfGoalsItemClickListener = new GoalListEditItemOnClickListener(this, listOfGoals);
        listOfGoals.setOnItemClickListener(listOfGoalsItemClickListener);

        List<Goal> doneGoals = new ArrayList<Goal>();
        Random r = new Random();
        for(int i = 0; i < 7; i++){
            Goal g = new Goal("Goal " + i, i * 1000, Units.STEPS);
            g.setGoalUUID("oldgoals-" + i);
            g.setDateOfGoal(new Date(System.currentTimeMillis() - (86400002 * i)));
            g.setGoalCompleted(r.nextInt((i * 1000)+ 500));
            g.setDone(true);
            goalDAO.saveOrUpdate(g);
            doneGoals.add(g);
        }

        List<Goal> goalsFinished = goalDAO.findAllFinished();
        listOfHistory = (ListView) findViewById(R.id.list_history);
        ListAdapter historyAdapter = new ActivityListAdapter(this,goalsFinished);
        listOfHistory.setAdapter(historyAdapter);
        listOfHistory.setFocusable(false);
        setListViewHeightBasedOnItems(listOfHistory);



        // Clicking progressBar
        mCircleProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked Progress circle", Toast.LENGTH_SHORT).show();
                DialogFragment addActivityDialog = new AddActivityDialog();
                Bundle addActivityBundle = new Bundle();
                addActivityBundle.putString(Constants.GOAL_ID, currentGoal.getGoalUUID());
                addActivityDialog.setArguments(addActivityBundle);

                addActivityDialog.show(getFragmentManager(), "Add Activity");
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
                Intent addGoalScreen = new Intent(view.getContext(),  AddGoalPage.class);
                addGoalScreen.putExtra(Constants.ADD_GOAL_MODE, Constants.ADD);
                startActivityForResult(addGoalScreen, RESULT_ADD_GOAL);
            }
        });


        // Hamburger menu
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.nav_settings){
                    Log.d("Nav Menu", "Pressed settings ****************************");
                    drawer.closeDrawer(Gravity.LEFT);
                    Intent settingsIntent = new Intent(getApplicationContext(), SettingsPage.class);
                    startActivity(settingsIntent);

                } else if(item.getItemId() == R.id.nav_deleteall){
                    goalDAO.deleteAll();
                    updateHomePage();
                }
                return true;
            }
        });

        updateHomePage();

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("HomePage.OnResult", "requestCode: " + requestCode + ", resultCode: " + resultCode);
        if(resultCode == RESULT_ADD_GOAL){
            if(data.getStringExtra(Constants.ADD_GOAL_MODE).equals(Constants.DELETE)){
                Toast.makeText(this, "DELETE", Toast.LENGTH_SHORT);
            } else {

                String goalName = data.getStringExtra(Constants.GOAL_NAME);
                double goalValue = data.getDoubleExtra(Constants.GOAL_VALUE, 0);
                String goalUnits = data.getStringExtra(Constants.GOAL_UNITS);

                Log.d("Return from Add", "goalval: " + goalValue);

                Goal newGoal = new Goal(goalName, goalValue, goalUnits);

                // if the new goal has been set as current, switch current
                if (data.getBooleanExtra(Constants.GOAL_CURRENT, false)) {
                    Toast.makeText(this, "Current Goal has changed", Toast.LENGTH_SHORT);
                    // set new as current
                    newGoal.setGoalCompleted(currentGoal.getGoalCompleted());
                    newGoal.setDateOfGoal(today);
                    newGoal.setCurrentGoal(true);

                    // clear current
                    currentGoal.setCurrentGoal(false);
                }

                // if the goal is new, add it
                if (data.getStringExtra(Constants.ADD_GOAL_MODE).equals(Constants.ADD)) {

                    // if there is no current goal, make the new goal the current goal
                    if (currentGoal == null) {
                        newGoal.setCurrentGoal(true);
                        newGoal.setDateOfGoal(today);
                        currentGoal = newGoal;
                    }

                    goalDAO.saveOrUpdate(newGoal);
                } else if (data.getStringExtra(Constants.ADD_GOAL_MODE).equals(Constants.EDIT)) {
                    // Editing goal, just get its uuid and update it.
                    newGoal.setGoalUUID(data.getStringExtra(Constants.GOAL_ID));
                    goalDAO.saveOrUpdate(newGoal);
                }

                boolean newGoalIsCurrent = data.getBooleanExtra(Constants.GOAL_CURRENT, false);

                if (currentGoal == null) {
                    currentGoal = newGoal;
                    currentGoal.setCurrentGoal(true);
                    currentGoal.setDateOfGoal(today);
                    if (!goalDAO.saveOrUpdate(newGoal)) {
                        Toast.makeText(this, "Failed to add Goal", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(this, "Current Goal set", Toast.LENGTH_SHORT).show();
                } else if (newGoalIsCurrent) {
                    newGoal.setGoalCompleted(currentGoal.getGoalCompleted());
                    newGoal.setDateOfGoal(today);
                    newGoal.setCurrentGoal(true);
                    currentGoal.setCurrentGoal(false);

                    goalDAO.saveOrUpdate(newGoal);
                    goalDAO.saveOrUpdate(currentGoal);
                    currentGoal = newGoal;

                }
            }
            updateHomePage();
        } else if(resultCode == RESULT_SETTINGS){
            //();
        }
        //updateUI();
    }

    private void addActivityToGoal(){

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

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        *//*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else *//*if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/

    public void updateHomePage(){
        updateCurrentGoal();
        updateGoalList();
    }

    public void updateCurrentGoal(){

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
            progressBarText.setText("Add\nActivity");
            mCircleProgressView.setProgress(currentGoal.getPercentageCompleted());
        } else {
            progressBarText.setText("No\ngoal");
        }


        // Progress Text
        if(currentGoal != null){
            if(currentGoal.getGoalUnits().equals(Units.STEPS)){
                progressText.setText((int)currentGoal.getGoalCompleted() + "/" + (int)currentGoal.getGoalValue()
                        + " " + currentGoal.getGoalUnits());
            } else {
                progressText.setText(currentGoal.getGoalCompleted() + "/" + currentGoal.getGoalValue()
                        + " " + currentGoal.getGoalUnits());
            }
        } else {
            progressText.setText("0/0 Steps");
        }
    }

    public void updateGoalList(){
        // ListView HomePage
        goalList = goalDAO.findAllNotCurrentNotFinished();
        ListAdapter goalListAdapter = new GoalListAdapter(this, goalList);
        listOfGoals.setAdapter(goalListAdapter);
        //setListViewHeightBasedOnItems(listOfGoals);
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
