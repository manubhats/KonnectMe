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

    public ArrayList<Event> getData() {

        ArrayList<Event> eventList = new ArrayList<>();

        String[] columns = new String[]{KEY_ROWID, KEY_NAME, KEY_EVENTID };

        try {
            String query = "SELECT * FROM " + DATABASE_TABLE;
            Cursor c = sqLiteDatabase.rawQuery(query, null);
            String result = "";

            int iRow = c.getColumnIndex(KEY_ROWID);
            int iName = c.getColumnIndex(KEY_NAME);
            int iEventID = c.getColumnIndex(KEY_EVENTID);

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {


                Event event = new Event();
                event.eventID = c.getString(iEventID);
                event.eventName = c.getString(iName);

                eventList.add(event);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eventList;
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
                    "CREATE TABLE "
                            + DATABASE_TABLE + " ("
                            + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + KEY_NAME + " TEXT NOT NULL, "
                            + KEY_EVENTID + " INTEGER);"
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
