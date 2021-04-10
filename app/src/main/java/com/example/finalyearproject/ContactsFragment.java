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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalyearproject.adapters.ContactAdapter;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.viewModel.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class ContactsFragment extends Fragment {
    public static final int ADD_CONTACT_REQUEST = 1;
    public static final int EDIT_CONTACT_REQUEST = 2;

    private FirebaseAuth authenticate;

    private ContactViewModel contactViewModel;
    private FloatingActionButton addContactBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contacts, container, false);

        authenticate = FirebaseAuth.getInstance();

        addContactBtn = v.findViewById(R.id.contacts_addBtn);
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

        final ContactAdapter adapter = new ContactAdapter(false, false);
/*
        adapter.setOnContactClickListener(new ContactAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                Intent intent = new Intent(getContext(), AddEditContactActivity.class);
                intent.putExtra(AddEditContactActivity.EXTRA_CONTACTID, contact.getContactId());
                intent.putExtra(AddEditContactActivity.EXTRA_IMAGEURL, contact.getContactImageUrl());
                intent.putExtra(AddEditContactActivity.EXTRA_FIRSTNAME, contact.getFirstName());
                intent.putExtra(AddEditContactActivity.EXTRA_LASTNAME, contact.getLastName());
                intent.putExtra(AddEditContactActivity.EXTRA_EMAILADDRESS, contact.getEmailAddress());
                intent.putExtra(AddEditContactActivity.EXTRA_PHONENUMBER, contact.getPhoneNumber());
                startActivityForResult(intent, EDIT_CONTACT_REQUEST);
            }
        });
*/
        recyclerView.setAdapter(adapter);

        contactViewModel = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);

        final Observer<List<Contact>> observer = new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        };

        contactViewModel.getAllContacts().observe(getViewLifecycleOwner(), observer);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                contactViewModel.delete(adapter.getContactAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Contact successfully deleted.", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Intent intent = new Intent(getContext(), AddEditContactActivity.class);
                Contact contact = adapter.getContactAt(viewHolder.getAdapterPosition());
                intent.putExtra(AddEditContactActivity.EXTRA_CONTACTID, contact.getContactId());
                intent.putExtra(AddEditContactActivity.EXTRA_IMAGEURL, contact.getContactImageUrl());
                intent.putExtra(AddEditContactActivity.EXTRA_FIRSTNAME, contact.getFirstName());
                intent.putExtra(AddEditContactActivity.EXTRA_LASTNAME, contact.getLastName());
                intent.putExtra(AddEditContactActivity.EXTRA_EMAILADDRESS, contact.getEmailAddress());
                intent.putExtra(AddEditContactActivity.EXTRA_PHONENUMBER, contact.getPhoneNumber());
                startActivityForResult(intent, EDIT_CONTACT_REQUEST);
            }
        }).attachToRecyclerView(recyclerView);

        return v;
    }

    private void launchAddContact() {
        Intent launchAddContactsActivity = new Intent(getActivity(), AddEditContactActivity.class);
        startActivityForResult(launchAddContactsActivity, ADD_CONTACT_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_CONTACT_REQUEST && resultCode == RESULT_OK) {
            String firstName = data.getStringExtra(AddEditContactActivity.EXTRA_FIRSTNAME);
            String lastName = data.getStringExtra(AddEditContactActivity.EXTRA_LASTNAME);
            String emailAddress = data.getStringExtra(AddEditContactActivity.EXTRA_EMAILADDRESS);
            String phoneNumber = data.getStringExtra(AddEditContactActivity.EXTRA_PHONENUMBER);
            String contactImageUrl = data.getStringExtra(AddEditContactActivity.EXTRA_IMAGEURL);

            Contact contact = new Contact(firstName, lastName, emailAddress, phoneNumber, contactImageUrl);
            contact.setUserId(authenticate.getCurrentUser().getUid());
            contactViewModel.insert(contact);
            Toast.makeText(getActivity(), "Contact Saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_CONTACT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditContactActivity.EXTRA_CONTACTID, -1);
            if (id == -1) {
                Toast.makeText(getActivity(), "Contact couldn't be updated.", Toast.LENGTH_SHORT).show();
                return;
            }
            String firstName = data.getStringExtra(AddEditContactActivity.EXTRA_FIRSTNAME);
            String lastName = data.getStringExtra(AddEditContactActivity.EXTRA_LASTNAME);
            String emailAddress = data.getStringExtra(AddEditContactActivity.EXTRA_EMAILADDRESS);
            String phoneNumber = data.getStringExtra(AddEditContactActivity.EXTRA_PHONENUMBER);
            String contactImageUrl = data.getStringExtra(AddEditContactActivity.EXTRA_IMAGEURL);
            Contact contact = new Contact(firstName, lastName, emailAddress, phoneNumber, contactImageUrl);
            contact.setContactId(id);
            contactViewModel.update(contact);
            Toast.makeText(getActivity(), "Contact has been updated.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "Unable to save contact.", Toast.LENGTH_SHORT).show();
        }

    }
}