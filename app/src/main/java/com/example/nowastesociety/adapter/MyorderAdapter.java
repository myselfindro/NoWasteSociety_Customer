package com.example.nowastesociety.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nowastesociety.Orderdetails;
import com.example.nowastesociety.R;
import com.example.nowastesociety.model.MyorderModel;

import java.util.ArrayList;

public class MyorderAdapter extends RecyclerView.Adapter<MyorderAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MyorderModel> myorderModelArrayList;
    Context ctx;

    public MyorderAdapter(Context ctx, ArrayList<MyorderModel> myorderModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.myorderModelArrayList = myorderModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public MyorderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_myorderlist, parent, false);
        MyorderAdapter.MyViewHolder holder = new MyorderAdapter.MyViewHolder(view);


        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull MyorderAdapter.MyViewHolder holder, int position) {

        holder.tvName.setText(myorderModelArrayList.get(position).getItemName());
        holder.tvAmount.setText(myorderModelArrayList.get(position).getItemPrice());
        holder.tvDate.setText(myorderModelArrayList.get(position).getDate());


        Glide.with(ctx)
                .load(myorderModelArrayList.get(position).getItemImg())
                .placeholder(R.drawable.item)
                .into(holder.itemImg);

        holder.btnMyorderdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ctx, Orderdetails.class);
                intent.putExtra("orderId", myorderModelArrayList.get(position).getItemId());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);


            }
        });



    }

    @Override
    public int getItemCount() {
        return myorderModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImg;
        TextView tvName, tvAmount, tvDate;
        LinearLayoutCompat btnMyorderdetails;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImg = (ImageView) itemView.findViewById(R.id.itemImg);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvAmount = (TextView) itemView.findViewById(R.id.tvAmount);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            btnMyorderdetails = (LinearLayoutCompat)itemView.findViewById(R.id.btnMyorderdetails);




        }
    }

}
