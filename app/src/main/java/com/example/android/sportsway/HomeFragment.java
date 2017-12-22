package com.example.android.sportsway;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.sportsway.Model.EventOnline;
import com.example.android.sportsway.Utility.XmlParser;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private View root;
    private ArrayList<EventOnline> mEvents = new ArrayList<EventOnline>();
    private double mLongitude, mLatitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_home, container, false);
        System.out.println("DEBUG: Going in fetch events");
        LocationManager manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Snackbar.make(this.root, "Location permissions not set", Snackbar.LENGTH_SHORT).show();
        }
        Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            System.out.println("Couldn't get location");
        }
        else {
            mLongitude = location.getLongitude();
            mLatitude = location.getLatitude();
        }
        fetchEvents();
        return this.root;
    }

    public void fetchEvents() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://api.eventful.com/rest/events/search?app_key=dw6DGw5d7Z2qfSCD&where="
                + mLatitude + "," + mLongitude + "&within=25";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                XmlParser parser = new XmlParser(response);
                mEvents = parser.getEvents();
                configureRecyclerView();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "" + error);
            }
        });
        queue.add(request);
    }

    public void configureRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) this.root.findViewById(R.id.api_events);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        RecyclerView.Adapter mAdapter = new ApiEventsAdapter(this.mEvents);
        mRecyclerView.setAdapter(mAdapter);
    }

}
