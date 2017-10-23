package assignment.keng.birthdayreminder;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;



public class BirthdayDbQueries {
    private BirthdayDbHelper helper;

    public BirthdayDbQueries(BirthdayDbHelper helper) {
        this.helper = helper;
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        SQLiteDatabase db = helper.getReadableDatabase();

        return db.query(
                BirthdayContract.ContactEntry.TABLE_NAME,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy
        );
    }

    public long insert(Contact contact) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BirthdayContract.ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(BirthdayContract.ContactEntry.COLUMN_NAME_EMAIL, contact.getEmail());
        values.put(BirthdayContract.ContactEntry.COLUMN_NAME_BIRTHDATE, contact.getBirthDate());

        long id = db.insert(BirthdayContract.ContactEntry.TABLE_NAME, null, values);
        contact.setId(id);
        return id;
    }

    public int update(Contact contact) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BirthdayContract.ContactEntry.COLUMN_NAME_NAME, contact.getName());
        values.put(BirthdayContract.ContactEntry.COLUMN_NAME_EMAIL, contact.getEmail());
        values.put(BirthdayContract.ContactEntry.COLUMN_NAME_BIRTHDATE, contact.getBirthDate());

        String selection = BirthdayContract.ContactEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(contact.getId())};

        return db.update(
                BirthdayContract.ContactEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public void delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = BirthdayContract.ContactEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};
        db.delete(BirthdayContract.ContactEntry.TABLE_NAME, selection, selectionArgs);
    }
}
