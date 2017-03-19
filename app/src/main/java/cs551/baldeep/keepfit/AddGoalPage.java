package cs551.baldeep.keepfit;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.SQLException;

import cs551.baldeep.utils.Constants;
import cs551.baldeep.dao.GoalDAO;
import cs551.baldeep.dialogs.DeleteGoalDialog;
import cs551.baldeep.models.Goal;
import cs551.baldeep.utils.Units;

/**
 * Created by balde on 09/02/2017.
 */

public class AddGoalPage extends AppCompatActivity {

    protected Spinner spinner;
    private Button clearButton;

    private GoalDAO goalDAO;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            goalDAO = new GoalDAO(getApplicationContext());
        } catch (SQLException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to initialise db.", Toast.LENGTH_SHORT);
            /*Thread.UncaughtExceptionHandler ueh = Thread.getDefaultUncaughtExceptionHandler();
            ueh.uncaughtException(Thread.currentThread(), e);*/
        }

        final EditText goalNameTxt = (EditText) findViewById(R.id.txt_goalname);
        EditText goalValueTxt = (EditText) findViewById(R.id.txt_goalmax);
        CheckBox setAsCurrentCheck = (CheckBox) findViewById(R.id.checkbox_setcurrent);

        spinner = (Spinner) findViewById(R.id.spinner_goalunits);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, Units.getUnitStrings());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        clearButton = (Button) findViewById(R.id.btn_clear_goal_name);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalNameTxt.setText("");
            }
        });

        Button addButton = (Button) findViewById(R.id.btn_addgoal);


        if(getIntent().getStringExtra(Constants.ADD_GOAL_MODE).equals(Constants.EDIT)){
            Goal goal = goalDAO.findById(getIntent().getStringExtra(Constants.GOAL_ID));
            goalNameTxt.setText(goal.getName());
            goalValueTxt.setText(goal.getGoalTarget()+"");
            if(goal.isCurrentGoal()){
                setAsCurrentCheck.setChecked(true);
            }
            spinner.setSelection(adapter.getPosition(goal.getGoalUnits()));
            addButton.setText("Save");
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText goalNameTxt = (EditText) findViewById(R.id.txt_goalname);
                String goalName = String.valueOf(goalNameTxt.getText());

                if(goalName.trim().isEmpty()){
                    goalName="My Goal";
                }

                EditText goalValueTxt = (EditText) findViewById(R.id.txt_goalmax);

                String goalUnits = spinner.getSelectedItem().toString();
                String goalValueString = String.valueOf(goalValueTxt.getText());

                double goalValue = 0;
                if(goalValueString.trim().isEmpty()){
                    if(goalUnits.equals(Units.STEPS)){
                        goalValue = 10000;
                    } else {
                        goalValue = Units.getStepsInUnits(goalUnits, 10000);
                    }
                } else {
                    goalValue = Double.valueOf(goalValueString);
                }

                Log.i("ADDING GOAL", "goalVal: " + goalValue);

                CheckBox setAsCurrentCheck = (CheckBox) findViewById(R.id.checkbox_setcurrent);
                boolean setAsCurrent = setAsCurrentCheck.isChecked();

                Intent goingBack = new Intent();
                goingBack.putExtra(Constants.ADD_GOAL_MODE, getIntent().getStringExtra(Constants.ADD_GOAL_MODE));
                goingBack.putExtra(Constants.GOAL_NAME, goalName);
                goingBack.putExtra(Constants.GOAL_VALUE, goalValue);
                goingBack.putExtra(Constants.GOAL_CURRENT, setAsCurrent);
                goingBack.putExtra(Constants.GOAL_UNITS, goalUnits);
                if(getIntent().getStringExtra(Constants.ADD_GOAL_MODE).equals(Constants.EDIT)){
                    goingBack.putExtra(Constants.GOAL_ID, getIntent().getStringExtra(Constants.GOAL_ID));
                }
                setResult(1, goingBack);
                finish();
            }
        });

        Button deleteButton = (Button) findViewById(R.id.btn_delete_goal);

        if(getIntent().getStringExtra(Constants.ADD_GOAL_MODE).equals(Constants.EDIT)){
            deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getApplicationContext(), "Clicked delete", Toast.LENGTH_SHORT);

                    DialogFragment confirm = new DeleteGoalDialog();
                    Bundle confirmBundle = new Bundle();
                    confirmBundle.putString(Constants.GOAL_ID, getIntent().getStringExtra(Constants.GOAL_ID));
                    confirm.setArguments(confirmBundle);
                    confirm.show(getFragmentManager(), "Confirm Delete");

                }
            });
        }

    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("AddGoalPage - 135: ", "Returned to AddGoalPage");
        if(resultCode == 1){
            Toast.makeText(this, "Confirmed", Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent goingBack = new Intent();
        goingBack.putExtra(Constants.ADD_GOAL_MODE, getIntent().getStringExtra(Constants.ADD_GOAL_MODE));
        setResult(1, goingBack);
        finish();

    }
}
