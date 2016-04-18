package hackdfw.connectme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Arnav on 4/17/2016.
 */
public class SQLDatabase {


    public static final String KEY_ROWID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_NUMBER = "phoneNumber";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_EVENTID = "eventID";
    public static final String KEY_EVENTDESC = "eventDesc";
    public static final String KEY_PLAN = "eventPlan";

    public static final String KEY_EVENTNAME = "eventName";

    private static final String DATABASE_NAME = "listContact";
    private static final String DATABASE_TABLE = "tablecontact";
    private static final int DATABASE_VERSION = 1;

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public SQLDatabase(Context context) {
        this.context = context;
    }

    public SQLDatabase open() {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public long createEntry(String name, String email, String phoneNumber, String eventID, String eventName, String eventDesc, String eventPlan) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_NUMBER, phoneNumber);
        contentValues.put(KEY_EMAIL, email);
        contentValues.put(KEY_EVENTID, eventID);
        contentValues.put(KEY_EVENTNAME, eventName);
        contentValues.put(KEY_EVENTDESC, eventDesc);
        contentValues.put(KEY_PLAN, eventPlan);

        return sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public void close() {

        dbHelper.close();
    }

    public ArrayList<UserInformation> getData(String eventID) {

        ArrayList<UserInformation> userList = new ArrayList<>();

        String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_NUMBER, KEY_EMAIL, KEY_EVENTID
        , KEY_EVENTDESC, KEY_EVENTNAME, KEY_PLAN};

        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE eventID = " + eventID;
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        String result = "";

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iNumber = c.getColumnIndex(KEY_NUMBER);
        int iEmail = c.getColumnIndex(KEY_EMAIL);
        int iEventID = c.getColumnIndex(KEY_EVENTID);
        int iEventString = c.getColumnIndex(KEY_EVENTNAME);
        int iEventData = c.getColumnIndex(KEY_EVENTDESC);
        int iEventPlan = c.getColumnIndex(KEY_PLAN);


        Log.d("SQLDATABASE", "SQLDATABASE: " + iRow + "\n" + iName + "\n" + iNumber + "\n" + iEmail + "\n" + iEventID);

        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {


            UserInformation userInformation = new UserInformation();
            userInformation.userID = c.getString(iRow);
            userInformation.name = c.getString(iName);
            userInformation.number = c.getString(iNumber);
            userInformation.email = c.getString(iEmail);
            userInformation.eventID = c.getString(iEventID);
            userInformation.eventName = c.getString(iEventString);
            userInformation.eventDesc = c.getString(iEventData);
            userInformation.plan =c.getString(iEventPlan);

            userList.add(userInformation);
        }

        return userList;
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(
                    "CREATE TABLE " + DATABASE_TABLE + " ("
                            + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + KEY_NAME + " TEXT NOT NULL, "
                            + KEY_NUMBER + " INTEGER, "
                            + KEY_EMAIL + " TEXT, "
                            + KEY_EVENTID + " INTEGER, "
                            + KEY_EVENTDESC + " TEXT, "
                            + KEY_PLAN + " TEXT, "
                            + KEY_EVENTNAME + " TEXT NOT NULL);"
            );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(
                    "DROP TABLE IF EXISTS " + DATABASE_TABLE
            );

            onCreate(db);
        }
    }
}
