package com.example.imdriving;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String SMS_BODY = "sms";
    public static final String CONTACT_NO_COLUMN= "contactNo text";
    public static final String CONTACT_NAME_COLUMN = "name text";

    SQLiteDatabase db;
    private String TAG="DPHelper";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+SMS_BODY+"(sms varchar2(60))");
        String query = "create table "+CONTACTS_TABLE_NAME+"("+CONTACT_NO_COLUMN+", "+CONTACT_NAME_COLUMN+")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+SMS_BODY);
        db.execSQL("DROP TABLE IF EXISTS "+CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    public boolean addDefaultSms(){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String sms = "Hi there! I'm driving right now. Call you later";
        values.put("sms",sms);
        long err = db.insert(SMS_BODY, null, values);
        Log.d(TAG,"In addDefault: "+err);
        db.close();
        return err != -1;
    }
    public void updateSMS(String newSms){
        db.execSQL("UPDATE "+SMS_BODY+" SET sms="+"'"+newSms+"'");
    }
    public String getSMS(){
        db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from "+SMS_BODY, null);
        r.moveToFirst();
        Log.d(TAG,"total SMS: "+r.getCount());
        db.close();
        return r.getString(r.getColumnIndex("sms"));
    }

    public boolean addContacts(String phoneNo, String name){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("contactNo", phoneNo);
        values.put("name", name );
        long err = db.insert(CONTACTS_TABLE_NAME, null, values);
        Log.d(TAG, err+"....");
        db.close();
        return err != -1;
    }

    public List<String> getTrustedContacts(){
        List<String> trusted = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from "+CONTACTS_TABLE_NAME, null);
        r.moveToFirst();
        Log.d(TAG,"got records "+r.getCount());
        while(!r.isAfterLast()){
            Log.d(TAG," Inside while loop....");
            trusted.add(r.getString(r.getColumnIndex("contactNo")).
                    replaceAll(" ","").trim());
            r.moveToNext();
        }
        db.close();
        return trusted;
    }

    public String[][] getNameAndContact(){
        String[][] rows = new String[getColumCount()][2];
        db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from "+CONTACTS_TABLE_NAME, null);
        r.moveToFirst();

        int i =0;
        while(!r.isAfterLast()){
            String c = r.getString(r.getColumnIndex("contactNo")).
                    replaceAll(" ","").trim();
            String nm = r.getString(r.getColumnIndex("name")).
                    replaceAll(" ","").trim();
            r.moveToNext();
            if(i >= rows.length) continue;
            rows[i][0] = c;
            rows[i][1] = nm;
            i++;
        }

        db.close();
        return rows;
    }

    public int getColumCount(){
        int c = 0;
        db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from "+CONTACTS_TABLE_NAME, null);
        c = r.getCount();
        db.close();

        return c;
    }

    public boolean deleteContact(String name){

        Log.d(TAG, name+" is going to be deleted...");
        return db.delete(CONTACTS_TABLE_NAME, "contactNo" + "='" + name+"'",
                null) > 0;
    }


//    public List<String> getAllTableNames(){
//        ArrayList<String> arrTblNames = new ArrayList<String>();
//
//        db = this.getWritableDatabase();
//        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
//
//        if (c.moveToFirst()) {
//            while ( !c.isAfterLast() ) {
//                arrTblNames.add( c.getString( c.getColumnIndex("name")) );
//                c.moveToNext();
//            }
//        }
//        db.close();
//        return arrTblNames;
//    }
}
