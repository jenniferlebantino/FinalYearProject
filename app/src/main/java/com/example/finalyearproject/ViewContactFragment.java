package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalyearproject.adapters.ContactAdapter;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.viewModel.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ViewContactFragment extends Fragment {
    public static final int EDIT_CONTACT_REQUEST = 1;

    private ContactAdapter adapter;
    private ContactViewModel viewModel;
    private Contact contact;
    private int contactId;
    private ImageView contactImageView;
    private TextView nameTxtView;
    private TextView emailTxtView;
    private TextView phoneNumberTxtView;
    private FloatingActionButton editBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_view_contact, container, false);

        adapter = new ContactAdapter(false, false);

        contactImageView = v.findViewById(R.id.viewContact_contactImage);
        nameTxtView =  v.findViewById(R.id.viewContact_contactName);
        emailTxtView =  v.findViewById(R.id.viewContact_contactEmail);
        phoneNumberTxtView =  v.findViewById(R.id.viewContact_phoneNumber);
        editBtn = v.findViewById(R.id.viewContact_editBtn);

/*
        viewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        final Observer<List<Contact>> observer = new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        };

        viewModel.getAllContacts().observe(getViewLifecycleOwner(), observer);
*/

        Bundle b = getArguments();
        contactId = b.getInt("contactId");
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contact = adapter.getContactById(contactId);
                launchEditContact();
            }
        });

        nameTxtView.setText(b.getString("name"));
        emailTxtView.setText(b.getString("emailAddress"));
        phoneNumberTxtView.setText(b.getString("phone"));

        loadImage(b.getString("imageUrl"));

        return v;
    }

    private void launchEditContact() {
        Intent intent = new Intent(getActivity(), AddEditContactActivity.class);
        intent.putExtra(AddEditContactActivity.EXTRA_CONTACTID, contact.getContactId());
        intent.putExtra(AddEditContactActivity.EXTRA_IMAGEURL, contact.getContactImageUrl());
        intent.putExtra(AddEditContactActivity.EXTRA_FIRSTNAME, contact.getFirstName());
        intent.putExtra(AddEditContactActivity.EXTRA_LASTNAME, contact.getLastName());
        intent.putExtra(AddEditContactActivity.EXTRA_EMAILADDRESS, contact.getEmailAddress());
        intent.putExtra(AddEditContactActivity.EXTRA_PHONENUMBER, contact.getPhoneNumber());
        startActivityForResult(intent, EDIT_CONTACT_REQUEST);
    }

    private void loadImage(String imageUrl) {
        if(imageUrl.equals(""))
        {
            contactImageView.setImageResource(R.drawable.im_no_image);
        }
        else {
            Picasso.get().load(imageUrl).fit().into(contactImageView);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == EDIT_CONTACT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditContactActivity.EXTRA_CONTACTID, -1);
            if (id == -1 ) {
                Toast.makeText(getActivity(), "Note couldn't be updated.", Toast.LENGTH_SHORT).show();
                return;
            }

            String firstName = data.getStringExtra(AddEditContactActivity.EXTRA_FIRSTNAME);
            String lastName = data.getStringExtra(AddEditContactActivity.EXTRA_LASTNAME);
            String emailAddress = data.getStringExtra(AddEditContactActivity.EXTRA_EMAILADDRESS);
            String phoneNumber = data.getStringExtra(AddEditContactActivity.EXTRA_PHONENUMBER);
            String contactImageUrl = data.getStringExtra(AddEditContactActivity.EXTRA_IMAGEURL);
            Contact contact = new Contact(firstName, lastName, emailAddress, phoneNumber, contactImageUrl);
            contact.setContactId(id);
            viewModel.update(contact);
            Toast.makeText(getActivity(), "Note has been updated.", Toast.LENGTH_SHORT).show();
        }
    }
}