package assignment.keng.birthdayreminder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class AddContactActivity extends AppCompatActivity {
    private EditText etName;
    private EditText etEmail;
    private EditText etBirthDate;

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private boolean saved = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPref = getSharedPreferences(getResources().getString(R.string.sp_save_state), Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        etName = (EditText)AddContactActivity.this.findViewById(R.id.input_name);
        etEmail = (EditText)AddContactActivity.this.findViewById(R.id.input_email);
        etBirthDate = (EditText)AddContactActivity.this.findViewById(R.id.input_birthdate);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String birthDate = etBirthDate.getText().toString();

                BirthdayDbQueries dbq = new BirthdayDbQueries(new BirthdayDbHelper(getApplicationContext()));
                Contact contact = new Contact(name, email, birthDate);
                if(dbq.insert(contact) != 0) {
                    saved = true;
                }
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void showDatePickerDialog(View view) {
        DialogFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(saved) {
            editor.clear();
        }
        else {
            String name = etName.getText().toString();
            String email = etEmail.getText().toString();
            String birthDate = etBirthDate.getText().toString();

            editor.putString("SAVE_STATE_NAME", name);
            editor.putString("SAVE_STATE_EMAIL", email);
            editor.putString("SAVE_STATE_BIRTHDATE", birthDate);
        }

        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        String name = sharedPref.getString("SAVE_STATE_NAME", "");
        String email = sharedPref.getString("SAVE_STATE_EMAIL", "");
        String birthDate = sharedPref.getString("SAVE_STATE_BIRTHDATE", "");

        etName.setText(name);
        etEmail.setText(email);
        etBirthDate.setText(birthDate);
    }
}
