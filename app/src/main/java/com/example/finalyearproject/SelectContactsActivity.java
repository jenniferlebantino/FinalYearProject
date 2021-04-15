package com.example.finalyearproject;

import android.content.Intent;
import android.os.Bundle;

import com.example.finalyearproject.adapters.ContactAdapter;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.viewModel.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.List;

public class SelectContactsActivity extends AppCompatActivity {
    public static final String EXTRA_SELECTEDCONTACTS_CONTACTS = "com.example.finalyearproject.EXTRA_SELECTEDCONTACTS_CONTACTS";
    public static final String EXTRA_SELECTCONTACTS_NAMES = "com.example.finalyearproject.EXTRA_SELECTEDCONTACTS_NAMES";

    private FloatingActionButton saveBtn;
    private TextView noContactsTextView;

    private ContactViewModel contactVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        RecyclerView rv = findViewById(R.id.selectContacts_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        final ContactAdapter adapter = new ContactAdapter(true);
        rv.setAdapter(adapter);
        contactVM = new ViewModelProvider(SelectContactsActivity.this).get(ContactViewModel.class);
        final Observer<List<Contact>> observer = new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        };
        contactVM.getAllContacts().observe(SelectContactsActivity.this, observer);

        saveBtn = findViewById(R.id.selectContacts_addBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder names = new StringBuilder();
                for (Integer contactId : adapter.getSelectedContacts()) {
                    names.append(adapter.getContactById(contactId).getFirstName()+ " " + adapter.getContactById(contactId).getLastName() + ",");
                }
                saveSelectedContacts(adapter.getSelectedContacts(), names.toString());
            }
        });
    }

    private void saveSelectedContacts(List<Integer> contactIds, String contactNames) {
        StringBuilder contactIdString = new StringBuilder();

        for(Integer contactId : contactIds) {
            contactIdString.append(contactId + ",");
        }
        String contacts = contactIdString.length() > 0 ? contactIdString.substring(0, contactIdString.length()) : "";

        Intent selectedContacts = new Intent();
        selectedContacts.putExtra(EXTRA_SELECTEDCONTACTS_CONTACTS, contacts);
        selectedContacts.putExtra(EXTRA_SELECTCONTACTS_NAMES, contactNames);
        setResult(RESULT_OK, selectedContacts);
        finish();
    }
}