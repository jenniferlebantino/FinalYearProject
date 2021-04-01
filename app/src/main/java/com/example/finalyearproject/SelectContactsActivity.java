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

import java.util.List;

public class SelectContactsActivity extends AppCompatActivity {
    public static final String EXTRA_SELECTEDCONTACTS_CONTACTS = "com.example.finalyearproject.EXTRA_SELECTEDCONTACTS";
    private ContactViewModel contactVM;
    private FloatingActionButton saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_contacts);

        RecyclerView rv = findViewById(R.id.selectContacts_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setHasFixedSize(true);

        final ContactAdapter adapter = new ContactAdapter(false, true);
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
                saveSelectedContacts(adapter.getSelectedContacts());
            }
        });
    }

    private void saveSelectedContacts(List<Integer> contactIds) {
        StringBuilder builder = new StringBuilder();
        for(Integer contactId : contactIds) {
            builder.append(contactId + ",");
        }
        String contacts = builder.length() > 0 ? builder.substring(0, builder.length()) : "";

        Intent selectedContacts = new Intent();
        selectedContacts.putExtra(EXTRA_SELECTEDCONTACTS_CONTACTS, contacts);
        setResult(RESULT_OK, selectedContacts);
        finish();
    }
}