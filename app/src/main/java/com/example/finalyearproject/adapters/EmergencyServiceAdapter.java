package com.example.finalyearproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.R;
import com.example.finalyearproject.entities.EmergencyServices;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.w3c.dom.Text;

public class EmergencyServiceAdapter extends FirestoreRecyclerAdapter<EmergencyServices, EmergencyServiceAdapter.EmergencyServiceHolder> {

    public EmergencyServiceAdapter(@NonNull FirestoreRecyclerOptions<EmergencyServices> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull EmergencyServiceHolder emergencyServiceHolder, int i, @NonNull EmergencyServices emergencyServices) {
        emergencyServiceHolder.countryTextView.setText(emergencyServices.getCountry());
        emergencyServiceHolder.policeTextView.setText(emergencyServices.getPolice());
        emergencyServiceHolder.ambulanceTextView.setText(emergencyServices.getAmbulance());
        emergencyServiceHolder.fireTextView.setText(emergencyServices.getFire());
        emergencyServiceHolder.notesTextView.setText(emergencyServices.getNotes());
    }

    @NonNull
    @Override
    public EmergencyServiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false);
        return new EmergencyServiceHolder(v);
    }

    class EmergencyServiceHolder extends RecyclerView.ViewHolder {
        TextView countryTextView;
        TextView policeTextView;
        TextView ambulanceTextView;
        TextView fireTextView;
        TextView notesTextView;

        public EmergencyServiceHolder(@NonNull View itemView) {
            super(itemView);
            countryTextView = itemView.findViewById(R.id.country_name);
            policeTextView = itemView.findViewById(R.id.police);
            ambulanceTextView = itemView.findViewById(R.id.ambulance);
            fireTextView = itemView.findViewById(R.id.fire);
            notesTextView = itemView.findViewById(R.id.notes);
        }
    }
}


