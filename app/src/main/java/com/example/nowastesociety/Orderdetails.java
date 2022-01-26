package com.example.nowastesociety;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowastesociety.adapter.OrderDetailsAdapter;
import com.example.nowastesociety.allurl.AllUrl;
import com.example.nowastesociety.internet.CheckConnectivity;
import com.example.nowastesociety.model.OderdetailsModel;
import com.example.nowastesociety.model.ResturentModel;
import com.example.nowastesociety.session.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Orderdetails extends AppCompatActivity {

    ImageView btn_back;
    String firstname, lastname, email, imgurl, cid, authToken, profilepic, loginId, orderId, address, cartTotal;
    LinearLayoutCompat btnHome, btnProfile, btnList, btnFav;
    RelativeLayout btnCart;
    TextView tv_count, tvOrderid, tvOrderno, tvCustomername, tvAddress, tvResturantname;
    SessionManager sessionManager;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String TAG = "Myapp";
    private ArrayList<OderdetailsModel> oderdetailsModelArrayList = new ArrayList<>();
    private RecyclerView rv_orderDetails;
    private OrderDetailsAdapter orderDetailsAdapter;
    Button btnTrack;
    String orderNo, deliveryAddressLatitude, deliveryAddressLongitude, restaurantLatitude, restaurantLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetails);
        tv_count = (TextView) findViewById(R.id.tv_count);
        btnHome = (LinearLayoutCompat) findViewById(R.id.btnHome);
        btnCart = (RelativeLayout) findViewById(R.id.btnCart);
        btnFav = (LinearLayoutCompat) findViewById(R.id.btnFav);
        btnList = (LinearLayoutCompat) findViewById(R.id.btnList);
        btnProfile = (LinearLayoutCompat) findViewById(R.id.btnProfile);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        tvOrderid = (TextView) findViewById(R.id.tvOrderid);
        tvOrderno = (TextView) findViewById(R.id.tvOrderno);
        tvCustomername = (TextView) findViewById(R.id.tvCustomername);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvResturantname = (TextView) findViewById(R.id.tvResturantname);
        rv_orderDetails = (RecyclerView) findViewById(R.id.rv_orderDetails);
        btnTrack = (Button) findViewById(R.id.btnTrack);

        rv_orderDetails.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        orderDetailsAdapter = new OrderDetailsAdapter(this, oderdetailsModelArrayList);
        rv_orderDetails.setAdapter(orderDetailsAdapter);


        sessionManager = new SessionManager(getApplicationContext());

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        firstname = sharedPreferences.getString("firstName", "");
        lastname = sharedPreferences.getString("lastName", "");
        email = sharedPreferences.getString("email", "");
        imgurl = sharedPreferences.getString("imgurl", "");
        cid = sharedPreferences.getString("id", "");
        authToken = sharedPreferences.getString("authToken", "");
        profilepic = sharedPreferences.getString("profilepic", "");
        loginId = sharedPreferences.getString("loginId", "");

        Log.d(TAG, "authToken-->" + authToken);

        Intent intent = getIntent();
        orderId = intent.getStringExtra("orderId");


        tv_count.setText(new SessionManager(this).getBatchcount());

        orderDetaiils();

//
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Orderdetails.this, Tracking.class);
                intent.putExtra("orderNo", orderNo);
                intent.putExtra("deliveryAddressLongitude", deliveryAddressLongitude);
                intent.putExtra("deliveryAddressLatitude", deliveryAddressLatitude);
                intent.putExtra("restaurantLatitude", restaurantLatitude);
                intent.putExtra("restaurantLongitude", restaurantLongitude);
                intent.putExtra("cartTotal", cartTotal);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Orderdetails.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Orderdetails.this, Editprofile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Orderdetails.this, ResturantList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Orderdetails.this, Myfavourite.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Orderdetails.this, Cartdetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });




    }


    public void orderDetaiils() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

            showProgressDialog();


            JSONObject params = new JSONObject();

            try {
                params.put("userType", "customer");
                params.put("orderId", orderId);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, AllUrl.OrderDetails, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    boolean success = result.getBoolean("success");
                    if (success) {
                        JSONObject response_data = result.getJSONObject("response_data");
                        orderNo = response_data.getString("orderNo");
                        address =
//                                response_data.getJSONObject("address").getString("addressType") + ", " +
                                response_data.getJSONObject("address").getString("fullAddress");
//                                response_data.getJSONObject("address").getString("flatOrHouseOrBuildingOrCompany");
                        String restaurantName = response_data.getString("restaurantName");
                        deliveryAddressLatitude = response_data.getString("deliveryAddressLatitude");
                        deliveryAddressLongitude = response_data.getString("deliveryAddressLongitude");
                        restaurantLatitude = response_data.getString("restaurantLatitude");
                        restaurantLongitude = response_data.getString("restaurantLongitude");


                        tvOrderno.setText("Order No: " + orderNo);
                        tvCustomername.setText("Customer Name: " + firstname + " " + lastname);
                        tvAddress.setText("Address: " + address);
                        tvResturantname.setText("Resturant Name: " + restaurantName);
                        tvOrderid.setText("Order ID: " + orderNo + ", " + restaurantName);

                        JSONArray orderArray = response_data.getJSONArray("cartDetail");
                        for (int i = 0; i < orderArray.length(); i++) {
                            JSONObject orderobj = orderArray.getJSONObject(i);
                            JSONObject subobj = orderobj.getJSONObject("itemId");

                            OderdetailsModel oderdetailsModel = new OderdetailsModel();
                            oderdetailsModel.setItemId(orderobj.getString("_id"));
                            oderdetailsModel.setItem(subobj.getString("itemName"));
                            oderdetailsModel.setItemImg(subobj.getString("menuImage"));
                            oderdetailsModel.setItemPrice(orderobj.getString("itemAmount"));
                            oderdetailsModel.setQuantity(orderobj.getInt("itemQuantity"));

                            oderdetailsModelArrayList.add(oderdetailsModel);

                        }

                        cartTotal = response_data.getString("cartTotal");

                    }

                    orderDetailsAdapter.notifyDataSetChanged();


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Orderdetails.this, "Invalid", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", authToken);
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(jsonRequest);

        } else {

            Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

        }


    }


    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


}