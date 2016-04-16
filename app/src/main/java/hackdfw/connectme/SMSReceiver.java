package hackdfw.connectme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Arnav on 4/16/2016.
 */
public class SMSReceiver extends BroadcastReceiver {

    private static final String LOG_TAG = SMSReceiver.class.getSimpleName();
    static boolean verification = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("MainActivity", "SMS received.");
        verification = true;
    }
}
