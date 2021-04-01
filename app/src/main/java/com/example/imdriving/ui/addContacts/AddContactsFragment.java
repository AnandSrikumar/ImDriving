package com.example.imdriving.ui.addContacts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.imdriving.ContactHelper;
import com.example.imdriving.R;

public class AddContactsFragment extends Fragment {

    private AddContactsViewModel addContactsViewModel;
    private Context mContext;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext().getApplicationContext();
        addContactsViewModel =
                new ViewModelProvider(this).get(AddContactsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_add_trusted_contacts, container, false);

        addContactsViewModel.setContext(mContext);
        fragmentHandler(root);
        return root;
    }
    private void fragmentHandler(View root){
        Button importContacts = root.findViewById(R.id.import_contact);
        ImageButton addContact = root.findViewById(R.id.add_phone_no);
        final EditText phoneNo = root.findViewById(R.id.enter_phone_no);
        importContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, 7);
            }
        });
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultIntent) {
        super.onActivityResult(requestCode , resultCode, resultIntent);
        ContactHelper.pickAContact(requestCode, resultCode, resultIntent, this, mContext);
    }

}