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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

    private String cid, name, numb;
    private ArrayList<ContactID> contactss = new ArrayList<ContactID>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


    final Button invitePeople = (Button) findViewById(R.id.invitePeople);
    invitePeople.setOnClickListener(new View.OnClickListener()
    {
        @Override
        public void onClick (View v){

        cont = new ContactID("1", "name", "numb");
        contactss.add(cont);
        ContactID contactArray[] = new ContactID[contactss.size()];
        contactArray = contactss.toArray(contactArray);
        JSONObject obj = new JSONObject();
        try {
            obj.put("initiator_name", initiator_name);
            obj.put("initiator_phone_number", user.getInitiator_phone_number());
            obj.put("event_id", user.getEvent_id());
            obj.put("event_name", user.getEvent_name());
            obj.put("event_message", user.getEvent_message());
            obj.put("contacts", (new JSONArray(Arrays.asList(contactArray))).toString());

            String json = obj.toString();
            String uri = "ec2-52-36-37-169.us-west-2.compute.amazonaws.com:8080/api/call";
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(uri);

            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            params.add(new BasicNameValuePair("data", json));

            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

//Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    // do something useful
                } finally {
                    instream.close();
                }
            }

            //makeRequest(json,uri);
            Log.i(TAG, json);
        } catch (Exception e) {

        }

/*

                user = new User(initiator_name,initiator_phone_number,event_id,event_name,event_message,contactss);
                // declare parameters that are passed to nodejs
                ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();


                // define the parameter
                postParameters.add(new BasicNameValuePair("initiator_name",user.getInitiator_name()));
                postParameters.add(new BasicNameValuePair("initiator_phone_number",user.getInitiator_phone_number()));
                postParameters.add(new BasicNameValuePair("event_id",user.getEvent_id()));
                postParameters.add(new BasicNameValuePair("event_name",user.getEvent_name()));
                postParameters.add(new BasicNameValuePair("event_message",user.getEvent_message()));
                postParameters.add(new BasicNameValuePair("contacts",user.getConts()));*/

//                httpWrapper.setPostParameters(postParameters);
//                try{
//                    httppost = new HttpPost("http://omega.uta.edu/Path to nodejs script");
//                    httpWrapper.setRegisterActivity(RegisterEvent.this);
//                    httpWrapper.execute(httppost);
//                }
//                catch(Exception e){
//                    Log.e("register_activity", "Error in http connection "+e.toString());
//                }

    }


    }

    );

}

    //    public static HttpResponse makeRequest(String uri, String json) {
//        try {
//
//
//            HttpPost httpPost = new HttpPost(uri);
//            httpPost.setEntity(new StringEntity(json));
//            httpPost.setHeader("Accept", "application/json");
//            httpPost.setHeader("Content-type", "application/json");
//            return new DefaultHttpClient().execute(httpPost);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

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

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PICK_CONTACT && resultCode == RESULT_OK) {
//            EditText contNumb = (EditText)findViewById(R.id.contNumb);
//            EditText contName = (EditText)findViewById(R.id.contName);
//            String contactName = null;
//            Uri contactUri = data.getData();
//            Cursor cursor = getContentResolver().query(contactUri, null, null, null, null);
//            while(cursor.moveToNext()){
//                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME));
//            }
//            cursor.moveToFirst();
//            int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
//            Log.d("phone number", cursor.getString(column));
//            contName.setText(contactName);
//            contNumb.setText(cursor.getString(column));
//        }
//    }

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

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RegisterEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://hackdfw.connectme/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "RegisterEvent Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://hackdfw.connectme/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}