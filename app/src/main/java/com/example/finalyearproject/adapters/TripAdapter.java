package com.example.finalyearproject.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.R;
import com.example.finalyearproject.entities.Trip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {

    private Context context;
    private List<Trip> trips = new ArrayList<>();

    @NonNull
    @Override
    public TripHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.trip_item, parent, false);

        return new TripHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TripHolder holder, int position) {
        //Java obj into view for holder
        Trip currentTrip = trips.get(position);
        holder.title.setText(currentTrip.getTitle());
        holder.description.setText(currentTrip.getDescription());
        String imageUrl = trips.get(position).getImageUrl();
        if(imageUrl.equals("")) {
            holder.imageView.setImageResource(R.drawable.im_no_image);
            return;
        }
        Picasso.get().load(imageUrl).into(holder.imageView);
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
        private ImageView imageView;

        public TripHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.trip_title);
            description = itemView.findViewById(R.id.trip_desc);
            imageView = itemView.findViewById(R.id.trips_tripImage);
        }
    }
}
