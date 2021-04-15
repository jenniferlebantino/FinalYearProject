package com.example.finalyearproject.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    private List<Integer> selectedContacts = new ArrayList<>();
    private boolean checkbox;
    private OnContactClickListener listener;

    public ContactAdapter(boolean pCheckbox) {
        checkbox = pCheckbox;
    }

    public ContactAdapter() {
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View itemView;
        final Context context = parent.getContext();

        itemView = LayoutInflater.from(context)
                .inflate(R.layout.contact_item, parent, false);

        return new ContactHolder(itemView);
    }

    public List<Integer> getSelectedContacts() {
        return selectedContacts;
    }

    @Override
    public void onBindViewHolder(@NonNull final ContactHolder holder, int position) {
        final Contact contact = contacts.get(position);
        holder.name.setText(contact.getFirstName() + " " + contact.getLastName());
        String imageUrl = contact.getContactImageUrl();
        if (imageUrl.equals("")) {
            holder.contactImageView.setImageResource(R.drawable.im_no_image);
        } else {
            Picasso.get().load(imageUrl).fit().into(holder.contactImageView);
        }

        if (checkbox) {
            holder.contactCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.contactCheckBox.isChecked()) {
                        selectedContacts.add(contact.getContactId());
                    } else {
                        selectedContacts.remove(contact.getContactId() - 1);
                    }
                }
            });
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

    public List<String> getContactEmails() {
        List<String> emails = new ArrayList<>();
        for (Contact contact : contacts) {
            emails.add(contact.getEmailAddress());
        }
        return emails;
    }

    class ContactHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private ImageView contactImageView;
        private CheckBox contactCheckBox;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.contacts_name);
            contactImageView = itemView.findViewById(R.id.contacts_contactImage);

            if (checkbox) {
                contactCheckBox = itemView.findViewById(R.id.contacts_checkBox);
                contactCheckBox.setVisibility(CheckBox.VISIBLE);
            } else {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION)
                            listener.onContactClick(contacts.get(position));
                    }
                });
            }
        }
    }

    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    public void setOnContactClickListener(OnContactClickListener listener) {
        this.listener = listener;
    }
}
