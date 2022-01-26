package com.example.nowastesociety.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowastesociety.Editaddress;
import com.example.nowastesociety.HomeActivity;
import com.example.nowastesociety.Myaddress;
import com.example.nowastesociety.R;
import com.example.nowastesociety.allurl.AllUrl;
import com.example.nowastesociety.internet.CheckConnectivity;
import com.example.nowastesociety.model.AddressviewModel;
import com.example.nowastesociety.session.SessionManager;
import com.example.nowastesociety.utils.CommonMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class AddresschangeAdapter extends RecyclerView.Adapter<AddresschangeAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<AddressviewModel> addressviewModelArrayList;
    Context ctx;
    String addressId;
    private static final String SHARED_PREFS_LOCATION = "addresspref";


    public AddresschangeAdapter(Context ctx, ArrayList<AddressviewModel> addressviewModelArrayList) {

        inflater = LayoutInflater.from(ctx);
        this.addressviewModelArrayList = addressviewModelArrayList;
        this.ctx = ctx;


    }


    @NonNull
    @Override
    public AddresschangeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_changeaddresslist, parent, false);
        MyViewHolder holder = new MyViewHolder(view);


        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddresschangeAdapter.MyViewHolder holder, int position) {

        holder.tvaddressType.setText(addressviewModelArrayList.get(position).getTvaddressType());
        holder.tvAddressdetails.setText(addressviewModelArrayList.get(position).getTvAddressdetails());
        addressId = addressviewModelArrayList.get(position).getAddressId();
        new SessionManager(ctx).setAddressId(addressviewModelArrayList.get(position).getAddressId());

        holder.btnChangeaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                Intent intent = new Intent(ctx, HomeActivity.class);
                SharedPreferences sharedPreferences = ctx.getSharedPreferences(SHARED_PREFS_LOCATION, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("addressId", addressviewModelArrayList.get(position).getAddressId());
                editor.putString("landmark", addressviewModelArrayList.get(position).getLandmark());
                editor.putString("addressType", addressviewModelArrayList.get(position).getTvaddressType());
                editor.putString("Lat", addressviewModelArrayList.get(position).getLat());
                editor.putString("Long", addressviewModelArrayList.get(position).getLong());
//                editor.putString("flatOrHouseOrBuildingOrCompany",
//                        addressviewModelArrayList.get(position).getFlatOrHouseOrBuildingOrCompany());
                editor.putString("flatOrHouseOrBuildingOrCompany",
                        addressviewModelArrayList.get(position).getFlatOrHouseOrBuildingOrCompany());
//                editor.putString("pinCode", addressviewModelArrayList.get(position).getPinCode());
//                editor.putString("townOrCity", addressviewModelArrayList.get(position).getTownOrCity());
                editor.putString("userId", addressviewModelArrayList.get(position).getUserId());
                editor.apply();

//                intent.putExtra("addressId", addressviewModelArrayList.get(position).getAddressId());
//                intent.putExtra("landmark", addressviewModelArrayList.get(position).getLandmark());
//                intent.putExtra("addressType", addressviewModelArrayList.get(position).getTvaddressType());
//                intent.putExtra("Lat", addressviewModelArrayList.get(position).getLat());
//                intent.putExtra("Long", addressviewModelArrayList.get(position).getLong());
//                intent.putExtra("flatOrHouseOrBuildingOrCompany",
//                        addressviewModelArrayList.get(position).getFlatOrHouseOrBuildingOrCompany());
//                intent.putExtra("areaOrColonyOrStreetOrSector",
//                        addressviewModelArrayList.get(position).getTvAddressdetails());
//                intent.putExtra("pinCode", addressviewModelArrayList.get(position).getPinCode());
//                intent.putExtra("townOrCity", addressviewModelArrayList.get(position).getTownOrCity());
//                intent.putExtra("userId", addressviewModelArrayList.get(position).getUserId());


//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                ctx.startActivity(intent);
                CommonMethod.isRefreshNeeded = true;
                ((Activity)ctx).finish();


            }
        });




    }

    @Override
    public int getItemCount() {
        return addressviewModelArrayList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvaddressType, tvAddressdetails;
        LinearLayoutCompat btnChangeaddress;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvaddressType = (TextView) itemView.findViewById(R.id.tvaddressType);
            tvAddressdetails = (TextView) itemView.findViewById(R.id.tvAddressdetails);
            btnChangeaddress = (LinearLayoutCompat)itemView.findViewById(R.id.btnChangeaddress);



        }




    }


}
