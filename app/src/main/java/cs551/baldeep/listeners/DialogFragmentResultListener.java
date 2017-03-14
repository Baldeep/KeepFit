package cs551.baldeep.listeners;

import android.content.Intent;

/**
 * Created by balde on 14/03/2017.
 */

public interface DialogFragmentResultListener {

    public void onDialogResult(int requestCode, int resultCode, Intent data);
}
