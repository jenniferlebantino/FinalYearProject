package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.adapters.ContactAdapter;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.entities.Trip;
import com.example.finalyearproject.viewModel.ContactViewModel;
import com.example.finalyearproject.viewModel.TripViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ContactsFragment extends Fragment {
    public static final int ADD_CONTACT_REQUEST = 1;
    private ContactViewModel contactViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        FloatingActionButton addContactBtn = v.findViewById(R.id.contacts_addBtn);
        addContactBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                launchAddContact();
            }
        });

        RecyclerView recyclerView = v.findViewById(R.id.contacts_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setHasFixedSize(true);

        final ContactAdapter adapter = new ContactAdapter();
        recyclerView.setAdapter(adapter);

        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        final Observer<List<Contact>> observer = new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        };

        contactViewModel.getAllContacts().observe(getViewLifecycleOwner(), observer);
        return v;
    }

    private void launchAddContact() {
        Intent launchAddContactsActivity = new Intent(getActivity(), AddContactActivity.class);
        startActivityForResult(launchAddContactsActivity, ADD_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_CONTACT_REQUEST && resultCode == RESULT_OK) {
            String firstName = data.getStringExtra(AddContactActivity.EXTRA_FIRSTNAME);
            String lastName = data.getStringExtra(AddContactActivity.EXTRA_LASTNAME);
            String emailAddress = data.getStringExtra(AddContactActivity.EXTRA_EMAILADDRESS);
            String phoneNumber = data.getStringExtra(AddContactActivity.EXTRA_PHONENUMBER);
            String contactImageUrl = data.getStringExtra(AddContactActivity.EXTRA_IMAGEURL);

            Contact contact = new Contact(firstName, lastName, emailAddress, phoneNumber, contactImageUrl);
            contactViewModel.insert(contact);
            Toast.makeText(getActivity(), "Contact Saved", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Unable to Save", Toast.LENGTH_SHORT).show();
        }

    }
}
