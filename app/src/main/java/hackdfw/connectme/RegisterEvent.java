package hackdfw.connectme;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Random;


public class RegisterEvent extends AppCompatActivity {

    private static final String LOG_TAG = RegisterEvent.class.getSimpleName();

    public static final int REQUEST_CODE_PICK_CONTACT = 1;
    public static final int MAX_PICK_CONTACT = 10;

    public final int PICK_CONTACT = 2015;
    private int eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Random rand = new Random();
        eventID = rand.nextInt(10000);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fetchContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(i, PICK_CONTACT);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
            EditText contNumb = (EditText) findViewById(R.id.contNumb);
            EditText contName = (EditText) findViewById(R.id.contName);
            String contactName = null;
            Uri contactUri = data.getData();
            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
            while (cursor.moveToNext()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
            }
            cursor.moveToFirst();
            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            Log.d("phone number", cursor.getString(column));
            contName.setText(contactName);
            contNumb.setText(cursor.getString(column));
        }

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CODE_PICK_CONTACT) {

                Bundle bundle = data.getExtras();

                String result = bundle.getString("result");
                ArrayList<String> contacts = bundle.getStringArrayList("result");

                Log.d(LOG_TAG, "launchMultiplePhonePicker bundle.toString()= " + contacts.get(0).toString());

            }

            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}