package com.example.android.sportsway;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.sportsway.Model.Event;
import com.example.android.sportsway.Model.EventOnline;

import java.util.ArrayList;

public class ApiEventsAdapter extends RecyclerView.Adapter<ApiEventsAdapter.ViewHolder> {
    private ArrayList<EventOnline> mDataset;

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
            tv1 = (TextView) v.findViewById(R.id.online_event_title_text);
            tv2 = (TextView) v.findViewById(R.id.online_event_city_text);
            tv3 = (TextView) v.findViewById(R.id.online_event_time_text);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ApiEventsAdapter(ArrayList<EventOnline> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ApiEventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_list_of_events, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.tv1.setText(mDataset.get(position).getTitle());
        holder.tv2.setText("City: " + mDataset.get(position).getCity_name());
        holder.tv3.setText("Time: " + mDataset.get(position).getStart_time());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}