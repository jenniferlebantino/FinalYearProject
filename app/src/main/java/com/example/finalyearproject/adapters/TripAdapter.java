package com.example.finalyearproject.adapters;

import android.content.Context;
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
    private OnItemClickListener clickListener;

    public interface OnItemClickListener {
        void onItemClick(Trip trip);
    }

    public void setOnItemClickListener(OnItemClickListener pClickListener) {
        clickListener = pClickListener;
    }

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
        String x = currentTrip.getDescription();
        holder.description.setText(currentTrip.getDescription());
        String imageUrl = trips.get(position).getImageUrl();
        if (imageUrl.equals("")) {
            holder.imageView.setImageResource(R.drawable.im_no_image);
            return;
        }

        Picasso.get().load(imageUrl).fit().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    public void setTrips(List<Trip> trips) {
        this.trips = trips;
        notifyDataSetChanged();
    }

    public Trip getTripAt(int position) {
        return trips.get(position);
    }

    public Trip getTripById(int tripId) {
        for (Trip trip : trips) {
            if (trip.getTripId() == tripId) {
                return trip;
            }
        }
        return null;
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (clickListener != null && position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(trips.get(position));
                    }
                }
            });
        }
    }
}
