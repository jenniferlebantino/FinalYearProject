package com.example.finalyearproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.R;
import com.example.finalyearproject.entities.Trip;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {

    private List<Trip> trips = new ArrayList<>();

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trip_item, parent, false);

        return new TripHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        //Java obj into view for holder
        Trip currentTrip = trips.get(position);
        holder.title.setText(currentTrip.getTitle());
        holder.description.setText(currentTrip.getDescription());
        //int to string String.valueOf
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    class TripHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView description;

        public TripHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trip_title);
            description = itemView.findViewById(R.id.trip_desc);
        }
    }
}
