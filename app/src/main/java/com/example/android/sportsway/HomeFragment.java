package com.example.android.sportsway;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.sportsway.Model.Event;
import com.example.android.sportsway.Model.EventOnline;
import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;
import com.example.android.sportsway.Utility.XmlParser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private ArrayList<Event> mEvents = new ArrayList<Event>();
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_home, container, false);

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchEvents();
        if (this.mEvents.size() == 0) {
            RecyclerView mRecyclerView = (RecyclerView) this.root.findViewById(R.id.list_of_events);
            LinearLayout emptyview = (LinearLayout) this.root.findViewById(R.id.home_empty_view);
            mRecyclerView.setVisibility(View.GONE);
            emptyview.setVisibility(View.VISIBLE);
        }
        else {
            RecyclerView mRecyclerView = (RecyclerView) this.root.findViewById(R.id.list_of_events);
            LinearLayout emptyview = (LinearLayout) this.root.findViewById(R.id.home_empty_view);
            mRecyclerView.setVisibility(View.VISIBLE);
            emptyview.setVisibility(View.GONE);
            configureRecyclerView();
        }
    }

    public void fetchEvents() {
        this.mEvents.clear();

        SportswayDbHelper mDbHelper = new SportswayDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor mCursor = db.query(SportswayContract.EventEntry.TABLE_NAME,
                new String[]{SportswayContract.EventEntry._ID, SportswayContract.EventEntry.COLUMN_EVENT_NAME,
                        SportswayContract.EventEntry.COLUMN_EVENT_LOCATION, SportswayContract.EventEntry.COLUMN_EVENT_DATE,
                        SportswayContract.EventEntry.COLUMN_START_TIME},
                null, null, null, null, null);

        int _id = mCursor.getColumnIndex(SportswayContract.EventEntry._ID);
        int event_name = mCursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_EVENT_NAME);
        int loc = mCursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_EVENT_LOCATION);
        int date = mCursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_EVENT_DATE);
        int time = mCursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_START_TIME);
        while(mCursor.moveToNext()) {
            Event event = new Event();
            event.set_id(mCursor.getInt(_id));
            event.setEvent_name(mCursor.getString(event_name));
            event.setEvent_location(mCursor.getString(loc));
            event.setEvent_date(mCursor.getString(date));
            event.setEvent_start_time(mCursor.getString(time));
            this.mEvents.add(event);
        }
        mCursor.close();

    }

    public void configureRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) this.root.findViewById(R.id.list_of_events);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        RecyclerView.Adapter mAdapter = new MyAdapter(this.mEvents);
        mRecyclerView.setAdapter(mAdapter);
    }
}
