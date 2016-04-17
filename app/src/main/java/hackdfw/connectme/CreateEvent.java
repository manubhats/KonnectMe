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

    private static final String USERNAME = "User Name";
    private static final String USERNUMBER = "User Number";
    private static final String LOG_TAG = CreateEvent.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String userNumber = getIntent().getStringExtra("user number");
        String userName = getIntent().getStringExtra("user name");

        Log.d(LOG_TAG, userNumber);

        Log.d(LOG_TAG, "WORRK DAMMIT");
        SharedPreferences sharedpreferences = getSharedPreferences(USERNUMBER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        try {

            editor.putString(
                    "user number",
                    userNumber
            );

            editor.putString(
                    "user name",
                    userName
            );
        } catch(Exception e) {
            e.printStackTrace();
        }
        editor.commit();

        Log.d(LOG_TAG, "USER NUMBER: " + sharedpreferences.getString("user number", ""));
        Log.d(LOG_TAG, "USER NAME: " + sharedpreferences.getString("user name", ""));

//        SharedPreferences sp = getSharedPreferences(USERNAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor edit = sharedpreferences.edit();
//        try {
////            String userNumber = getIntent().getStringExtra("user number");
//            String userName = getIntent().getStringExtra("user name");
//            editor.putString(
//                    USERNAME,
//                    userName
//            );
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        editor.commit();


//        SharedPreferences sp1 = this.getSharedPreferences(USERNAME, Context.MODE_PRIVATE);
//        String getName, getNumber;
//
//        getNumber = sp.getString("user number", "").toString();
////        getName = sp.getString("user name", null).toString();
//
//        Log.d(LOG_TAG, "Number: " + getNumber
////        + "\nName: " + getName
//        );
//

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button createEvent = (Button)findViewById(R.id.createEvent);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CreateEvent.this, RegisterEvent.class);
                startActivity(i);
            }
        });
    }

}
