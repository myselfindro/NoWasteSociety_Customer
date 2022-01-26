package com.example.nowastesociety.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nowastesociety.MainActivity;
import com.example.nowastesociety.R;
import com.example.nowastesociety.model.ResturantDetailsModel;

import java.util.ArrayList;

public class ResturantDetailsAdapter extends RecyclerView.Adapter<ResturantDetailsAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<ResturantDetailsModel> resturantDetailsModelArrayList;
    private Context ctx;

    public ResturantDetailsAdapter(Context ctx, ArrayList<ResturantDetailsModel> resturantDetailsModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.resturantDetailsModelArrayList = resturantDetailsModelArrayList;
        this.ctx = ctx;

    }


    @NonNull
    @Override
    public ResturantDetailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_resturantdetails, parent, false);
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ResturantDetailsAdapter.MyViewHolder holder, int position) {
        ResturantDetailsModel databean = resturantDetailsModelArrayList.get(position);
        holder.tvItemname.setText(databean.getItemName());
        holder.tvItemprice.setText("$" + databean.getPrice());
        holder.tvQuantity.setText("" + databean.getQuantity());
        if(databean.getQuantity()>0){
            holder.ll_Counter.setVisibility(View.VISIBLE);
            holder.ll_addtoCart.setVisibility(View.GONE);
        }else{
            holder.ll_Counter.setVisibility(View.GONE);
            holder.ll_addtoCart.setVisibility(View.VISIBLE);
        }

        Glide.with(ctx)
                .load(databean.getMenuImage())
                .placeholder(R.drawable.item)
                .into(holder.itemImg);


        if (databean.getDescription() == "null") {
            holder.tvDescription.setText("There are many variations.");
        } else {
            holder.tvDescription.setText(databean.getDescription());
        }

        holder.btnAdd.setOnClickListener(view -> {
//            if (ctx instanceof MainActivity && databean.getQuantity() > 0) {
//                ((MainActivity) ctx).addToCart(databean);
//            } else {
//
//                Toast.makeText(view.getContext(), "Please add Quantity", Toast.LENGTH_SHORT).show();
//
//            }
            ((MainActivity) ctx).addToCart(databean,holder.getAdapterPosition());

        });
        holder.ivDecrese.setOnClickListener(view -> {

            int val = databean.getQuantity() - 1;
            if (val > 0) {
                holder.tvQuantity.setText("" + val);
                databean.setQuantity(val);
                        ((MainActivity) ctx).updateCart(databean);
            }else{
                val=0;
                databean.setQuantity(0);
                notifyItemChanged(holder.getAdapterPosition());
                ((MainActivity) ctx).removeToCart(databean);
            }

        });
        holder.ivIncrease.setOnClickListener(view -> {

            int val = databean.getQuantity() + 1;
            if (val > 0) {
                holder.tvQuantity.setText("" + val);
                databean.setQuantity(val);
                ((MainActivity) ctx).updateCart(databean);
            }else{
                val=0;
                databean.setQuantity(0);
                notifyItemChanged(holder.getAdapterPosition());
                ((MainActivity) ctx).removeToCart(databean);
            }
        });

    }

    @Override
    public int getItemCount() {
        return resturantDetailsModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvItemname, tvItemprice, tvDescription, btnAdd, tvQuantity;
        ImageView itemImg;
        ImageView ivDecrese;
        ImageView ivIncrease;
        LinearLayoutCompat ll_Counter, ll_addtoCart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvItemname = (TextView) itemView.findViewById(R.id.tvItemname);
            tvItemprice = (TextView) itemView.findViewById(R.id.tvItemprice);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            itemImg = (ImageView) itemView.findViewById(R.id.itemImg);
            btnAdd = (TextView) itemView.findViewById(R.id.btnAdd);
            ivDecrese = itemView.findViewById(R.id.ivDecrese);
            ivIncrease = itemView.findViewById(R.id.ivIncrease);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            ll_Counter = itemView.findViewById(R.id.ll_Counter);
            ll_addtoCart = itemView.findViewById(R.id.ll_addtoCart);


        }

    }
}
