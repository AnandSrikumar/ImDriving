package com.example.imdriving;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class ContactHelper {
    private static final String TAG= "ContactHelper";

    public static boolean pickAContact(int requestCode, int resultCode,
                                       @Nullable Intent resultIntent,
                                       Fragment activity, Context mContext){
        switch (requestCode) {

            case (7):
                if (resultCode == Activity.RESULT_OK) {
                    DBHelper helper = new DBHelper(mContext, null, null, 1);
                    Uri uri;
                    Cursor cursor1, cursor2;
                    String TempNumberHolder, TempContactID, IDresult = "" ;
                    int iDresultHolder ;

                    uri = resultIntent.getData();

                    cursor1 = activity.getActivity().getContentResolver().query(uri, null, null, null, null);

                    if (cursor1.moveToFirst()) {

                        TempContactID = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID));

                        IDresult = cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        iDresultHolder = Integer.valueOf(IDresult) ;

                        if (iDresultHolder == 1) {

                            cursor2 = activity.getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + TempContactID, null, null);

                            while (cursor2.moveToNext()) {

                                TempNumberHolder = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String name = cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                if(TempNumberHolder.startsWith("+91"))
                                    TempNumberHolder = TempNumberHolder.substring(3);
                                boolean err= helper.addContacts(TempNumberHolder, name);
                                Log.d(TAG," selected number->"+TempNumberHolder);
                                Log.d(TAG," selected number->"+name);
                                if(!err) {
                                    Toast.makeText(mContext, TempNumberHolder+" with name"+
                                            name+" could not be added as trusted contact",
                                            Toast.LENGTH_LONG).show();
                                    Log.d(TAG," Could not be added as trusted contact");
                                }
                                else {
                                    Toast.makeText(mContext, TempNumberHolder+" with name '"+
                                                    name+"' Successfully added as trusted contact",
                                            Toast.LENGTH_LONG).show();
                                    Log.d(TAG, "Successfully added as trusted contact");
                                }

                            }
                        }

                    }
                }
                break;
        }




        return true;
    }
}
