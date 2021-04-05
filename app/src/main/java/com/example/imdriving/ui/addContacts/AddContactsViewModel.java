package com.example.imdriving.ui.addContacts;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imdriving.DBHelper;

public class AddContactsViewModel extends ViewModel {

    private MutableLiveData<String> mText;
    private Context mContext;
    private DBHelper helper;
    public AddContactsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public void setContext(Context context){
        mContext = context;
        helper = new DBHelper(mContext, "App.db", null, 1);
    }

    public LiveData<String> getText() {
        return mText;
    }
    public DBHelper getHelper(){
        return helper;
    }
}