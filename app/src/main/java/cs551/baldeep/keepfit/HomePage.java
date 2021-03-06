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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.eralp.circleprogressview.CircleProgressView;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import cs551.baldeep.adapters.HistoryListAdapter;
import cs551.baldeep.adapters.GoalListAdapter;
import cs551.baldeep.listeners.FilterDatePickerListener;
import cs551.baldeep.listeners.HistoryFilterDateClearButtonListener;
import cs551.baldeep.listeners.HistoryFilterDatePickerListener;
import cs551.baldeep.listeners.HistoryFilterModeSpinnerListener;
import cs551.baldeep.listeners.HistoryFilterUnitsSpinnerListener;
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
    private List<Goal> historyList;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceListener;

    protected DrawerLayout drawer;
    private CircleProgressView mCircleProgressView;
    private FloatingActionButton fab;

    private ListView listOfGoals;
    private ListView listOfHistory;

    // Filtering UI elements
    protected String historyFilterDateMode;
    protected Date startFilterDate;
    protected Date endFilterDate;
    protected int historyFilterCompletedStart = 0;
    protected int historyFilterCompletedEnd = 100;

    private GoalDAO goalDAO;
    private GoalUtils goalUtils;

    private Date today;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        today = new Date(System.currentTimeMillis());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        // This listener stops showing changes made to the date after 3 changes...weird
        sharedPreferenceListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if(key.startsWith(Constants.TEST_MODE)) {
                    checkTestMode();
                } else if(key.startsWith("filter_stats")){
                    filterStats();
                } else if(key.startsWith("filter_")) {
                    filterHistory();
                }
                Log.i("HP - Prefs Listener", "Prefs updated: " + key);
            }
        };
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceListener);


        goalUtils = new GoalUtils(getApplicationContext());
        try {
            goalDAO = new GoalDAO(getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
            Thread.UncaughtExceptionHandler ueh = Thread.getDefaultUncaughtExceptionHandler();
            ueh.uncaughtException(Thread.currentThread(), e);
        }

        checkTestMode();

        historyList = goalDAO.findAllFinished();

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

        // HISTORY
        listOfHistory = (ListView) findViewById(R.id.list_history);
        listOfHistory.setFocusable(false);

        Spinner historyDateSpinner = (Spinner) findViewById(R.id.history_date_filter_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goalUtils.getHistoryFilterStrings());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        historyDateSpinner.setAdapter(adapter);
        historyDateSpinner.setSelection(adapter.getPosition(GoalUtils.HISTORY_ALL));
        historyDateSpinner.setOnItemSelectedListener(new HistoryFilterModeSpinnerListener(this, sharedPreferences, Constants.FILTER_HISTORY_MODE));

        Spinner historyUnitsSpinner = (Spinner) findViewById(R.id.history_units_filter_spinner);
        ArrayAdapter<String> unitsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Units.getFilterUnitStrings());
        unitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        historyUnitsSpinner.setAdapter(unitsAdapter);
        historyUnitsSpinner.setSelection(unitsAdapter.getPosition(Units.ORIGINAL));
        historyUnitsSpinner.setOnItemSelectedListener(new HistoryFilterUnitsSpinnerListener(this, sharedPreferences, Constants.FILTER_HISTORY_MODE));

        Button startDateBtn = (Button) findViewById(R.id.btn_history_date_start_filter);
        startDateBtn.setOnClickListener(new HistoryFilterDatePickerListener(getFragmentManager(), sharedPreferences, Constants.FILTER_HISTORY_MODE, Constants.START, today));

        ImageButton startDateClearBtn = (ImageButton) findViewById(R.id.btn_history_date_start_clear);
        startDateClearBtn.setOnClickListener(new HistoryFilterDateClearButtonListener(sharedPreferences, Constants.FILTER_HISTORY_MODE, Constants.START));

        Button endDateBtn = (Button) findViewById(R.id.btn_history_date_end_filter);
        endDateBtn.setOnClickListener(new HistoryFilterDatePickerListener(getFragmentManager(), sharedPreferences, Constants.FILTER_HISTORY_MODE, Constants.END, today));

        ImageButton endDateClearBtn = (ImageButton) findViewById(R.id.btn_history_date_end_clear);
        endDateClearBtn.setOnClickListener(new HistoryFilterDateClearButtonListener(sharedPreferences, Constants.FILTER_HISTORY_MODE, Constants.END));

        // STATISTICS
        Spinner statsDateSpinner = (Spinner) findViewById(R.id.stats_date_filter_spinner);
        ArrayAdapter<String> statsSpinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, goalUtils.getHistoryFilterStrings());
        statsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statsDateSpinner.setAdapter(statsSpinnerAdapter);
        statsDateSpinner.setSelection(statsSpinnerAdapter.getPosition(GoalUtils.HISTORY_ALL));
        statsDateSpinner.setOnItemSelectedListener(new HistoryFilterModeSpinnerListener(this, sharedPreferences, Constants.FILTER_STATS_MODE));

        Spinner statsUnitsSpinner = (Spinner) findViewById(R.id.stats_units_filter_spinner);
        ArrayAdapter<String> statsUnitsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Units.getFilterUnitStrings());
        statsUnitsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statsUnitsSpinner.setAdapter(statsUnitsAdapter);
        statsUnitsSpinner.setSelection(statsUnitsAdapter.getPosition(Units.ORIGINAL));
        statsUnitsSpinner.setOnItemSelectedListener(new HistoryFilterUnitsSpinnerListener(this, sharedPreferences, Constants.FILTER_STATS_MODE));

        Button startStatsDateBtn = (Button) findViewById(R.id.btn_stats_date_start_filter);
        startStatsDateBtn.setOnClickListener(new HistoryFilterDatePickerListener(getFragmentManager(), sharedPreferences, Constants.FILTER_STATS_MODE, Constants.START, today));

        ImageButton startStatsDateClearBtn = (ImageButton) findViewById(R.id.btn_stats_date_start_clear);
        startStatsDateClearBtn.setOnClickListener(new HistoryFilterDateClearButtonListener(sharedPreferences, Constants.FILTER_STATS_MODE, Constants.START));

        Button endStatsDateBtn = (Button) findViewById(R.id.btn_stats_date_end_filter);
        endStatsDateBtn.setOnClickListener(new HistoryFilterDatePickerListener(getFragmentManager(), sharedPreferences, Constants.FILTER_STATS_MODE, Constants.END, today));

        ImageButton endStatsDateClearBtn = (ImageButton) findViewById(R.id.btn_stats_date_end_clear);
        endStatsDateClearBtn.setOnClickListener(new HistoryFilterDateClearButtonListener(sharedPreferences, Constants.FILTER_STATS_MODE, Constants.END));

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

        Units units = new Units(getApplicationContext());

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
                        double steps = units.getUnitsInSteps(currentGoal.getGoalUnits(), currentGoal.getGoalCompleted());
                        newGoal.setGoalCompleted(units.getStepsInUnits(newGoal.getGoalUnits(), steps));
                        newGoal.setDateOfGoal(today);
                    }
                    goalDAO.saveOrUpdate(currentGoal);
                    currentGoal = newGoal;
                }

                goalDAO.saveOrUpdate(newGoal);
            }
            updateHomePage();
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
        Log.i("HP - Resuming", "Resuming");
        currentGoal = goalDAO.findCurrentGoal();

        checkTestMode();
        updateUI();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        AppUtils.resetHistoryFilterPreference(sharedPreferences);
        super.onDestroy();
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

        MenuItem testModeTxt = menu.findItem(R.id.menu_test_mode_txt);

        if (testMode) {
            testModeTxt.setVisible(true);
            SimpleDateFormat df = new SimpleDateFormat(sharedPreferences.getString(Constants.DATE_FORMAT, "dd/MM/yy"));
            testModeTxt.setTitle("TEST MODE (" + df.format(today) + ")");
            menu.setGroupVisible(R.id.overflow_menu_test_options, true);
        } else {
            menu.setGroupVisible(R.id.overflow_menu_test_options, false);
            testModeTxt.setVisible(false);
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
            datePicker.setListener(new TestModeDatePickerListener(sharedPreferences));
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
        //updateHistoryList();
        filterHistory();
        filterStats();

        List<Goal> goals = goalDAO.findAll();
        for(Goal g : goals){
            String spacer = "";
            for(int i = g.getName().length(); i < 25; i++){
                spacer += " ";
            }
            Log.i("HOMEPAGE - 336: ", g.getName() + spacer + " - " + g.getGoalCompleted() + "/" +
                    g.getGoalTarget() + " " + g.getGoalUnits() + " (" + g.getPercentageCompleted() + "), current: " + g.isCurrentGoal() +
                    ", done: " + g.isDone() + ", date: " + g.getDateOfGoal());
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
        TextView listReplacementText = (TextView) findViewById(R.id.txt_no_history_list_replacement);
        if(historyList.size()>0){
            listReplacementText.setVisibility(View.GONE);
        } else {
            listReplacementText.setVisibility(View.VISIBLE);
        }

        ListAdapter historyAdapter = new HistoryListAdapter(this,historyList, Units.KM);
        listOfHistory.setAdapter(historyAdapter);
        UIUtils.setListViewHeightBasedOnItems(listOfHistory);
    }

    private void checkTestMode(){
        boolean testMode = sharedPreferences.getBoolean(Constants.TEST_MODE, false);

        // Refreshes the overflow menu
        invalidateOptionsMenu();

        if(testMode){
            today = AppUtils.getSharedPreferecnceDate(sharedPreferences, Constants.TEST_MODE + "_");
        } else {
            today = new Date(System.currentTimeMillis());
        }


        if(currentGoal != null) {
            currentGoal.setDateOfGoal(today);
            goalDAO.saveOrUpdate(currentGoal);
        }
    }

    protected void filterHistory(){
        String historyFilterDateMode = sharedPreferences.getString(Constants.FILTER_HISTORY_MODE, GoalUtils.HISTORY_ALL);

        if(historyFilterDateMode == null){
            historyFilterDateMode = GoalUtils.HISTORY_ALL;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        Date startFilterDate = AppUtils.getSharedPreferecnceDate(sharedPreferences, "filter_" + Constants.START + "_");

        Button startDateBtn = (Button) findViewById(R.id.btn_history_date_start_filter);
        if(AppUtils.dateIsOutOfBounds(startFilterDate)){
            startDateBtn.setText("None Set");
        } else {
            startDateBtn.setText(sdf.format(startFilterDate));
        }

        Date endFilterDate = AppUtils.getSharedPreferecnceDate(sharedPreferences, "filter_" + Constants.END + "_");

        Button endDateBtn = (Button) findViewById(R.id.btn_history_date_end_filter);
        if(AppUtils.dateIsOutOfBounds(endFilterDate)){
            endDateBtn.setText("None Set");
        } else {
            endDateBtn.setText(sdf.format(endFilterDate));
        }

        historyList = goalUtils.filterHistory(historyFilterDateMode,
                startFilterDate,  endFilterDate,
                historyFilterCompletedStart, historyFilterCompletedEnd);

        Log.i("HP - filterHistory", "Found in history: " + historyList.size());

        updateHistoryList();
    }

    public void filterStats(){

        TextView genericText = (TextView) findViewById(R.id.txt_stats_generic);

        String filterMode = sharedPreferences.getString(Constants.FILTER_STATS_MODE, "NONE");
        String filterUnits = sharedPreferences.getString(Constants.FILTER_STATS_UNITS, "NONE");
        Date startDate = AppUtils.getSharedPreferecnceDate(sharedPreferences, "filter_stats_" + Constants.START + "_");
        Date endDate = AppUtils.getSharedPreferecnceDate(sharedPreferences, "filter_stats_" + Constants.END + "_");

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String startDateString = sdf1.format(startDate);
        String endDateString = sdf1.format(endDate);

        Button startDateBtn = (Button) findViewById(R.id.btn_history_date_start_filter);
        if(AppUtils.dateIsOutOfBounds(startDate)){
            startDateBtn.setText("None Set");
        } else {
            startDateBtn.setText(sdf1.format(startDate));
        }

        Button endDateBtn = (Button) findViewById(R.id.btn_history_date_end_filter);
        if(AppUtils.dateIsOutOfBounds(endDate)){
            endDateBtn.setText("None Set");
        } else {
            endDateBtn.setText(sdf1.format(endDate));
        }

        genericText.setText("Mode: " + filterMode + "\n" +
                            "Units: " + filterUnits + "\n" +
                            "StartDate: " + startDateString + "\n" +
                            "EndDateString: " + endDateString + "\n" );

        Goal mostActive = goalUtils.getMaxActivity(filterMode, startDate, endDate, 0, 100);
        updateMostActive(mostActive, filterUnits);

        Goal leastActive = goalUtils.getMinActivity(filterMode, startDate, endDate, 0, 100);
        updateLeastActive(leastActive, filterUnits);
        Log.i("Home Page", "Filtered Stats");
    }

    private void updateMostActive(Goal mostActive, String filterUnits){
        if(mostActive != null) {
            TextView mostActiveName = (TextView) findViewById(R.id.txt_most_active_name);
            mostActiveName.setText(mostActive.getName());

            TextView mostActiveDate = (TextView) findViewById(R.id.txt_most_active_date);
            String dateFormat = sharedPreferences.getString(Constants.DATE_FORMAT, "dd/MM/yy");
            SimpleDateFormat sdf;
            if (dateFormat.startsWith("MM")) {
                sdf = new SimpleDateFormat("MMM-dd-yy (EEE)");
            } else {
                sdf = new SimpleDateFormat("dd-MMM-yy (EEE)");
            }

            mostActiveDate.setText(sdf.format(mostActive.getDateOfGoal()));

            TextView mostActiveProgress = (TextView) findViewById(R.id.txt_most_active_progress);
            if (filterUnits.equals(Units.ORIGINAL)) {
                mostActiveProgress.setText(goalUtils.getFormattedProgressString(mostActive));
            } else {
                mostActiveProgress.setText(goalUtils.getFormattedProgressStringInUnits(mostActive, filterUnits));
            }

            if (mostActive.getPercentageCompleted() >= 100) {
                ImageView imageView = (ImageView) findViewById(R.id.image_most_active_icon);
                imageView.setImageResource(R.drawable.btn_star_big_on_pressed);
            }
            ProgressBar goalProgress = (ProgressBar) findViewById(R.id.progressBar_most_active);
            goalProgress.setProgress((int) mostActive.getPercentageCompleted());
        } else {
            LinearLayout layout = (LinearLayout) findViewById(R.id.max_goal_layout);
            layout.setVisibility(View.GONE);
        }
    }

    private void updateLeastActive(Goal leastActive, String filterUnits){
        if(leastActive != null) {
            TextView leastActiveName = (TextView) findViewById(R.id.txt_least_active_name);
            leastActiveName.setText(leastActive.getName());

            TextView leastActiveDate = (TextView) findViewById(R.id.txt_least_active_date);
            String dateFormat = sharedPreferences.getString(Constants.DATE_FORMAT, "dd/MM/yy");
            SimpleDateFormat sdf;
            if (dateFormat.startsWith("MM")) {
                sdf = new SimpleDateFormat("MMM-dd-yy (EEE)");
            } else {
                sdf = new SimpleDateFormat("dd-MMM-yy (EEE)");
            }
            leastActiveDate.setText(sdf.format(leastActive.getDateOfGoal()));

            TextView leastActiveProgress = (TextView) findViewById(R.id.txt_least_active_progress);
            if (filterUnits.equals(Units.ORIGINAL)) {
                leastActiveProgress.setText(goalUtils.getFormattedProgressString(leastActive));
            } else {
                leastActiveProgress.setText(goalUtils.getFormattedProgressStringInUnits(leastActive, filterUnits));
            }

            if (leastActive.getPercentageCompleted() >= 100) {
                ImageView imageView = (ImageView) findViewById(R.id.image_least_active_icon);
                imageView.setImageResource(R.drawable.btn_star_big_on_pressed);
            }
            ProgressBar goalProgress = (ProgressBar) findViewById(R.id.progressBar_least_active);
            goalProgress.setProgress((int) leastActive.getPercentageCompleted());
        }else {
            LinearLayout layout = (LinearLayout) findViewById(R.id.max_goal_layout);
            layout.setVisibility(View.GONE);
        }
    }

}
