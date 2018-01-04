package com.example.android.sportsway;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.android.sportsway.Model.Event;
import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;

import java.util.ArrayList;

public class EventsFragment extends Fragment {

    private ArrayList<Event> mEvents = new ArrayList<Event>();
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_events, container, false);
        fetchEvents();
        configureRecyclerView();
        return this.root;
    }

    public void fetchEvents() {
        SportswayDbHelper dbHelper = new SportswayDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SportswayContract.EventEntry.TABLE_NAME, new String[]{SportswayContract.EventEntry._ID,
                SportswayContract.EventEntry.COLUMN_EVENT_NAME,
                SportswayContract.EventEntry.COLUMN_EVENT_LOCATION, SportswayContract.EventEntry.COLUMN_START_TIME,
                SportswayContract.EventEntry.COLUMN_TICKETS}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            Event event = new Event();
            event.set_id(cursor.getInt(cursor.getColumnIndex(SportswayContract.EventEntry._ID)));
            event.setEvent_name(cursor.getString(cursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_EVENT_NAME)));
            event.setEvent_location(cursor.getString(cursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_EVENT_LOCATION)));
            event.setEvent_start_time(cursor.getString(cursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_START_TIME)));
            event.setEvent_tickets(cursor.getInt(cursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_TICKETS)));
            mEvents.add(event);
        }
        cursor.close();
        db.close();
        dbHelper.close();
    }

    public void configureRecyclerView() {
        if (mEvents.size() > 0) {
            LinearLayout layout = (LinearLayout) this.root.findViewById(R.id.home_empty_view);
            layout.setVisibility(View.GONE);
            RecyclerView mRecyclerView = (RecyclerView) this.root.findViewById(R.id.list_of_events);
            mRecyclerView.setHasFixedSize(true);
            // use a linear layout manager
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            mRecyclerView.setLayoutManager(mLayoutManager);
            // specify an adapter
            RecyclerView.Adapter mAdapter = new MyAdapter(getContext(), mEvents);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            RecyclerView mRecyclerView = (RecyclerView) this.root.findViewById(R.id.list_of_events);
            LinearLayout layout = (LinearLayout) this.root.findViewById(R.id.home_empty_view);
            mRecyclerView.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
        }
    }
}
