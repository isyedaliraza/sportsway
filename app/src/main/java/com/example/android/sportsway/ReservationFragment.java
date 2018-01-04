package com.example.android.sportsway;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.android.sportsway.Model.Reservation;
import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;

import java.util.ArrayList;

public class ReservationFragment extends Fragment {

    private ArrayList<Reservation> mReservations = new ArrayList<Reservation>();
    private View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_reservation, container, false);
        fetchReservations();
        configureRecyclerView();
        return this.root;
    }

    public void fetchReservations() {
        SharedPreferences preferences = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        int userId = preferences.getInt("userId", 0);
        SportswayDbHelper dbHelper = new SportswayDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SportswayContract.ReservationEntry.TABLE_NAME, new String[]{SportswayContract.ReservationEntry.COLUMN_TICKET_ID,
                SportswayContract.ReservationEntry.COLUMN_EVENT_ID}, SportswayContract.ReservationEntry.COLUMN_USER_ID+"=?",
                new String[]{Integer.toString(userId)}, null, null, null);
        while (cursor.moveToNext()) {
            Reservation reservation = new Reservation();
            reservation.setTicket_id(cursor.getInt(cursor.getColumnIndex(SportswayContract.ReservationEntry.COLUMN_TICKET_ID)));
            reservation.setEvent_id(cursor.getInt(cursor.getColumnIndex(SportswayContract.ReservationEntry.COLUMN_EVENT_ID)));
            mReservations.add(reservation);
        }
        cursor.close();
        db.close();
        dbHelper.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        mReservations.clear();
        fetchReservations();
        configureRecyclerView();
    }

    public void configureRecyclerView() {
        RecyclerView mRecyclerView = (RecyclerView) this.root.findViewById(R.id.list_of_reservations);
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter
        RecyclerView.Adapter mAdapter = new ReservationsAdapter(getContext(), mReservations);
        mRecyclerView.setAdapter(mAdapter);
    }
}
