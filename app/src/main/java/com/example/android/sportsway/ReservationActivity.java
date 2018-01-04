package com.example.android.sportsway;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.sportsway.Model.Event;
import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;

import java.util.ArrayList;
import java.util.Random;

public class ReservationActivity extends AppCompatActivity {

    public static String extra_event_id = "evtId";
    public static String extra_event_name = "evtName";
    public static String extra_event_start_time = "evtStartTime";
    public static String extra_event_tickets = "evtTickets";
    public static String extra_ticket_price = "tktPrice";


    private int event_id = 0;
    private String event_name = "";
    private String event_start_time = "";
    private int event_tickets = 0;
    private int user_id = 0;
    private String ticket_price = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        user_id = preferences.getInt("userId", 0);

        // Populating data
        Intent intent = getIntent();
        event_id = intent.getIntExtra(extra_event_id, 0);
        event_name = intent.getStringExtra(extra_event_name);
        event_start_time = intent.getStringExtra(extra_event_start_time);
        event_tickets = intent.getIntExtra(extra_event_tickets, 0);
        ticket_price = intent.getStringExtra(extra_ticket_price);

        if (event_tickets <= 0) {
            soldOut();
        }
        else {
            // making sold out invisible
            ImageView iv = (ImageView) findViewById(R.id.reservation_sold_out);
            iv.setVisibility(View.GONE);
            // setting views
            TextView reservation_tickets_left = (TextView) findViewById(R.id.reservation_tickets_left);
            TextView reservation_event_name = (TextView) findViewById(R.id.reservation_event_name);
            TextView reservation_event_start_time = (TextView) findViewById(R.id.reservation_event_start_time);
            TextView reservation_ticket_price = (TextView) findViewById(R.id.reservation_ticket_price);
            reservation_event_name.setText(event_name);
            reservation_event_start_time.setText("Start Time: " + event_start_time);
            reservation_tickets_left.setText(Integer.toString(event_tickets) + " tickets left");
            reservation_ticket_price.setText("Ticket Price: Rs. 700");
        }
    }

    public void buyTicket(View view) {
        SportswayDbHelper dbHelper = new SportswayDbHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SportswayContract.EventEntry.COLUMN_TICKETS, event_tickets - 1);
        db.update(SportswayContract.EventEntry.TABLE_NAME, cv, "_id=?", new String[]{Integer.toString(event_id)});
        cv = new ContentValues();
        cv.put(SportswayContract.ReservationEntry.COLUMN_EVENT_ID, event_id);
        cv.put(SportswayContract.ReservationEntry.COLUMN_TICKET_ID, event_tickets);
        cv.put(SportswayContract.ReservationEntry.COLUMN_USER_ID, user_id);
        db.insert(SportswayContract.ReservationEntry.TABLE_NAME, null, cv);
        db.close();
        dbHelper.close();
        Toast.makeText(this, "Ticket reserved successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void soldOut() {
        CardView cv = (CardView) findViewById(R.id.reservation_card_view);
        LinearLayout layout = (LinearLayout) findViewById(R.id.reservation_event_details);
        Button btn = (Button) findViewById(R.id.reservation_buy_button);
        ImageView iv = (ImageView) findViewById(R.id.reservation_sold_out);
        cv.setVisibility(View.GONE);
        layout.setVisibility(View.GONE);
        btn.setVisibility(View.GONE);
        iv.setVisibility(View.VISIBLE);
    }
}
