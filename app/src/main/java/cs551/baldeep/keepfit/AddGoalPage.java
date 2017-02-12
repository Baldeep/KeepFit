package cs551.baldeep.keepfit;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import cs551.baldeep.constants.BundleConstants;
import cs551.baldeep.utils.Units;

/**
 * Created by balde on 09/02/2017.
 */

public class AddGoalPage extends AppCompatActivity {

    protected Spinner spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        spinner = (Spinner) findViewById(R.id.spinner_goalunits);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, Units.getUnitStrings());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Button addButton = (Button) findViewById(R.id.btn_addgoal);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText goalNameTxt = (EditText) findViewById(R.id.txt_goalname);
                String goalName = String.valueOf(goalNameTxt.getText());

                EditText goalValueTxt = (EditText) findViewById(R.id.txt_goalmax);
                int goalValue = Integer.valueOf(String.valueOf(goalValueTxt.getText()));

                CheckBox setAsCurrentCheck = (CheckBox) findViewById(R.id.checkbox_setcurrent);
                boolean setAsCurrent = setAsCurrentCheck.isChecked();

                Intent goingBack = new Intent();
                goingBack.putExtra(BundleConstants.goalName, goalName);
                goingBack.putExtra(BundleConstants.goalValue, goalValue);
                goingBack.putExtra(BundleConstants.goalIsCurrent, setAsCurrent);
                goingBack.putExtra(BundleConstants.goalUnits, spinner.getSelectedItem().toString());
                setResult(1, goingBack);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent goingBack = new Intent();
        setResult(0, goingBack);
        finish();
    }
}
