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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;


public class RegisterEvent extends AppCompatActivity implements View.OnClickListener{

   // public final int PICK_CONTACT = 2015;
    private static final String TAG = RegisterEvent.class.getSimpleName();
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;     // contacts unique ID
    int eventID;
    public static final int REQUEST_CODE_PICK_CONTACT = 1;
    public static final int MAX_PICK_CONTACT = 10;

    private String contactNumber = null;
    private String contactName = null;

    private SQLDatabase sqlDatabase;
    private SQLEventDatabase sqlEventDatabase;

    private Button add, invite;
    private EditText etName, etEmail, etNumber;


    public final int PICK_CONTACT = 2015;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = (EditText) findViewById(R.id.contName);
        etNumber = (EditText) findViewById(R.id.contNumb);
        etEmail = (EditText) findViewById(R.id.emailId);

        sqlDatabase = new SQLDatabase(this);
        sqlEventDatabase = new SQLEventDatabase(this);

        sqlDatabase.open();
        sqlDatabase.deleteAll();
        sqlDatabase.close();

        Random rand = new Random();
        eventID = rand.nextInt(10000);

        add = (Button) findViewById(R.id.addSelected);
        invite = (Button) findViewById(R.id.invitePeople);

        add.setOnClickListener(this);
        invite.setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fetchContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();

            retrieveContactName();
            retrieveContactNumber();

            if(contactName != null && contactNumber != null) {

                sqlDatabase.open();
                sqlDatabase.createEntry(contactName, contactNumber, null, String.valueOf(eventID));
                sqlDatabase.close();
            }
        }
    }



    private void retrieveContactNumber() {

        EditText contNumb = (EditText)findViewById(R.id.contNumb);

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();
        contNumb.setText(contactNumber);
        Log.d(TAG, "Contact Phone Number: " + contactNumber);
    }

    private void retrieveContactName() {

        EditText contName = (EditText)findViewById(R.id.contName);
        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {

            // DISPLAY_NAME = The display name for the contact.
            // HAS_PHONE_NUMBER =   An indicator of whether this contact has at least one phone number.

            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();
        contName.setText(contactName);
        Log.d(TAG, "Contact Name: " + contactName);

    }


    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.addSelected:
                if(contactName != null && contactNumber != null) {

                    sqlDatabase.open();
                    sqlDatabase.createEntry(contactName, contactNumber, null, String.valueOf(eventID));
                    sqlDatabase.close();

                    if(!etName.getText().toString().equals("")) {
                        etName.setText("");
                    }

                    if(!etEmail.getText().toString().equals("")) {
                        etEmail.setText("");
                    }

                    if(!etNumber.getText().toString().equals("")) {
                        etNumber.setText("");
                    }
                    Toast.makeText(this, "Added!", Toast.LENGTH_LONG).show();

                }
                break;
            case R.id.invitePeople:

//                sqlDatabase.open();
//                Log.d("RegisterEvent", sqlDatabase.getData(String.valueOf(eventID)));
//                sqlDatabase.close();
                sqlEventDatabase.open();
//                sqlEventDatabase.createEntry(eventName, eventID);
                break;
        }
    }
}