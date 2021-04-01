package com.example.imdriving;

import android.Manifest;
import android.content.BroadcastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Build;

import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import java.lang.reflect.Method;

public class IncomingCallRejecter extends BroadcastReceiver {


    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onReceive(Context context, Intent intent) {

        TelecomManager tm = (TelecomManager) context.getApplicationContext().getSystemService(Context.TELECOM_SERVICE);
        AppInAction action = new AppInAction();
        if (tm != null && action.getIsDriving()) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                if (ActivityCompat.checkSelfPermission(context.getApplicationContext(), Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                boolean success = tm.endCall();

            }
        }
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        if(incomingNumber!=null && !incomingNumber.equals("") ) {
            Log.d("Incoming number", "Found number "+incomingNumber);
            action.setPhoneNumber(incomingNumber);
        }
        Log.d("Incomming Number", "Number is ," + incomingNumber);
    }




}

