package com.example.android.sportsway;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;

import java.util.concurrent.TimeUnit;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public void addEvent(View view) {
        EditText event_name_et = (EditText) findViewById(R.id.event_name_et);
        EditText event_location_et = (EditText) findViewById(R.id.event_location_et);
        EditText event_date_et = (EditText) findViewById(R.id.event_date_et);
        EditText event_start_time_et = (EditText) findViewById(R.id.event_start_time_et);

        String event_name = event_name_et.getText().toString();
        String event_location = event_location_et.getText().toString();
        String event_date = event_date_et.getText().toString();
        String event_start_time = event_start_time_et.getText().toString();

        SportswayDbHelper dbHelper = new SportswayDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SportswayContract.EventEntry.COLUMN_EVENT_NAME, event_name);
        cv.put(SportswayContract.EventEntry.COLUMN_EVENT_LOCATION, event_location);
        cv.put(SportswayContract.EventEntry.COLUMN_EVENT_DATE, event_date);
        cv.put(SportswayContract.EventEntry.COLUMN_START_TIME, event_start_time);
        long rowID = db.insert(SportswayContract.EventEntry.TABLE_NAME, null, cv);

        if (rowID < 0) {
            Snackbar.make(view, "Error inserting event", Snackbar.LENGTH_SHORT).show();
        }
        else {
            finish();
        }
    }
}
