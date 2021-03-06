package hackdfw.connectme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.telephony.gsm.SmsMessage;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements Runnable{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private TextView verifed;
    private Button sendSMS;
    private EditText userPhoneNumber;
    private boolean authStarted = false;
    private static boolean verification = false;
    private Thread thread = null;
    private EditText userName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sp = getSharedPreferences("User Number", Context.MODE_PRIVATE);

        try {
            String number = sp.getString("user number", "");

            if(number != "") {
                Intent intent = new Intent(MainActivity.this, CreateEvent.class);
                startActivity(intent);
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        sendSMS = (Button) findViewById(R.id.btn_sendSMS);
        userPhoneNumber = (EditText) findViewById(R.id.et_userPhone);
        verifed = (TextView) findViewById(R.id.tv_verified);
        userName = (EditText) findViewById(R.id.et_userName);
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


        Button next = (Button)findViewById(R.id.gotoNext);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent n = new Intent(MainActivity.this,CreateEvent.class);
                startActivity(n);
            }
        });
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
                    intent.putExtra("user number", userPhoneNumber.getText().toString());
                    intent.putExtra("user name", userName.getText().toString());
                    startActivity(intent);
                    authStarted = false;
                    break;
                }
                else if(counter == 10) {
                    verifed.setText("Not Verified");
                    Toast.makeText(MainActivity.this, "SORRY, VERIFICATION FAILED", Toast.LENGTH_LONG).show();
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

//            Log.d(LOG_TAG, "Message received.");
//            verification = true;

            Bundle extras = intent.getExtras();

            if (extras == null)
                return;

            Object[] pdus = (Object[]) extras.get("pdus");
            SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[0]);
            String origNumber = msg.getOriginatingAddress();
            String msgBody = msg.getMessageBody();

            if(msgBody.equals("Works")) {
                verification = true;
            }
        }
    }
}
