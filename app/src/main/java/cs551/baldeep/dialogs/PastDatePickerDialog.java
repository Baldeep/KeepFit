package cs551.baldeep.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Date;
import java.util.Calendar;

import cs551.baldeep.keepfit.R;

/**
 * Created by balde on 19/03/2017.
 */

public class PastDatePickerDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private DatePickerDialog.OnDateSetListener listener;
    private Date initialDate;
    private Date minDate;
    private Date maxDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();

        if (initialDate == null) {
            initialDate = new Date(System.currentTimeMillis());
        }

        c.setTime(initialDate);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(getActivity(), listener, year, month, day);

        DatePicker dp = dpd.getDatePicker();

        if(minDate != null){
            dp.setMinDate(minDate.getTime());
        }

        if (maxDate != null) {
            dp.setMaxDate(maxDate.getTime());
        } else {
            dp.setMaxDate(System.currentTimeMillis() + 84000);
        }

        return dpd;
    }

    public void setInitialDate(Date date){
        this.initialDate = date;
    }

    public void setListener(DatePickerDialog.OnDateSetListener listener){
        this.listener = listener;
    }

    public void setMinDate(Date date){
        this.minDate = date;
    }

    public void setMaxDate(Date date){
        this.maxDate = date;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if(listener != null) {
            this.listener.onDateSet(view, year, month, dayOfMonth);
        } else {
            Toast.makeText(getActivity(), "Date picked: " + dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT);
        }
    }
}