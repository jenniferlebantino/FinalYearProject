package com.example.finalyearproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.R;
import com.example.finalyearproject.ViewTripFragment;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.entities.Trip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {
    private List<Contact> contacts = new ArrayList<>();
    private boolean smallContactItem;
    private OnContactClickListener listener;

    public ContactAdapter(boolean pSmallContactItem) {
        smallContactItem = pSmallContactItem;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View itemView;
        final Context context = parent.getContext();

        if(!smallContactItem)
        {
            itemView = LayoutInflater.from(context)
                    .inflate(R.layout.contact_item, parent, false);
        }
        else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.contact_item_small, parent, false);
        }

        return new ContactHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        Contact contact = contacts.get(position);
        holder.name.setText(contact.getFirstName() + " " + contact.getLastName());
        String imageUrl = contact.getContactImageUrl();
        if(imageUrl.equals(""))
        {
            holder.contactImageView.setImageResource(R.drawable.im_no_image);
            return;
        }
        else {
            Picasso.get().load(imageUrl).fit().into(holder.contactImageView);
        }

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }

    public Contact getContactAt(int position) {
        return contacts.get(position);
    }

    public Contact getContactById(int contactId) {
        for (Contact contact : contacts) {
            if (contact.getContactId() == contactId) {
                return contact;
            }
        }
        return null;
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView contactImageView;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contacts_name);
            contactImageView = itemView.findViewById(R.id.contacts_contactImage);
            if(smallContactItem)
            {
                Button closeBtn = itemView.findViewById(R.id.contact_item_small_removeBtn);
                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO: Remove contact.
                    }
                });
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION)
                    listener.onContactClick(contacts.get(position));
                }
            });
        }
    }

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    public void setOnContactClickListener(OnContactClickListener listener) {
        this.listener = listener;
    }

}
