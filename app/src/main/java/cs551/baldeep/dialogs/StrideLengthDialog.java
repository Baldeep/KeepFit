package cs551.baldeep.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import cs551.baldeep.keepfit.R;
import cs551.baldeep.utils.Constants;

/**
 * Created by balde on 20/03/2017.
 */

public class StrideLengthDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialog_layout = inflater.inflate(R.layout.dialog_stride_len, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialog_layout);
        builder.setTitle("Set Stride Length");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText strideEditText = (EditText) getDialog().findViewById(R.id.txt_stride_len);

                String strideLenghtString = String.valueOf(strideEditText.getText());

                double activityToAdd = 0.0;

                if(!strideLenghtString.trim().isEmpty()){
                    activityToAdd = Double.valueOf(strideLenghtString);
                }

                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putFloat(Constants.STRIDE_LENGTH, (float) activityToAdd);
                editor.apply();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder.create();
    }
}
