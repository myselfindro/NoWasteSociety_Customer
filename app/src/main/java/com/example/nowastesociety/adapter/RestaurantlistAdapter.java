package com.example.nowastesociety.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.nowastesociety.HomeActivity;
import com.example.nowastesociety.MainActivity;
import com.example.nowastesociety.R;
import com.example.nowastesociety.ResturantList;
import com.example.nowastesociety.model.ResturentModel;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

public class RestaurantlistAdapter extends RecyclerView.Adapter<RestaurantlistAdapter.MyViewHolder> {


    private LayoutInflater inflater;
    private ArrayList<ResturentModel> resturentModelArrayList;
    Context ctx;


    public RestaurantlistAdapter(Context ctx, ArrayList<ResturentModel> resturentModelArrayList) {
        inflater = LayoutInflater.from(ctx);
        this.resturentModelArrayList = resturentModelArrayList;
        this.ctx = ctx;

    }



    @NonNull
    @Override
    public RestaurantlistAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_resturant_list, parent, false);
        RestaurantlistAdapter.MyViewHolder holder = new RestaurantlistAdapter.MyViewHolder(view);



        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantlistAdapter.MyViewHolder holder, int position) {

        String resName = "";
        if (resturentModelArrayList.get(position).getName().length() >=15){

            resName = resturentModelArrayList.get(position).getName().substring(0, 15)+"...";
        }else {

            resName = resturentModelArrayList.get(position).getName();

        }

        holder.tvResturantname.setText(resName);
        holder.tvDistance.setText(resturentModelArrayList.get(position).getDistance());
        holder.tvRatings.setText(resturentModelArrayList.get(position).getRating());
        holder.tvDescription.setText(resturentModelArrayList.get(position).getDescription());

        Glide.with(ctx)
                .load(resturentModelArrayList.get(position).getLogo())
                .placeholder(R.drawable.item)
                .into(holder.itemImg);


        if(resturentModelArrayList.get(position).getFavourite().equals("0")){
            holder.btnHeart.setLiked(false);
        }else{
            holder.btnHeart.setLiked(true);
        }


        holder.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Lat = ((ResturantList)ctx).getLat();
                String Long = ((ResturantList)ctx).getLong();


                Intent intent = new Intent(ctx, MainActivity.class);
                intent.putExtra("vendorId", resturentModelArrayList.get(position).getId());
                intent.putExtra("Lat", Lat);
                intent.putExtra("Long", Long);
                intent.putExtra("name", resturentModelArrayList.get(position).getName());
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(intent);

            }
        });



        holder.btnHeart.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                ((ResturantList) ctx).addTofavourite(resturentModelArrayList.get(position));


            }

            @Override
            public void unLiked(LikeButton likeButton) {


                ((ResturantList) ctx).addTounfavourite(resturentModelArrayList.get(position));



            }
        });





    }

    @Override
    public int getItemCount() {
        return resturentModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvResturantname, tvDistance, tvRatings, tvDescription;
        ImageView itemImg;
        LinearLayout btnDetails;
        LikeButton btnHeart;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvResturantname = (TextView) itemView.findViewById(R.id.tvResturantname);
            tvDistance = (TextView) itemView.findViewById(R.id.tvDistance);
            tvRatings = (TextView) itemView.findViewById(R.id.tvRatings);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            itemImg = (ImageView) itemView.findViewById(R.id.itemImg);
            btnDetails = (LinearLayout)itemView.findViewById(R.id.btnDetails);
            btnHeart = (LikeButton)itemView.findViewById(R.id.btnHeart);


        }
    }
}
