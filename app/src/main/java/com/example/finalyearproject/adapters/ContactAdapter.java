package com.example.finalyearproject.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.R;
import com.example.finalyearproject.entities.Contact;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    private List<Contact> contacts = new ArrayList<>();

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.name.setText(contact.getFirstName().concat(contact.getLastName()));
        String imageUrl = contact.getContactImageUrl();
        if(imageUrl.equals(""))
        {
            holder.contactImageView.setImageResource(R.drawable.im_no_image);
            return;
        }
        Picasso.get().load(imageUrl).into(holder.contactImageView);
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView contactImageView;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contacts_name);
            contactImageView = itemView.findViewById(R.id.contacts_contactImage);
        }
    }



}
