package hackdfw.connectme;

import android.content.BroadcastReceiver;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView verifed;
    private Button sendSMS;
    private EditText userPhoneNumber;
    private BroadcastReceiver receiver;
    private Thread thread;

    private SMSReceiver smsReceiver;
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

        sendSMS.setOnClickListener(this);

        if(smsReceiver.verification) {

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        //Code edited by Manu
        Button gotoNext = (Button)findViewById(R.id.gotoNextActivity);
        gotoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(i);
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
    public void onClick(View v) {

        switch(v.getId()) {

            case R.id.btn_sendSMS:
                String number = userPhoneNumber.getText().toString();

                SmsManager sms = SmsManager.getDefault();
                sms.sendTextMessage(number, null, "Works", null, null);
                Log.d(LOG_TAG, "Button Pressed");
        }
    }


}
