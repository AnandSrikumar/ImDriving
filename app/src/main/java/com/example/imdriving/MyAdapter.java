package com.example.imdriving;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private final String TAG = "MyAdapter";
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, ph;
        Button bn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            ph = itemView.findViewById(R.id.phone);
            bn = itemView.findViewById(R.id.delete);
        }
    }
    private Context mContext;
    private String[][] dets;
    private Button bn;
    public MyAdapter(Context mContext, String[][] dets){
        this.mContext = mContext;
        this.dets = dets;
        //this.bn = bn;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflate = LayoutInflater.from(mContext);
        View view = inflate.inflate(R.layout.detailed_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ph.setText(dets[position][0]);
        holder.name.setText(dets[position][1]);

        final String nm = dets[position][0];
        holder.bn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(holder.getAdapterPosition());
                DBHelper helper = new DBHelper(mContext, "App.db",null, 1);
                Log.d(TAG,"about to delete the item....");
                helper.deleteContact(nm);

            }
        });
    }

    private void removeItem(int position){
        dets[position][0] = "REMOVED.....";
        dets[position][1] = "REMOVED.....";
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return dets.length;
    }
}
