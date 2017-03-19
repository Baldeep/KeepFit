package cs551.baldeep.keepfit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;

import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import cs551.baldeep.adapters.HistoryListAdapter;
import cs551.baldeep.adapters.GoalListAdapter;
import cs551.baldeep.utils.Constants;
import cs551.baldeep.dialogs.PastDatePickerDialog;
import cs551.baldeep.listeners.CircleProgressOnClickListener;
import cs551.baldeep.listeners.DrawerItemSelectedListener;
import cs551.baldeep.listeners.FABOnClickListener;
import cs551.baldeep.listeners.GoalListEditItemOnClickListener;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.listeners.TestModeDatePickerListener;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.AppUtils;
import cs551.baldeep.utils.GoalUtils;
import cs551.baldeep.utils.UIUtils;
import cs551.baldeep.utils.Units;

public class HomePage extends AppCompatActivity {

    private static final int RESULT_ADD_GOAL = 1;
    private static final int RESULT_SETTINGS = 2;
    private static final int RESULT_EDIT_GOAL = 3;
    private Goal currentGoal;
    private List<Goal> goalList;

    private SharedPreferences sharedPreferences;
    protected DrawerLayout drawer;

    private CircleProgressView mCircleProgressView;
    private FloatingActionButton fab;

    private ListView listOfGoals;
    private ListView listOfHistory;

    private GoalDAO goalDAO;
    private GoalUtils goalUtils;

    private Date today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        today = new Date(System.currentTimeMillis());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                checkTestMode();
            }
        });

        goalUtils = new GoalUtils(getApplicationContext());
        try {
            goalDAO = new GoalDAO(getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
            Thread.UncaughtExceptionHandler ueh = Thread.getDefaultUncaughtExceptionHandler();
            ueh.uncaughtException(Thread.currentThread(), e);
        }

        checkTestMode();

        goalList = goalDAO.findAll();
        currentGoal = goalDAO.findCurrentGoal();
        if(currentGoal != null) {
            currentGoal.setDateOfGoal(today);
        }

        setUpTabs();

        mCircleProgressView = (CircleProgressView) findViewById(R.id.circle_progress_view);
        mCircleProgressView.setInterpolator(new AccelerateDecelerateInterpolator());
        mCircleProgressView.setStartAngle(-90);


        // Goals List View
        listOfGoals = (ListView) findViewById(R.id.listview_goals);
        listOfGoals.setFocusable(false);
        GoalListEditItemOnClickListener listOfGoalsItemClickListener = new GoalListEditItemOnClickListener(this, listOfGoals);
        listOfGoals.setOnItemClickListener(listOfGoalsItemClickListener);
        listOfHistory = (ListView) findViewById(R.id.list_history);
        listOfHistory.setFocusable(false);

        // Clicking progressBar
        CircleProgressOnClickListener circleListener = new CircleProgressOnClickListener(getFragmentManager(), goalDAO);
        mCircleProgressView.setOnClickListener(circleListener);

        // ToolBar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Action Button
        fab = (FloatingActionButton) findViewById(R.id.fab_addgoal);
        FABOnClickListener fabOnClickListener = new FABOnClickListener(this, RESULT_ADD_GOAL);
        fab.setOnClickListener(fabOnClickListener);


        // Hamburger menu
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerItemSelectedListener navListener = new DrawerItemSelectedListener(this, goalDAO, drawer);
        navigationView.setNavigationItemSelectedListener(navListener);

        updateUI();

        AppUtils.setUpAlarm(getApplicationContext());
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

                Goal newGoal = new Goal(goalName, goalValue, goalUnits);

                // Use old ID if it's edited
                if (data.getStringExtra(Constants.ADD_GOAL_MODE).equals(Constants.EDIT)) {
                    newGoal.setGoalUUID(data.getStringExtra(Constants.GOAL_ID));
                }

                // if it's been set as current
                if(currentGoal == null || data.getBooleanExtra(Constants.GOAL_CURRENT, false)){
                    if(currentGoal == null) {
                        newGoal.setCurrentGoal(true);
                        newGoal.setDateOfGoal(today);
                    } else {
                        currentGoal.setCurrentGoal(false);

                        newGoal.setCurrentGoal(true);
                        double steps = Units.getUnitsInSteps(currentGoal.getGoalUnits(), currentGoal.getGoalCompleted());
                        newGoal.setGoalCompleted(Units.getStepsInUnits(newGoal.getGoalUnits(), steps));
                        newGoal.setDateOfGoal(today);
                    }
                    goalDAO.saveOrUpdate(currentGoal);
                    currentGoal = newGoal;
                }

                goalDAO.saveOrUpdate(newGoal);
            }
            updateHomePage();
        } else if(resultCode == RESULT_SETTINGS){

        } else if(resultCode == 3){
            Log.i("HOME *** ", "Got result from add - " + data.getStringExtra("A"));
        }
    }

    @Override
    protected void onPause() {
        // Don't save the current goal here, since a dialog might have changed it, without changing
        // the current goal in the HomePage.java
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.i("HOME ***", "RESUMING");
        currentGoal = goalDAO.findCurrentGoal();

        checkTestMode();
        updateUI();
        super.onResume();
    }

    private void setUpTabs(){
        // Tabs
        TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
        tabHost.setup();

        //Home tab
        TabHost.TabSpec specs = tabHost.newTabSpec(Constants.HOME);
        specs.setContent(R.id.tabhome);
        specs.setIndicator(Constants.HOME);
        tabHost.addTab(specs);

        // History tab
        specs = tabHost.newTabSpec(Constants.HISTORY);
        specs.setContent(R.id.tabhistory);
        specs.setIndicator(Constants.HISTORY);
        tabHost.addTab(specs);

        // Stats tab
        specs = tabHost.newTabSpec(Constants.STATISTICS);
        specs.setContent(R.id.tabstats);
        specs.setIndicator(Constants.STATISTICS);
        tabHost.addTab(specs);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(tabId.equals(Constants.HISTORY) || tabId.equals(Constants.STATISTICS)){
                    fab.hide();
                } else {
                    fab.show();
                }
            }
        });
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean testMode = sharedPreferences.getBoolean(Constants.TEST_MODE, false);

        if (testMode) {
            menu.setGroupVisible(R.id.overflow_menu_test_options, true);
        } else {
            menu.setGroupVisible(R.id.overflow_menu_test_options, false);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.menu_make_history) {
            /*Random r = new Random();

            for(int i = 0; i < 15; i++){
                Goal g = new Goal("Goal " + i, i * 1000, Units.STEPS);
                g.setGoalUUID(UUID.randomUUID().toString());
                g.setDateOfGoal(new Date(System.currentTimeMillis() - (86400002 * (i * 2))));
                g.setGoalCompleted(r.nextInt((i * 1000)+ 500));
                g.setDone(true);
                goalDAO.saveOrUpdate(g);
            }

            updateHistoryList();*/
        }

        if(id == R.id.menu_end_day){
            goalUtils.endOfDay();
            currentGoal = goalDAO.findCurrentGoal();
            updateUI();
        } else if(id == R.id.menu_set_date){
            PastDatePickerDialog datePicker = new PastDatePickerDialog();
            datePicker.setInitialDate(today);
            datePicker.setListener(new TestModeDatePickerListener(this, sharedPreferences));
            datePicker.show(getFragmentManager(), "Date Picker");
            checkTestMode();
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateHomePage(){
        updateCurrentGoal();
        updateGoalList();
    }

    public void updateCurrentGoal(){
        // Goal name heading
        TextView dailyGoalName = (TextView) findViewById(R.id.currentGoalName);
        TextView progressBarText = (TextView) findViewById(R.id.txt_progress_view);
        TextView progressText = (TextView) findViewById(R.id.txt_goalprogress);
        if(currentGoal != null){
            dailyGoalName.setText(currentGoal.getName());

            progressBarText.setText("Add\nActivity");
            mCircleProgressView.setProgress(currentGoal.getPercentageCompleted());

            progressText.setText(goalUtils.getFormattedProgressString(currentGoal));
        } else {
            dailyGoalName.setText(R.string.no_goal_message);

            progressBarText.setText("No\ngoal");

            progressText.setText("0/0 Steps");
        }
    }

    public void updateUI(){
        Log.i("Home Page", "Updating UI");
        updateHomePage();
        updateHistoryList();

        List<Goal> goals = goalDAO.findAll();
        for(Goal g : goals){
            String spacer = "";
            for(int i = g.getName().length(); i < 25; i++){
                spacer += " ";
            }
            Log.i("HOMEPAGE - 336: ", g.getName() + spacer + " - " + g.getGoalCompleted() + "/" + g.getGoalTarget() + " " + g.getGoalUnits() + ", current: " + g.isCurrentGoal() + ", done: " + g.isDone() + ", date: " + g.getDateOfGoal());
        }
    }

    public void updateGoalList(){
        goalList = goalDAO.findAllNotCurrentNotFinished();

        TextView listReplacementText = (TextView) findViewById(R.id.txt_no_goals_list_replacement);
        if(goalList.size()>0){
            listReplacementText.setVisibility(View.GONE);
        } else {
            listReplacementText.setVisibility(View.VISIBLE);
        }

        ListAdapter goalListAdapter = new GoalListAdapter(this, goalList);
        listOfGoals.setAdapter(goalListAdapter);
    }

    public void updateHistoryList(){
        // Goals List
        List<Goal> goalsFinished = goalDAO.findAllFinished();

        TextView listReplacementText = (TextView) findViewById(R.id.txt_no_history_list_replacement);
        if(goalsFinished.size()>0){
            listReplacementText.setVisibility(View.GONE);
        } else {
            listReplacementText.setVisibility(View.VISIBLE);
        }

        ListAdapter historyAdapter = new HistoryListAdapter(this,goalsFinished);
        listOfHistory.setAdapter(historyAdapter);
        UIUtils.setListViewHeightBasedOnItems(listOfHistory);
    }

    private void checkTestMode(){
        boolean testMode = sharedPreferences.getBoolean(Constants.TEST_MODE, false);

        TextView testModeText = (TextView) findViewById(R.id.txt_test_mode_active);
        TextView testModeHistory = (TextView) findViewById(R.id.txt_test_mode_active_history);

        // Refreshes the overflow menu
        invalidateOptionsMenu();

        if(testMode){
            today = AppUtils.getTestModeDate(sharedPreferences);
            Log.i("CHECKTESTMODE", today.toString());
            SimpleDateFormat df = new SimpleDateFormat(sharedPreferences.getString(Constants.DATE_FORMAT, "dd/MM/yy"));
            testModeText.setText("TEST MODE (" + df.format(today) + ")");
            testModeText.setVisibility(View.VISIBLE);
            testModeHistory.setText("TEST MODE (" + df.format(today) + ")");
            testModeHistory.setVisibility(View.VISIBLE);
        } else {
            Log.i("CHECKTESTMODE", today.toString());
            today = new Date(System.currentTimeMillis());
            testModeText.setVisibility(View.INVISIBLE);
            testModeHistory.setVisibility(View.INVISIBLE);
        }
        if(currentGoal != null) {
            currentGoal.setDateOfGoal(today);
            goalDAO.saveOrUpdate(currentGoal);
        }
    }

}
