package assignment.keng.birthdayreminder;

import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


public class PostJsonTask extends AsyncTask<Void, Void, JSONObject> {
    private static final String JSON_URL = "http://labs.jamesooi.com/uecs3253-asg.php";

    private MainActivity mainActivity;

    public PostJsonTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        JSONObject response = null;

        try {
            response = postJSON();
        } catch (IOException ex) {
            Log.e("IO_EXCEPTION", ex.toString());
        }
        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        try {
            if (response.getLong("recordsSynced") > 0) {
                Toast toast = Toast.makeText(mainActivity, response.getLong("recordsSynced")+" records backuped", Toast.LENGTH_LONG);
                toast.show();
            }
            Log.d("records synced", Long.toString(response.getLong("recordsSynced")));
        }
        catch (Exception ex) {
            Log.e("JSON_EXCEPTION", ex.toString());
        }
    }

    private JSONObject postJSON() throws IOException {
        InputStream is = null;
        OutputStream os = null;

        String[] columns = {
                BirthdayContract.ContactEntry._ID,
                BirthdayContract.ContactEntry.COLUMN_NAME_NAME,
                BirthdayContract.ContactEntry.COLUMN_NAME_EMAIL,
                BirthdayContract.ContactEntry.COLUMN_NAME_BIRTHDATE
        };

        Cursor cursor = mainActivity.dbQueries.query(columns, null, null, null, null, null);

        JSONArray jArr = new JSONArray();
        try{
            if (cursor.moveToNext()) {
                do{
                    JSONObject jContact = new JSONObject();
                    jContact.put("id", cursor.getString(cursor.getColumnIndex(BirthdayContract.ContactEntry._ID)));
                    jContact.put("name", cursor.getString(cursor.getColumnIndex(BirthdayContract.ContactEntry.COLUMN_NAME_NAME)));
                    jContact.put("birthDate", cursor.getString(cursor.getColumnIndex(BirthdayContract.ContactEntry.COLUMN_NAME_BIRTHDATE)));
                    jContact.put("email", cursor.getString(cursor.getColumnIndex(BirthdayContract.ContactEntry.COLUMN_NAME_EMAIL)));
                    jArr.put(jContact);
                } while (cursor.moveToNext());
            }

            URL url = new URL(JSON_URL);

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(jArr));
            writer.flush();
            writer.close();
            os.close();

            int responseCode = conn.getResponseCode();
            if(responseCode == 200) {
                is = conn.getInputStream();

                return readInputStream(is);
            }
            else {
                Log.e("HTTP_ERROR", Integer.toString(responseCode));
                return null;
            }

        } catch (Exception ex) {
            Log.e("EXCEPTION", ex.toString());
            return null;
        }
    }

    public JSONObject readInputStream(InputStream is)
            throws IOException, JSONException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuilder builder = new StringBuilder();

        String input;
        while ((input = reader.readLine()) != null)
            builder.append(input);

        return new JSONObject(builder.toString());
    }

    private String getPostDataString(JSONArray data) throws Exception {

        StringBuilder result = new StringBuilder();

        result.append(URLEncoder.encode("data", "UTF-8"));
        result.append("=");
        result.append(URLEncoder.encode(data.toString(), "UTF-8"));

        return result.toString();
    }
}
