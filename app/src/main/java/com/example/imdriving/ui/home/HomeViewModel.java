package com.example.imdriving.ui.home;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.imdriving.AppInAction;
import com.example.imdriving.DBHelper;
import com.example.imdriving.LocationTrack;
import com.example.imdriving.MainActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class HomeViewModel extends ViewModel{

    private MutableLiveData<String> mText;
    private LocationTrack locationTrack;
    private String TAG = "HomeViewModel";
    private Context context;
    private DBHelper helper;
    public HomeViewModel() {
        mText = new MutableLiveData<>();

    }
    public void setContext(Context context){
        this.context = context;
        helper = new DBHelper(context, "App.db", null, 1);
        helper.addDefaultSms();
    }
    public LiveData<String> getText(){
        return mText;
    }

    public void refreshLocation(Context context, AppInAction action){
        locationTrack = new LocationTrack(context);
        if (locationTrack.canGetLocation()) {


            double longitude = locationTrack.getLongitude();
            double latitude = locationTrack.getLatitude();
            action.setLatitude(latitude);
            action.setLongitude(longitude);
            String address = addFullAddress(latitude, longitude, context);
            action.setAddressLine(address);
            //Toast.makeText(context, "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Please turn on location service",
                    Toast.LENGTH_LONG).show();

        }
    }

    private String addFullAddress(double latitude, double longitude, Context context){
        try {
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());

            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            Log.d(TAG, address);
            return address;

        }catch(Exception e){
            Log.d(TAG, "Unable to get location");
            e.printStackTrace();
            return "";
        }
    }

    public String strTimeRepr(int seconds){
        String time= "";
        int h = seconds/3600;
        int m = seconds%3600;
        m = m/60;
        time = h+" hrs: "+m+" mins";
        return time;
    }
    public String getSmsBody(String ph, String location, String time){

        List<String> trusted = helper.getTrustedContacts();

        String smsBody = "";
        smsBody += helper.getSMS();
        if(ph.startsWith("+91"))
            ph = ph.substring(3);
        Log.d(TAG,"SMS sending to Phone number: "+ph);
        Log.d(TAG, trusted+" are trusted");
        if(trusted.contains(ph)){
            smsBody += "Im currently at "+location;
            Log.d(TAG, "Present in trusted contacts");
        }
        smsBody += " .Approximately for "+time+" I will be driving..";
        return smsBody;
    }

    public String getTodaytime(){
        Calendar now = Calendar.getInstance();

        SimpleDateFormat sd = new SimpleDateFormat(
                "MM/dd ' ' HH:mm:ss");

        return sd.format(now.getTime());
    }
    public List<String> getContactsList(){
        return helper.getTrustedContacts();
    }
    public List<String> getAllTables(){return helper.getAllTableNames();}
    public DBHelper getHelper(){return helper;}
}

