package hackdfw.connectme;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Arnav on 4/17/2016.
 */
public class SQLEventDatabase {
    public static final String KEY_ROWID = "id";
    public static final String KEY_NAME = "event_name";
    public static final String KEY_EVENTID = "eventID";

    private static final String DATABASE_NAME = "EventID";
    private static final String DATABASE_TABLE = "EventTable";
    private static final int DATABASE_VERSION = 1;

    private DBHelper dbHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Context context;

    public SQLEventDatabase(Context context) {
        this.context = context;
    }

    public SQLEventDatabase open() {
        dbHelper = new DBHelper(context);
        sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public long createEntry(String name, String eventID) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_EVENTID, eventID);

        return sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public void close() {

        dbHelper.close();
    }

    public ArrayList<UserInformation> getData(String eventID) {

        ArrayList<UserInformation> userList = new ArrayList<>();

        String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_EVENTID };

        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE eventID = " + eventID;
        Cursor c = sqLiteDatabase.rawQuery(query, null);
        String result = "";

        int iRow = c.getColumnIndex(KEY_ROWID);
        int iName = c.getColumnIndex(KEY_NAME);
        int iEventID = c.getColumnIndex(KEY_EVENTID);


//        Log.d("SQLDATABASE", "SQLDATABASE: " + iRow + "\n" + iName + "\n" + iNumber + "\n" + iEmail + "\n" + iEventID);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {


            UserInformation userInformation = new UserInformation();
            userInformation.userID = c.getString(iRow);
            userInformation.name = c.getString(iName);
            userInformation.eventID = c.getString(iEventID);

            userList.add(userInformation);

//            result = result + c.getString(iRow)
//                    + " " + c.getString(iName)
//                    + " " + c.getString(iNumber)
//                    + " " + c.getString(iEmail)
//                    + " " + c.getString(iEventID)
//                    + "\n";
        }

        return userList;
    }

    public void deleteAll()
    {
        sqLiteDatabase.delete(DATABASE_TABLE, null, null);
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(
                    "CREATE TABLE " + DATABASE_TABLE + " (" +
                            KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            KEY_NAME + " TEXT NOT NULL, " + KEY_EVENTID + " INTEGER);"
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
