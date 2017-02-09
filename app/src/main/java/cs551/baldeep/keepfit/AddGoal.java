package cs551.baldeep.keepfit;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import cs551.baldeep.constants.BundleConstants;

/**
 * Created by balde on 09/02/2017.
 */

public class AddGoal extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);

        Button addButton = (Button) findViewById(R.id.btn_addgoal);

        Log.i("Add Goal", "Opened Add Goal!");

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
                goingBack.putExtra(BundleConstants.goalUnits, "Steps");
                setResult(RESULT_OK, goingBack);
                finish();
            }
        });

    }
}
