package hackdfw.connectme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable{

    private static final String LOG_TAG = "Authentication";

    private TextView verifed;
    private Button sendSMS;
    private EditText userPhoneNumber;
    private BroadcastReceiver receiver;
    private Thread thread;
    private BroadcastReceiver broadcastReceiver;
    private static Switch switchVerfication;
    private SMSReceiver smsReceiver;
    private static boolean verification = false;
    private boolean authStarted = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        smsReceiver = new SMSReceiver();


        sendSMS = (Button) findViewById(R.id.btn_sendSMS);
        userPhoneNumber = (EditText) findViewById(R.id.et_userPhone);
        verifed = (TextView) findViewById(R.id.tv_verified);
        switchVerfication = (Switch) findViewById(R.id.verfication);
        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = userPhoneNumber.getText().toString();
                String msg = "Works";
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(number, null, msg, null, null);
                authStarted = true;
                Log.d(LOG_TAG, "AuthStarted received." + authStarted);

                thread = new Thread(MainActivity.this);
                thread.start();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void run() {
        int counter = 0;
        Log.d(LOG_TAG, "Running.");
        while (authStarted) {
            Log.d(LOG_TAG, "Running.");
            Log.d(LOG_TAG, "Started");
            try {
                Thread.sleep(1000);
                counter++;
                if (verification == true) {
                    Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                    startActivity(intent);
                    authStarted = false;
                    break;
                } else if (counter == 10) {
                    verifed.setText("Verfication Failed.");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
        thread.interrupt();

    }

    public static class SMSReceiver extends BroadcastReceiver {

        public SMSReceiver() {

            //0 Argument Constructor.
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            verification = true;
            Log.d(LOG_TAG, "SMS received." + verification);
        }
    }


}
