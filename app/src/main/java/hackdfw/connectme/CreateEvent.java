package hackdfw.connectme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CreateEvent extends AppCompatActivity {

    private static final String USERNUMBER = "User Number";
    private static final String LOG_TAG = CreateEvent.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences(USERNUMBER, Context.MODE_PRIVATE);
        ;

        try {
            String userNumber = getIntent().getStringExtra("user number");
            String userName = getIntent().getStringExtra("user name");

            if (!userName.equals("") && !userNumber.equals("")) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString(
                        "user number",
                        userNumber
                );

                editor.putString(
                        "user name",
                        userName
                );
                editor.commit();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

//        Log.d(LOG_TAG, userNumber);

            Log.d(LOG_TAG, "WORRK DAMMIT");

            Log.d(LOG_TAG, "USER NUMBER: " + sharedPreferences.getString("user number", ""));
            Log.d(LOG_TAG, "USER NAME: " + sharedPreferences.getString("user name", ""));


            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });

            Button createEvent = (Button) findViewById(R.id.createEvent);
            createEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(CreateEvent.this, RegisterEvent.class);
                    startActivity(i);
                }
            });
        }

    }
