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
    public static final String DATABASE_NAME = "App.db";
    public static final String CONTACTS_TABLE_NAME = "contacts";
    public static final String SMS_BODY = "sms";
    SQLiteDatabase db;
    private String TAG="DPHelper";

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+SMS_BODY+"(sms varchar2(60))");
        String query = "Create table "+CONTACTS_TABLE_NAME+"(contactNo text, name text)";
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
        return r.getString(r.getColumnIndex("sms"));
    }

    public boolean addContacts(String phoneNo, String name){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("contactNo", phoneNo);
        values.put("name", name );
        long err = db.insertOrThrow(CONTACTS_TABLE_NAME, null, values);
        Log.d(TAG, err+"....");
        return err != -1;
    }

    public List<String> getTrustedContacts(){
        List<String> trusted = new ArrayList<>();
        db = this.getReadableDatabase();
        Cursor r = db.rawQuery("select * from "+CONTACTS_TABLE_NAME, null);
        r.moveToFirst();
        Log.d(TAG,"got records "+r.getCount());
       /* Log.d("DB"," moved to first: "+
                r.getString(r.getColumnIndex("contacts")));*/
        while(!r.isAfterLast()){
            Log.d(TAG," Inside while loop....");
            trusted.add(r.getString(r.getColumnIndex("contactNo")).
                    replaceAll(" ","").trim());
            r.moveToNext();
        }
        return trusted;
    }
}