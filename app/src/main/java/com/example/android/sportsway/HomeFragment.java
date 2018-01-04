package com.example.android.sportsway;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    private ArrayList<EventOnline> mOnlineEvents = new ArrayList<EventOnline>();
    private float mLatitude, mLongitude;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        mLatitude = preferences.getFloat("latitude", (float) 33.660036);
        mLongitude = preferences.getFloat("longitude", (float) 73.229354);
        fetchOnlineEvents();

        return this.root;
    }

    public void fetchOnlineEvents() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://api.eventful.com/rest/events/search?app_key=dw6DGw5d7Z2qfSCD&where="
                + mLatitude + "," + mLongitude + "&within=25";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                XmlParser parser = new XmlParser(response);
                mOnlineEvents = parser.getEvents();
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
        RecyclerView.Adapter mAdapter = new ApiEventsAdapter(mOnlineEvents);
        mRecyclerView.setAdapter(mAdapter);
    }
}