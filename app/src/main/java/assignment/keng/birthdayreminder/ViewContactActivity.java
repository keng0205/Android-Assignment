package assignment.keng.birthdayreminder;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ViewContactActivity extends AppCompatActivity {
    public static final String EXTRA_CONTACT = "assignment.keng.birthdayreminder.CONTACT";
    private Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    public void onResume() {
        super.onResume();

        Intent intent = getIntent();
        long id = intent.getLongExtra(MainActivity.EXTRA_ID, 0);

        BirthdayDbQueries dbq = new BirthdayDbQueries(new BirthdayDbHelper(getApplicationContext()));

        final String[] columns = {
                BirthdayContract.ContactEntry._ID,
                BirthdayContract.ContactEntry.COLUMN_NAME_NAME,
                BirthdayContract.ContactEntry.COLUMN_NAME_EMAIL,
                BirthdayContract.ContactEntry.COLUMN_NAME_BIRTHDATE
        };

        String selection = BirthdayContract.ContactEntry._ID + " = ?";
        String[] selectionArgs = {Long.toString(id)};

        Cursor cursor = dbq.query(columns, selection, selectionArgs, null, null, null);

        if(cursor.moveToNext()) {
            contact = new Contact(
                    cursor.getLong(cursor.getColumnIndex(BirthdayContract.ContactEntry._ID)),
                    cursor.getString(cursor.getColumnIndex(BirthdayContract.ContactEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndex(BirthdayContract.ContactEntry.COLUMN_NAME_EMAIL)),
                    cursor.getString(cursor.getColumnIndex(BirthdayContract.ContactEntry.COLUMN_NAME_BIRTHDATE))
            );

            setTitle(contact.getName());

            EditText etName = (EditText)findViewById(R.id.item_name);
            EditText etEmail = (EditText)findViewById(R.id.item_email);
            EditText etBirthday = (EditText)findViewById(R.id.item_birthDate);

            etName.setText(contact.getName());
            etEmail.setText(contact.getEmail());
            etBirthday.setText(contact.getBirthDate());

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), UpdateContactActivity.class);
                   intent.putExtra(EXTRA_CONTACT, contact);
                   startActivity(intent);
                }
            });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        else {
            Log.e("id not found", Long.toString(cursor.getLong(cursor.getColumnIndex(BirthdayContract.ContactEntry._ID))));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id) {
            case R.id.action_update:
               Intent intent = new Intent(getApplicationContext(), UpdateContactActivity.class);
               intent.putExtra(EXTRA_CONTACT, contact);
               startActivity(intent);
                return true;

            case R.id.action_delete:
                BirthdayDbQueries dbq = new BirthdayDbQueries(new BirthdayDbHelper(getApplicationContext()));
                dbq.delete(contact.getId());
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
