package hackdfw.connectme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class CreateEvent extends AppCompatActivity{

    private static final String USERNUMBER = "User Number";
    private static final String LOG_TAG = CreateEvent.class.getSimpleName();
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listView = (ListView) findViewById(R.id.lvList);

        SQLEventDatabase eventDatabase = new SQLEventDatabase(this);
        eventDatabase.open();
        ArrayList<Event> eventList = eventDatabase.getData();
        eventDatabase.close();
        ArrayAdapter<Event> arrayAdapter = new ArrayAdapter<Event>(
                CreateEvent.this, R.layout.list_item, eventList);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(CreateEvent.this, ShowUsers.class);
                intent.putExtra("Position", position);
                startActivity(intent);

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(USERNUMBER, Context.MODE_PRIVATE);
        String number = sharedPreferences.getString("user number", "");

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
        } catch (Exception e) {
            e.printStackTrace();
        }


        String usergivennumber = sharedPreferences.getString("user number", "");

        Log.d(LOG_TAG, "WORRK DAMMIT");

        Log.d(LOG_TAG, "USER NUMBER: " + sharedPreferences.getString("user number", ""));
        Log.d(LOG_TAG, "USER NAME: " + sharedPreferences.getString("user name", ""));


        Button createEvent = (Button) findViewById(R.id.createEvent);
        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(CreateEvent.this, RegisterEvent.class);
                startActivity(i);
            }
        });
//
//        RevieveData rr = new RevieveData();
//        rr.execute(usergivennumber);
    }



}
