package com.example.imdriving;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;

public class RecyclerActivity extends AppCompatActivity {
    private RecyclerView recycle;
    private String[][] dets;
    private String TAG = "RecyclerActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.inflated_layout);
        activityHandler();
    }
    private void activityHandler(){
        if(dets == null)
            dets = AppInAction.getDets();
        Log.d(TAG," RecyclerActivity....");
        recycle = findViewById(R.id.detail_holder);
        Log.d(TAG,"About to inflate.....");
        MyAdapter adapter = new MyAdapter(this, dets);
        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        Log.d(TAG,"Layout inflated.....");

    }

}