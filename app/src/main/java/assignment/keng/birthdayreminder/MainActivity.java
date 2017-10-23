package assignment.keng.birthdayreminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "assignment.keng.birthdayreminder.ID";
    private ListView listView;
    private int dayToShow;
    private boolean showAll = true;
    Cursor cursor;
    BirthdayDbQueries dbQueries;


    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.all_contact) {
                showAll = true;
            } else {
                showAll = false;
            }
            switch (item.getItemId()) {
                case R.id.all_contact:
                    onResume();
                    return true;
                case R.id.today:
                    dayToShow=0;
                    onResume();
                    return true;
                case R.id.tmr:
                    dayToShow=1;
                    onResume();
                    return true;
                case R.id.dayAfter:
                    dayToShow=2;
                    onResume();
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddContactActivity.class);
                startActivity(intent);


            }
        });

        listView = (ListView)findViewById(R.id.list_view);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = (Cursor)adapterView.getItemAtPosition(position);

                Intent intent = new Intent(MainActivity.this, ViewContactActivity.class);
                intent.putExtra(EXTRA_ID, cursor.getLong(cursor.getColumnIndex(BirthdayContract.ContactEntry._ID)));
                MainActivity.this.startActivity(intent);
            }
        });
        BottomNavigationView botNav = (BottomNavigationView) findViewById(R.id.navigation);
        botNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        dbQueries = new BirthdayDbQueries(new BirthdayDbHelper(getApplicationContext()));

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,8);
        Intent intent= new Intent(getApplicationContext(),Notification_receiver.class);
        PendingIntent pendingIntent= PendingIntent.getBroadcast(getApplicationContext(),100,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager= (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY,pendingIntent);

    }


    @Override
    public void onResume() {
        super.onResume();

        String[] columns = {
                BirthdayContract.ContactEntry._ID,
                BirthdayContract.ContactEntry.COLUMN_NAME_NAME,
                BirthdayContract.ContactEntry.COLUMN_NAME_BIRTHDATE
        };
        String selection =  "strftime('%m',birthDate) = ? AND strftime('%d',birthDate) = ? " ;

        int strMonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
        int strDate = Integer.parseInt(new SimpleDateFormat("dd").format(new Date())) + dayToShow;

        String[] selectionArgs = {
                String.format("%2s", String.valueOf(strMonth)).replace(' ', '0'),
                String.format("%2s", String.valueOf(strDate)).replace(' ', '0')
        };

        if(showAll) {
            cursor = dbQueries.query(columns, null, null, null, null, BirthdayContract.ContactEntry.COLUMN_NAME_NAME + " ASC");
        } else {
            cursor = dbQueries.query(columns, selection, selectionArgs ,null,null, BirthdayContract.ContactEntry.COLUMN_NAME_NAME + " ASC");
        }

        BirthdayCursorAdapter adapter = new BirthdayCursorAdapter(this, cursor, 0);

        listView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_backup_all, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.backup_all:
                //backup data
                ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if(networkInfo != null && networkInfo.isConnected()) {
                    new PostJsonTask(MainActivity.this).execute();
                }else {
                    Toast toast = Toast.makeText(MainActivity.this, getResources().getString(R.string.network_unavailable), Toast.LENGTH_LONG);
                    toast.show();
                }
        }
        return super.onOptionsItemSelected(item);
    }



}
