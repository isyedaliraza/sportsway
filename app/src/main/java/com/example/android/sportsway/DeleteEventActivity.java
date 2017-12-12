package com.example.android.sportsway;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;

public class DeleteEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_event);
    }

    public void deleteEvent(View view) {
        EditText event_id_et = (EditText) findViewById(R.id.event_id_et);
        String event_id = event_id_et.getText().toString();

        SportswayDbHelper dbHelper = new SportswayDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        db.delete(SportswayContract.EventEntry.TABLE_NAME, SportswayContract.EventEntry._ID + " = ?",
                new String[]{event_id});
        finish();
    }
}
