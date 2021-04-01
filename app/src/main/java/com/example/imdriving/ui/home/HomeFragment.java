package com.example.imdriving.ui.home;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import com.example.imdriving.AppInAction;

import com.example.imdriving.ContactHelper;

import com.example.imdriving.R;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private AppInAction action;
    private long refreshLoc = 10000;
    private int timeLeft = 0;
    private final String TAG = "HomeFragment.class";
    private TextView view;
    private TextView appInAction;
    private TextView timeL;
    private EditText hrs;
    private EditText mins;
    private TextView timeDisplay;
    private Context mContext;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mContext = getContext().getApplicationContext();
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        action = new AppInAction();
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        fragmentHandler(root);
        timeHandler();

        return root;
    }

    private void fragmentHandler(View root){
        homeViewModel.setContext(mContext);
        view = root.findViewById(R.id.text_home);
        ImageButton startDriving= root.findViewById(R.id.start_driving);
        ImageButton stopDriving = root.findViewById(R.id.stop_driving);

        LinearLayout viewContact = root.findViewById(R.id.view_trusted_contacts);
        LinearLayout addContacts = root.findViewById(R.id.add_trusted_contacts);

        appInAction = root.findViewById(R.id.app_in_action);
        timeDisplay = root.findViewById(R.id.time_display);

        startDriving.setOnClickListener(this);
        stopDriving.setOnClickListener(this);
        viewContact.setOnClickListener(this);
        addContacts.setOnClickListener(this);

        homeViewModel.refreshLocation(mContext, action);
        view.setText(action.getAddressLine());
        hrs = root.findViewById(R.id.hours);
        mins = root.findViewById(R.id.mins);
        timeL = root.findViewById(R.id.time_left);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.start_driving){
            if(!action.getIsDriving()) {
                action.setIsDriving(true);
                setupAppFunction("App is running, calls will be rejected...");
            }
        }
        if(id == R.id.stop_driving){
            if(action.getIsDriving()){
                action.setIsDriving(false);
                setupAppFunction("");
            }
        }
        if(id == R.id.view_trusted_contacts){
            Toast.makeText(mContext, homeViewModel.getContactsList()+"",
                    Toast.LENGTH_LONG).show();
        }
        if(view.getId() == R.id.add_trusted_contacts){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(intent, 7);
        }
    }
    private void setupAppFunction(String text){
        appInAction.setText(text);
        appInAction.invalidate();
        if(!text.equals("")){
            int set = 0;
            String s1=hrs.getText().toString().trim(), s2=mins.getText().toString().trim();
            Log.d(TAG,s1+ "---->"+s2);
            if(!s1.equals("")){
                if(s1.startsWith("0")) s1 = s1.substring(1);
                timeLeft = Integer.parseInt(s1)*3600;
                set = 1;
            }
            if(!s2.equals("")){
                if(s2.startsWith("0")) s2 = s2.substring(1);
                timeLeft += Integer.parseInt(s2)*60;
                set = 1;
            }
            Log.d(TAG, "####>"+timeLeft);
            if(set == 1)
                action.setTimeLeft(timeLeft);
            hrs.setEnabled(false);
            mins.setEnabled(false);
        }
        else{
            hrs.setText(""); mins.setText("");
            hrs.setEnabled(true);
            mins.setEnabled(true);
            action.setTimeLeft(120);
            timeLeft = 0;
            timeL.setText("");
            timeL.invalidate();
        }

        hrs.invalidate();
        mins.invalidate();
    }
    public void timeHandler(){
        Handler handler = new Handler(); // to update UI  from background //thread.
        Log.d("onRefresh= ", Thread.currentThread() + "");
        handler.post(new Runnable() {
            @Override
            public void run() {
                timeDisplay.setText(homeViewModel.getTodaytime());
                timeDisplay.invalidate();
                if(refreshLoc < 0){
                    Log.d(TAG,"location refreshed...");
                    homeViewModel.refreshLocation(mContext,
                            action);
                    view.setText(action.getAddressLine());
                    view.invalidate();
                    refreshLoc = 10000;
                }
                if(action.getIsDriving()) {
                    timeLeft = action.getTimeLeft();
                    String tRepr = homeViewModel.strTimeRepr(timeLeft);
                    timeL.setText(tRepr);
                    timeL.invalidate();

                    action.setTimeLeft(timeLeft-1);
                    String ph = action.getPhoneNumber();
                    if(ph != null && !ph.equals("")){
                        String smsB = homeViewModel.getSmsBody(ph, action.getAddressLine(), tRepr);
                        Log.d(TAG,smsB+"--->will be sent to user...."+ph);
                        action.setPhoneNumber("");
                        smsManager(ph, smsB);
                    }
                    if(timeLeft <= 0){
                        action.setIsDriving(false);
                        setupAppFunction("");
                    }

                }
                refreshLoc -= 1000;
                handler.postDelayed(this, 1000);
            }
        });

    }

    private void smsManager(String phoneNo, String smsBody){
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNo, null, smsBody,
                    null, null);

        } catch (Exception e) {
            Log.d(TAG, "SMS Was not sent...");
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent resultIntent) {
        super.onActivityResult(requestCode , resultCode, resultIntent);
        ContactHelper.pickAContact(requestCode, resultCode, resultIntent, this, mContext);
    }
}