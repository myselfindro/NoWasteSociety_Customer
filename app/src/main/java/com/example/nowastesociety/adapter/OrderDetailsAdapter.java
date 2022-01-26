package com.example.nowastesociety.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nowastesociety.R;
import com.example.nowastesociety.model.OderdetailsModel;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<OderdetailsModel> oderdetailsModelArrayList;
    private Context ctx;

    public OrderDetailsAdapter(Context ctx, ArrayList<OderdetailsModel> oderdetailsModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.oderdetailsModelArrayList = oderdetailsModelArrayList;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_order_details, parent, false);
        OrderDetailsAdapter.MyViewHolder holder = new OrderDetailsAdapter.MyViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.MyViewHolder holder, int position) {

        holder.tvItemname.setText(oderdetailsModelArrayList.get(position).getItem());
        holder.tvQuantity.setText(oderdetailsModelArrayList.get(position).getQuantity()+"");
        holder.tvItemprice.setText("$ "+oderdetailsModelArrayList.get(position).getItemPrice());

        Glide.with(ctx)
                .load(oderdetailsModelArrayList.get(position).getItemImg())
                .placeholder(R.drawable.item)
                .into(holder.itemImg);


    }

    @Override
    public int getItemCount() {
        return oderdetailsModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemname, tvDescription, tvQuantity, tvItemprice;
        ImageView itemImg;
        LinearLayoutCompat btnRemove;
        ImageView ivDecrese;
        ImageView ivIncrease;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemname = (TextView)itemView.findViewById(R.id.tvItemname);
            tvDescription = (TextView)itemView.findViewById(R.id.tvDescription);
            tvQuantity = (TextView)itemView.findViewById(R.id.tvQuantity);
            tvItemprice = (TextView)itemView.findViewById(R.id.tvItemprice);
            ivDecrese = itemView.findViewById(R.id.ivDecrese);
            ivIncrease = itemView.findViewById(R.id.ivIncrease);
            itemImg = (ImageView)itemView.findViewById(R.id.itemImg);
            btnRemove = (LinearLayoutCompat)itemView.findViewById(R.id.btnRemove);


        }
    }

}
