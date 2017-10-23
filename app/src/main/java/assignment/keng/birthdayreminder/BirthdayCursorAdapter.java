package assignment.keng.birthdayreminder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


public class BirthdayCursorAdapter extends CursorAdapter {
    private LayoutInflater inflater;

    public BirthdayCursorAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView tvName = (TextView)view.findViewById(R.id.item_name);
        TextView tvBirthDate = (TextView)view.findViewById(R.id.item_birthDate);
        String name = cursor.getString(cursor.getColumnIndex(BirthdayContract.ContactEntry.COLUMN_NAME_NAME));
        String phone = cursor.getString(cursor.getColumnIndex(BirthdayContract.ContactEntry.COLUMN_NAME_BIRTHDATE));
        tvName.setText(name);
        tvBirthDate.setText(phone);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return inflater.inflate(R.layout.list_item, parent, false);
    }
}
