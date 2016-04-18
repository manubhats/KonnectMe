package hackdfw.connectme;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

;

public class RegisterEvent extends AppCompatActivity implements View.OnClickListener {
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
    private EditText etName, etEmail, etNumber, etEventName, etEventDesc;


    public final int PICK_CONTACT = 2015;
    private HttpPost httppost;
    private HttpResponse response;
    private InputStream is;
    // private HttpWrapper httpWrapper;
    private User user;
    private ContactID cont;
    private String initiator_name;
    private String initiator_phone_number;
    private String event_id;
    private String event_name;
    private String event_message;

    private String USER_name;
    private String USER_number;

    private String cid, name, numb;
    private ArrayList<ContactID> contactss = new ArrayList<ContactID>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        etName = (EditText) findViewById(R.id.contName);
        etNumber = (EditText) findViewById(R.id.contNumb);
        etEmail = (EditText) findViewById(R.id.emailId);
        etEventName = (EditText) findViewById(R.id.eventName);
        etEventDesc = (EditText) findViewById(R.id.descEvent);


        SharedPreferences sharedPreferences = getSharedPreferences("User Number", Context.MODE_PRIVATE);

        USER_name =sharedPreferences.getString("user name", "");
        USER_number = sharedPreferences.getString("user number", "");

        sqlDatabase = new SQLDatabase(this);
        sqlEventDatabase = new SQLEventDatabase(this);


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
        }
    }


    private void retrieveContactNumber() {

        String contactNumber = null;
        EditText contNumb = (EditText) findViewById(R.id.contNumb);

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

        String contactName = null;
        EditText contName = (EditText) findViewById(R.id.contName);
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

        switch (v.getId()) {

            case R.id.addSelected:

                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String number = etNumber.getText().toString();
                String eventName = etEventName.getText().toString();
                String eventDesc = etEventDesc.getText().toString();
                sqlDatabase.open();
                sqlDatabase.createEntry(name, email, number, String.valueOf(eventID), eventName, eventDesc, "Unknown");
                sqlDatabase.close();

                etName.setText("");
                etEmail.setText("");
                etNumber.setText("");

                break;

            case R.id.invitePeople:


                String fullContacts = "";

                sqlDatabase.open();
                ArrayList<UserInformation> userInformation = sqlDatabase.getData(String.valueOf(eventID));
                sqlDatabase.close();
                eventName = etEventName.getText().toString();

                sqlEventDatabase.open();
                sqlEventDatabase.createEntry(eventName, String.valueOf(eventID));
                sqlEventDatabase.close();


                for(UserInformation al : userInformation) {

                    fullContacts += "{\"username\": \"" + USER_name + "\", \"user_number\": \"" + USER_number + "\", " + al.toString2() + "\n";
                }

                HTMLCon call = new HTMLCon();
                call.execute(fullContacts);

                Intent intent = new Intent(RegisterEvent.this, CreateEvent.class);
                startActivity(intent);
                break;
        }

    }

    public class HTMLCon extends AsyncTask<String, Void, Void> {


        @Override
        protected Void doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            // replace with your url
            HttpPost httpPost = new HttpPost("http://ec2-52-36-37-169.us-west-2.compute.amazonaws.com:8080/api/call");


            //Post Data
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("", params[0]));
            //nameValuePair.add(new BasicNameValuePair("EventDesc", "Lets goto airport"));


            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                // log exception
                e.printStackTrace();
            }

            //making POST request.
            try {
                HttpResponse response = httpClient.execute(httpPost);
                // write response to log
                Log.d("Http Post Response:", response.toString());
            } catch (ClientProtocolException e) {
                // Log exception
                e.printStackTrace();
            } catch (IOException e) {
                // Log exception
                e.printStackTrace();
            }
            return null;
        }

    }
}