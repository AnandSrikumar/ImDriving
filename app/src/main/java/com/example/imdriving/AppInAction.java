package com.example.imdriving;

public class AppInAction {
    private static boolean isDriving = false;
    private String latitude = "";
    private String longitude = "";
    private int timeLeft = 120;
    private String addressLine = "";
    private static String phoneNumber="";
    private static String dets[][];

    public void setIsDriving(boolean isDriving){
        this.isDriving = isDriving;
    }

    public boolean getIsDriving(){
        return isDriving;
    }

    public void setLatitude(double latitude){
        this.latitude = latitude+"";
    }
    public String getLatitude(){
        return latitude;
    }

    public void setLongitude(double longitude){
        this.longitude = longitude+"";
    }
    public String getLongitude(){
        return longitude;
    }

    public void setTimeLeft(int timeLeft){
        this.timeLeft = timeLeft;
    }
    public int getTimeLeft(){
        return timeLeft;
    }

    public String getAddressLine(){
        return addressLine;
    }

    public void setAddressLine(String addressLine){
        this.addressLine = addressLine;
    }

    public void setPhoneNumber(String phoneNumber){ this.phoneNumber = phoneNumber;}
    public String getPhoneNumber(){return phoneNumber;}

    public void setDets(String[][] dets){this.dets = dets;}
    public static String[][] getDets(){return dets;}

}
