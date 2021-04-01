package com.example.imdriving.ui.addContacts;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddContactsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Context mContext;
    public AddContactsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }
    public void setContext(Context context){
        mContext = context;
    }

    public LiveData<String> getText() {
        return mText;
    }
}