package hackdfw.connectme;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Arnav on 4/17/2016.
 */
public class SQLDatabase {


    public static final String KEY_ROWID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_NUMBER = "phoneNumber";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_EVENTID = "eventID";

    private static final String DATABASE_NAME = "ContactList";
    private static final String DATABASE_TABLE = "ContactTable";
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

    public long createEntry(String name, String phoneNumber, String email, String eventID) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_NUMBER,phoneNumber);
        contentValues.put(KEY_EMAIL, email);
        contentValues.put(KEY_EVENTID, eventID);

        return sqLiteDatabase.insert(DATABASE_TABLE, null, contentValues);
    }

    public void close() {

        dbHelper.close();
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
                            KEY_NAME + " TEXT NOT NULL, " + KEY_NUMBER +
                            " INTEGER, " + KEY_EMAIL + " TEXT, " + KEY_EVENTID + " INTEGER);"
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
