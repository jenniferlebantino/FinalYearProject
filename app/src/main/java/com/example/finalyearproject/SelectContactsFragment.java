package com.example.finalyearproject;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalyearproject.adapters.ContactAdapter;
import com.example.finalyearproject.entities.Contact;
import com.example.finalyearproject.viewModel.ContactViewModel;

import java.util.List;

public class SelectContactsFragment extends Fragment {
    private ContactViewModel contactVM;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_contacts, container, false);

        RecyclerView rv = view.findViewById(R.id.contacts_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setHasFixedSize(true);

        final ContactAdapter adapter = new ContactAdapter(false);
        rv.setAdapter(adapter);

        contactVM = new ViewModelProvider(requireActivity()).get(ContactViewModel.class);
        final Observer<List<Contact>> observer = new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContacts(contacts);
            }
        };

        contactVM.getAllContacts().observe(getViewLifecycleOwner(), observer);
        return view;
    }

}