package com.example.android.sportsway;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pools;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.sportsway.Model.Event;
import com.example.android.sportsway.Utility.XmlParser;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements LocationListener {

    private double mLongitude;
    private double mLatitude;
    private ArrayList<Event> mEvents = new ArrayList<Event>();
    private View root;
    private RequestQueue mQueue;
    private LocationManager mLocationManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.root = inflater.inflate(R.layout.fragment_home, container, false);
        this.mQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        this.mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        getEvents();

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mQueue.cancelAll(new RequestQueue.RequestFilter() {
                    @Override
                    public boolean apply(Request<?> request) {
                        return true;
                    }
                });
                getEvents();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;
    }

    public void getEvents() {
        if (ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Location permission not set");
        }
        Location mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (mLocation != null) {
            mLongitude = mLocation.getLongitude();
            mLatitude = mLocation.getLatitude();
            String url = "http://api.eventful.com/rest/events/search?app_key=dw6DGw5d7Z2qfSCD&where="+mLatitude+","+mLongitude+"&within=25";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    System.out.println("Now parsing XML response");
                    XmlParser parser = new XmlParser(response);
                    mEvents = parser.getEvents();
                    configureRecyclerView();
                    System.out.println("Recycler view setup complete");
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error);
                }
            });
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            mQueue.add(stringRequest);
        }
    }

    public void configureRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) this.root.findViewById(R.id.list_of_events);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        RecyclerView.Adapter mAdapter = new MyAdapter(mEvents);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("Location", s);
    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d("Location", s);
    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d("Location", s);
    }
}
