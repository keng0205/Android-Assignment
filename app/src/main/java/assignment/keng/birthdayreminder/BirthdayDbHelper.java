package assignment.keng.birthdayreminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class BirthdayDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contact.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BirthdayContract.ContactEntry.TABLE_NAME + " (" +
                    BirthdayContract.ContactEntry._ID + " INTEGER PRIMARY KEY," +
                    BirthdayContract.ContactEntry.COLUMN_NAME_NAME + " TEXT," +
                    BirthdayContract.ContactEntry.COLUMN_NAME_EMAIL + " TEXT," +
                    BirthdayContract.ContactEntry.COLUMN_NAME_BIRTHDATE + " TEXT)";

    public BirthdayDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
