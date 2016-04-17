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
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Runnable{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView verifed;
    private Button sendSMS;
    private EditText userPhoneNumber;
    private boolean authStarted = false;
    private static boolean verification = false;
    private Thread thread = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sendSMS = (Button) findViewById(R.id.btn_sendSMS);
        userPhoneNumber = (EditText) findViewById(R.id.et_userPhone);
        verifed = (TextView) findViewById(R.id.tv_verified);
        thread = new Thread(MainActivity.this);

        sendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String number = userPhoneNumber.getText().toString();
                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(number, null, "Works", null, null);
                authStarted = true;
                if(!thread.isAlive()) thread.start();
                Log.d(LOG_TAG, "Button Pressed");
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

        Log.d(LOG_TAG, "Started!");
        int counter = 1;
        while(authStarted) {

            counter += 1;
            try {
                Thread.sleep(1000);
                if(verification) {
                    Thread.sleep(1500);
                    Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                    startActivity(intent);
                    authStarted = false;
                    break;
                }
                else if(counter == 5) {
                    verifed.setText("Not Verified");
                    authStarted = false;
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
        authStarted = false;
    }

    public static class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d(LOG_TAG, "Message received.");
            verification = true;
        }
    }
}
