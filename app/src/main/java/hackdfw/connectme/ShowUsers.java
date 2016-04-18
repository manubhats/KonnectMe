package hackdfw.connectme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arnav on 4/17/2016.
 */
public class ShowUsers extends Activity {

    Button suckit;


    SQLEventDatabase eventDatabase = new SQLEventDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_members);


        SharedPreferences sharedPreferences = getSharedPreferences("User Number", Context.MODE_PRIVATE);
        final String usergivennumber = sharedPreferences.getString("user number", "");
        suckit = (Button) findViewById(R.id.suckit);

        suckit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final RevieveData rd = new RevieveData();
                rd.execute(usergivennumber);
            }
        });




        ListView lvUser = (ListView) findViewById(R.id.lvListUser);

        int position = getIntent().getIntExtra("Position", 0);
        eventDatabase.open();
        ArrayList<Event> eventList = eventDatabase.getData();
        eventDatabase.close();

        ArrayList<String> UserNames = new ArrayList<>();

        SQLDatabase sqlDatabase = new SQLDatabase(this);
        sqlDatabase.open();
        ArrayList<UserInformation> user = sqlDatabase.getData(eventList.get(position).eventID);
        sqlDatabase.close();

        Log.d("BABYYY", "" + user.size());

        ArrayList<UserInformation> tempuser = new ArrayList<>();

        for (int i = 0; i < user.size(); i++) {

            if (user.get(i).eventID == eventList.get(position).eventID) {

                tempuser.add(user.get(i));
            }
        }

        Log.d("BABYYY", "" + tempuser.size());

        final ArrayAdapter<UserInformation> arrayAdapter = new ArrayAdapter<UserInformation>(
                ShowUsers.this, R.layout.list_item, user);

        lvUser.setAdapter(arrayAdapter);




    }

    public class RevieveData extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            // replace with your url
            HttpPost httpPost = new HttpPost("http://ec2-52-36-37-169.us-west-2.compute.amazonaws.com:8080/api/update");


            //Post Data
            List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(2);
            nameValuePair.add(new BasicNameValuePair("", params[0]));
            //nameValuePair.add(new BasicNameValuePair("EventDesc", "Lets goto airport"));


            //Encoding POST data
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                Log.d("HTTPPOST", "hahha");
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

        public AsyncResponse delegate = null;

        @Override
        protected void onPostExecute(String result) {
            delegate.processFinish(result);
        }
    }
}
