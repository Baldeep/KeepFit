package cs551.baldeep.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import cs551.baldeep.utils.AppUtils;

public class DeviceBootReciever extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            AppUtils.setUpAlarm(context);
            Toast.makeText(context, "Set Up KeepFit Alarm", Toast.LENGTH_LONG).show();
        }
    }
}