package com.example.android.sportsway;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.sportsway.Model.EventOnline;
import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;
import com.example.android.sportsway.Utility.XmlParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends AppCompatActivity {

    private Context t = this;
    private SportswayDbHelper mDbHelper;
    private Cursor mCursor;
    private float mLatitude, mLongitude;
    private ArrayList<EventOnline> mOnlineEvents = new ArrayList<EventOnline>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new Timer().schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        // Using shared preferences
                        SharedPreferences pref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        if (pref.getBoolean("hasData", false)) {
                            // Dd nothing
                        }
                        else {
                            // getting location
                            float longitude = 0;
                            float latitude = 0;
                            if (ActivityCompat.checkSelfPermission(t, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(t, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(t, "Location permissions not set", Toast.LENGTH_SHORT).show();
                            }
                            LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location == null) {
                                System.out.println("Couldn't get location");
                            }
                            else {
                                longitude = (float) location.getLongitude();
                                latitude = (float) location.getLatitude();
                            }
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putBoolean("hasData", true);
                            editor.putFloat("longitude", longitude);
                            editor.putFloat("latitude", latitude);
                            editor.commit();
                            mLatitude = latitude;
                            mLongitude = longitude;
                            fetchEventsAndPopulateDb();
                        }
                    }
                },
                5000
        );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // requesting permissions if not set
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        // getting users from database
        this.mDbHelper = new SportswayDbHelper(this);
        SQLiteDatabase db = this.mDbHelper.getReadableDatabase();
        this.mCursor = db.query(SportswayContract.UserEntry.TABLE_NAME,
                new String[]{SportswayContract.UserEntry._ID, SportswayContract.UserEntry.COLUMN_EMAIL, SportswayContract.UserEntry.COLUMN_PASSWORD},
                null, null, null, null, null);
    }

    public void login(View view) {
        EditText et1 = (EditText)findViewById(R.id.username);
        EditText et2 = (EditText)findViewById(R.id.password);
        String etEmail = et1.getText().toString();
        String etPassword = et2.getText().toString();

        int user_email = this.mCursor.getColumnIndex(SportswayContract.UserEntry.COLUMN_EMAIL);
        int user_password = this.mCursor.getColumnIndex(SportswayContract.UserEntry.COLUMN_PASSWORD);

        boolean found = false;
        this.mCursor.moveToPosition(-1);
        while (this.mCursor.moveToNext()) {
            int id = mCursor.getInt(mCursor.getColumnIndex(SportswayContract.UserEntry._ID));
            String email = this.mCursor.getString(user_email);
            String password = this.mCursor.getString(user_password);
            Log.d(this.getClass().getSimpleName(), "Email: " + email);
            Log.d(this.getClass().getSimpleName(), "Password: " + password);
            if (email.equals(etEmail) && password.equals(etPassword)) {
                found = true;
                SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("userId", id);
                editor.commit();
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                finish();
            }
        }

        if (!found) {
            Snackbar.make(view, "Invalid username or password try again", Snackbar.LENGTH_SHORT).show();
        }

    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mCursor.close();
        this.mDbHelper.close();
    }

    private int getRandomNumberInRange(int min, int max) {

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public void fetchEventsAndPopulateDb() {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.eventful.com/rest/events/search?app_key=dw6DGw5d7Z2qfSCD&where="
                + mLatitude + "," + mLongitude + "&within=25";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                XmlParser parser = new XmlParser(response);
                mOnlineEvents = parser.getEvents();
                try {
                    storeEventsTodb();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "" + error);
            }
        });
        queue.add(request);
    }

    private void storeEventsTodb() throws Exception{
        SportswayDbHelper dbHelper = new SportswayDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Iterator<EventOnline> itr = mOnlineEvents.iterator();
        while (itr.hasNext()) {
            EventOnline eventOnline = itr.next();
            ContentValues cv = new ContentValues();
            cv.put(SportswayContract.EventEntry.COLUMN_EVENT_NAME, eventOnline.getTitle());
            cv.put(SportswayContract.EventEntry.COLUMN_EVENT_LOCATION, eventOnline.getCity_name());
            cv.put(SportswayContract.EventEntry.COLUMN_START_TIME, eventOnline.getStart_time());
            cv.put(SportswayContract.EventEntry.COLUMN_TICKETS, getRandomNumberInRange(50, 200));
            db.insert(SportswayContract.EventEntry.TABLE_NAME, null, cv);
        }
        db.close();
        dbHelper.close();
    }
}
