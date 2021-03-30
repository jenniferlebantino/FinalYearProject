package com.example.finalyearproject;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalyearproject.dummy.DummyContent.DummyItem;
import com.example.finalyearproject.entities.Contact;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class SelectContactAdapter extends RecyclerView.Adapter<SelectContactAdapter.SelectContactHolder> {

    private List<Contact> allContacts = new ArrayList<>();

    @Override
    public SelectContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new SelectContactHolder(view);
    }

    @Override
    public void onBindViewHolder(final SelectContactHolder holder, int position) {
        Contact currentContact = allContacts.get(position);
        holder.name.setText(currentContact.getFirstName() + " " + currentContact.getLastName());
        String imageUrl = currentContact.getContactImageUrl();
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
        return allContacts.size();
    }

    public void setAllContacts(List<Contact> contacts)  {
        allContacts = contacts;
        notifyDataSetChanged();;
    }

    public class SelectContactHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView contactImageView;

        public SelectContactHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contacts_name);
            contactImageView = itemView.findViewById(R.id.contacts_contactImage);
        }
    }
}