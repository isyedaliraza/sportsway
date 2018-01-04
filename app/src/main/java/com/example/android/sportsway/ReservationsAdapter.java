package com.example.android.sportsway;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sportsway.Model.EventOnline;
import com.example.android.sportsway.Model.Reservation;
import com.example.android.sportsway.Model.SportswayContract;
import com.example.android.sportsway.Model.SportswayDbHelper;

import java.util.ArrayList;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.ViewHolder> {
    private ArrayList<Reservation> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv1;
        public TextView tv2;
        public TextView tv3;
        public ViewHolder(View v) {
            super(v);
            tv1 = (TextView) v.findViewById(R.id.reservation_ticket_id);
            tv2 = (TextView) v.findViewById(R.id.reservation_event_name);
            tv3 = (TextView) v.findViewById(R.id.reservation_event_start_time);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReservationsAdapter(Context context, ArrayList<Reservation> myDataset) {
        mDataset = myDataset;
        mContext = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReservationsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_of_reservations, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv1.setText("Ticket ID: " + mDataset.get(position).getTicket_id());
        SportswayDbHelper dbHelper = new SportswayDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(SportswayContract.EventEntry.TABLE_NAME, new String[]{SportswayContract.EventEntry.COLUMN_EVENT_NAME,
                SportswayContract.EventEntry.COLUMN_START_TIME}, SportswayContract.EventEntry._ID + "=?",
                new String[]{Integer.toString(mDataset.get(position).getEvent_id())}, null, null, null);
        cursor.moveToNext();
        holder.tv2.setText("Event: " + cursor.getString(cursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_EVENT_NAME)));
        holder.tv3.setText("Start Time: " + cursor.getString(cursor.getColumnIndex(SportswayContract.EventEntry.COLUMN_START_TIME)));
        cursor.close();
        db.close();
        dbHelper.close();
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}