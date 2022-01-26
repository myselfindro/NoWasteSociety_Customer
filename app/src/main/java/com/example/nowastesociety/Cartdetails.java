package com.example.nowastesociety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.nowastesociety.adapter.CardviewAdapter;
import com.example.nowastesociety.adapter.CartitemAdapter;
import com.example.nowastesociety.allurl.AllUrl;
import com.example.nowastesociety.internet.CheckConnectivity;
import com.example.nowastesociety.model.AddressviewModel;
import com.example.nowastesociety.model.CardModel;
import com.example.nowastesociety.model.CartitemModel;
import com.example.nowastesociety.model.ResturantDetailsModel;
import com.example.nowastesociety.retrofit.ApiClient;
import com.example.nowastesociety.retrofit.ApiInterface;
import com.example.nowastesociety.session.SessionManager;
import com.example.nowastesociety.utils.CommonMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.navigation.NavigationView;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class Cartdetails extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "Myapp";
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView navprofilePic;
    TextView navuserfirstName, navuserlastName, navuserEmail, tvSubtotal, tvTotalamount, tv_count, userAddress, userPhone, tvuserName, btnchangeAddress;
    LinearLayoutCompat btnHome, btnProfile, btnList, btnFav;
    RelativeLayout btnCart;
    String firstname, lastname, email, imgurl, cid, authToken, profilepic, loginId, vendorId, addressId, updateLat, updateLong, phone, areaOrColonyOrStreetOrSector;
    String cartid;
    SessionManager sessionManager;
    ActionBarDrawerToggle toggle;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SHARED_PREFS_LOCATION = "addresspref";


    private GoogleApiClient mGoogleApiClient;

    ArrayList<CartitemModel> cartitemModelArrayList = new ArrayList<>();
    private CartitemAdapter cartitemAdapter;
    private RecyclerView rvCartitems;
    LinearLayout ll_cart;
    LinearLayout rl_empty_cart;
    Button btnPayment, btnBrowse;
    String cartTotal, _id;
    ImageView imgSearch;
    RadioButton rbFromresturant, rbDelivery;
    String deliveryOption = "";
    SharedPreferences LocationsharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartdetails);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        phone = sharedPreferences.getString("phone", "");


        Log.d(TAG, "authToken-->" + authToken);


        LocationsharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS_LOCATION, MODE_PRIVATE);
        addressId = LocationsharedPreferences.getString("addressId", "");
        updateLat = LocationsharedPreferences.getString("Lat", "");
        updateLong = LocationsharedPreferences.getString("Long", "");
        areaOrColonyOrStreetOrSector = LocationsharedPreferences.getString("flatOrHouseOrBuildingOrCompany", "");

        if (areaOrColonyOrStreetOrSector == null) {
            areaOrColonyOrStreetOrSector = "";
        } else {
            areaOrColonyOrStreetOrSector = LocationsharedPreferences.getString("flatOrHouseOrBuildingOrCompany", "");

        }


        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();

        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        toggle = new ActionBarDrawerToggle(
                Cartdetails.this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorHighlight));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navuserfirstName = header.findViewById(R.id.navuserfirstName);
        navuserlastName = header.findViewById(R.id.navuserlastName);
        navuserEmail = header.findViewById(R.id.navuserEmail);
        navprofilePic = header.findViewById(R.id.navprofilePic);

        btnHome = (LinearLayoutCompat) findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnFav = (LinearLayoutCompat) findViewById(R.id.btnFav);
        btnList = (LinearLayoutCompat) findViewById(R.id.btnList);
        btnProfile = (LinearLayoutCompat) findViewById(R.id.btnProfile);
        rvCartitems = (RecyclerView) findViewById(R.id.rvCartitems);
        ll_cart = (LinearLayout) findViewById(R.id.ll_cart);
        rl_empty_cart = (LinearLayout) findViewById(R.id.rl_empty_cart);
        tvSubtotal = (TextView) findViewById(R.id.tvSubtotal);
        tvTotalamount = (TextView) findViewById(R.id.tvTotalamount);
        tv_count = (TextView) findViewById(R.id.tv_count);
        userAddress = (TextView) findViewById(R.id.userAddress);
        userPhone = (TextView) findViewById(R.id.userPhone);
        tvuserName = (TextView) findViewById(R.id.tvuserName);
        btnPayment = (Button) findViewById(R.id.btnPayment);
        rbDelivery = (RadioButton) findViewById(R.id.rbDelivery);
        rbFromresturant = (RadioButton) findViewById(R.id.rbFromresturant);
        btnchangeAddress = (TextView) findViewById(R.id.btnchangeAddress);
        btnBrowse = (Button) findViewById(R.id.btnBrowse);

        tv_count.setText(new SessionManager(this).getBatchcount());
        imgSearch = (ImageView) findViewById(R.id.imgSearch);


        navigationView.setNavigationItemSelectedListener(this);

        navuserfirstName.setText(firstname);
        navuserlastName.setText(lastname);
        navuserEmail.setText(email);
        tvuserName.setText(firstname + " " + lastname);
        userPhone.setText(phone);
        userAddress.setText(areaOrColonyOrStreetOrSector);


        Glide.with(Cartdetails.this)
                .load(profilepic)
                .circleCrop()
                .placeholder(R.drawable.dp)
                .into(navprofilePic);

        findViewById(R.id.iv_menu).setOnClickListener(view -> {
            setDrawerLocked();
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Cartdetails.this, HomeActivity.class));
                finish();

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Cartdetails.this, Editprofile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Cartdetails.this, ResturantList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Cartdetails.this, Myfavourite.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addressId.length() > 0) {
                    boolean isAvail = false;
                    for (int i = 0; i < cartitemModelArrayList.size(); i++) {
                        if (cartitemModelArrayList.get(i).getItemIsAvailable().equalsIgnoreCase("YES")) {
                            isAvail = true;
                        } else {
                            isAvail = false;
                            break;
                        }

                    }

                    if (isAvail) {
                        Cartavailability();

                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Cartdetails.this);
                        builder.setMessage("Some items are no longer available! Please remove those items to continue");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }

                } else {

                    Toast.makeText(getApplicationContext(), "Please Select an Address", Toast.LENGTH_LONG).show();


                }


            }
        });


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Cartdetails.this, Search.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        btnchangeAddress.setOnClickListener(view -> {
            startActivity(new Intent(Cartdetails.this, Changeaddress.class));
        });

        btnBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Cartdetails.this, HomeActivity.class));

            }
        });


        setupRecycler();
        cartitemList();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (CommonMethod.isRefreshNeeded) {
            CommonMethod.isRefreshNeeded = false;
            addressId = LocationsharedPreferences.getString("addressId", "");
            updateLat = LocationsharedPreferences.getString("Lat", "");
            updateLong = LocationsharedPreferences.getString("Long", "");
            areaOrColonyOrStreetOrSector = LocationsharedPreferences.getString("flatOrHouseOrBuildingOrCompany", "");
            userAddress.setText(areaOrColonyOrStreetOrSector);

            if (addressId.length() > 0) {
                btnchangeAddress.setVisibility(View.GONE);
            } else {
                btnchangeAddress.setVisibility(View.VISIBLE);
            }
        }

    }


    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.rbDelivery:
                if (checked)
                    deliveryOption = "DELIVERY";
                break;
            case R.id.rbFromresturant:
                if (checked)
                    deliveryOption = "PICKUP";
                break;

        }


    }


    public void Cartavailability() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();


            JSONObject params = new JSONObject();


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, AllUrl.Cartavailability, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    boolean success = result.getBoolean("success");
                    if (success) {

                        postOrder();


                    } else {

                        cartitemList();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Cartdetails.this);
                        builder.setMessage("Some items are no longer available! Please remove those items to continue");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Cartdetails.this, "Invalid", Toast.LENGTH_SHORT).show();

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


    public void postOrder() {


        if (TextUtils.isEmpty(new SessionManager(this).getAddressId())) {
            Toast.makeText(this, "Please add address", Toast.LENGTH_LONG).show();
        } else if (deliveryOption.length() == 0) {

            Toast.makeText(this, "Please Select Delivery Option", Toast.LENGTH_LONG).show();

        } else {
            if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {

                showProgressDialog();
                JSONArray itemarry = new JSONArray();

                try {
                    for (int i = 0; i < cartitemModelArrayList.size(); i++) {
                        CartitemModel item = cartitemModelArrayList.get(i);
                        JSONObject itemobject = new JSONObject();
                        itemobject.put("name", item.getItemname());
                        itemobject.put("quantity", item.getQuantity());
                        itemobject.put("price", item.getItemprice());
                        itemobject.put("itemId", item.getItemId());
                        itemarry.put(itemobject);
                    }

                    Log.v("Items arry", itemarry.toString());
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                StringRequest stringRequest = new StringRequest(Request.Method.POST, AllUrl.PostOrder,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response----->", response);


                                Intent intent = new Intent(Cartdetails.this, Paymentwebview.class);
                                intent.putExtra("url", response);
                                startActivity(intent);


                                hideProgressDialog();
                            }
                        },
                        new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("volley", "Error: " + error.getMessage());
                                error.printStackTrace();

                            }
                        }) {

                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("userType", "customer");
                        params.put("customerId", cid);
                        params.put("vendorId", _id);
                        params.put("price", cartTotal);
                        params.put("specialInstruction", "");
                        params.put("discount", "");
                        params.put("finalPrice", cartTotal);
                        params.put("promocodeId", "");
                        params.put("offerId", "");
                        params.put("items", itemarry.toString());
                        params.put("latitude", updateLat);
                        params.put("longitude", updateLong);
                        params.put("appType", "ANDROID");
                        params.put("deliveryPreference", deliveryOption);
                        params.put("addressId", addressId);
                        params.put("orderType", "NORMAL");
                        params.put("cartId", cartid);

                        return params;
                    }

                    @Override
                    public Map getHeaders() throws AuthFailureError {
                        Map headers = new HashMap();
                        headers.put("Authorization", authToken);
                        return headers;
                    }


                };

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            } else {

                Toast.makeText(getApplicationContext(), "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

            }
        }


    }


    public void setDrawerLocked() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    private void signOut() {
        if (mGoogleApiClient.isConnected()) {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    public void cartitemList() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();


            JSONObject params = new JSONObject();


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, AllUrl.FetchCart, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    boolean success = result.getBoolean("success");
                    if (success) {


//                        Toast.makeText(Cartdetails.this, msg, Toast.LENGTH_SHORT).show();
                        cartitemModelArrayList.clear();
                        JSONObject response_data = result.getJSONObject("response_data");
                        if (!response_data.has("cartTotal")) {
                            cartitemAdapter.notifyDataSetChanged();
                            //show and hide
                            hideProgressDialog();
                            ll_cart.setVisibility(View.GONE);
                            rl_empty_cart.setVisibility(View.VISIBLE);
                            CommonMethod.removeBatchcount(Cartdetails.this, tv_count, "");

                            return;
                        }
                        cartTotal = response_data.getString("cartTotal");

                        cartid = response_data.getString("_id");
                        String userId = response_data.getString("userId");
                        JSONObject vendorId = response_data.getJSONObject("vendorId");
                        JSONArray deliveryOptionArray = vendorId.getJSONArray("deliveryOption");
                        if (deliveryOptionArray.length() > 1) {
                            rbFromresturant.setVisibility(View.VISIBLE);
                            rbDelivery.setVisibility(View.VISIBLE);

                        } else {
                            for (int j = 0; j < deliveryOptionArray.length(); j++) {

                                String value = deliveryOptionArray.getString(j);
                                Log.d(TAG, "delivery-->" + value);

                                if (value.equals("DELIVERY")) {

                                    rbDelivery.setVisibility(View.VISIBLE);
                                    rbFromresturant.setVisibility(View.GONE);

                                } else if (value.equals("PICKUP")) {

                                    rbFromresturant.setVisibility(View.VISIBLE);
                                    rbDelivery.setVisibility(View.GONE);

                                } else {

                                    rbFromresturant.setVisibility(View.VISIBLE);
                                    rbDelivery.setVisibility(View.VISIBLE);

                                }

                            }
                        }

                        String restaurantName = vendorId.getString("restaurantName");
                        _id = vendorId.getString("_id");

                        tvSubtotal.setText("$" + cartTotal);
                        tvTotalamount.setText("$" + cartTotal);
                        JSONArray itemArray = response_data.getJSONArray("item");
                        for (int i = 0; i < itemArray.length(); i++) {
                            JSONObject itemobj = itemArray.getJSONObject(i);
                            JSONObject subobj = itemobj.getJSONObject("itemId");
                            CartitemModel cartitemModel = new CartitemModel();

//                            cartitemModel.setItemId(itemobj.getString("_id"));

                            cartitemModel.setItemname(subobj.getString("itemName"));
                            cartitemModel.setItemImg(subobj.getString("menuImage"));
                            cartitemModel.setItemId(subobj.getString("_id"));
                            cartitemModel.setItemIsAvailable(itemobj.getString("itemIsAvailable"));


//                            if (itemobj.has("description"))
//                                mCartitemModel.setDescription(itemobj.getString("description"));
//                            else
//                                mCartitemModel.setDescription(itemobj.getString(""));
                            cartitemModel.setItemprice(itemobj.getString("itemAmount"));
                            cartitemModel.setQuantity(itemobj.getInt("itemQuantity"));

                            cartitemModelArrayList.add(cartitemModel);
                        }
                        int count = 0;
                        for (CartitemModel mDatabean : cartitemModelArrayList) {
                            count += mDatabean.getQuantity();
                        }
                        CommonMethod.removeBatchcount(Cartdetails.this, tv_count, count + "");

                        cartitemAdapter.notifyDataSetChanged();


                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Cartdetails.this, "invalid", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Cartdetails.this, "Invalid", Toast.LENGTH_SHORT).show();

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


    private void setupRecycler() {
        rvCartitems.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        cartitemAdapter = new CartitemAdapter(this, cartitemModelArrayList);
        rvCartitems.setAdapter(cartitemAdapter);

    }

    public void removeToCart(CartitemModel cartitemModel) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Food item will be remove from your cart!");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                deleteItem(cartitemModel);

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                dialog.cancel();
                Toast.makeText(Cartdetails.this, "Cancel", Toast.LENGTH_SHORT).show();


            }
        });

        AlertDialog alert = builder.create();
        alert.show();


    }

    public void updateCart(CartitemModel cartitemModel) {
//        Log.i("Response-->", "cart quantity updating");


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("cartId", cartid);
                params.put("itemId", cartitemModel.getItemId());
                params.put("itemQuantity", cartitemModel.getQuantity());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, AllUrl.UpdateCart, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    boolean success = result.getBoolean("success");
                    if (success) {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        cartitemList();

                    } else {

                        Toast.makeText(this, "invalid 1", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Cartdetails.this, "Invalid", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", authToken);
                    return params;
                }
            };

            Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);

        } else {

            Toast.makeText(this, "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

        }


    }


    public void deleteItem(CartitemModel cartitemModel) {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("cartId", cartid);
                params.put("itemId", cartitemModel.getItemId());


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, AllUrl.RemoveToCart, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    boolean success = result.getBoolean("success");
                    if (success) {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        cartitemList();

                    } else {

                        Toast.makeText(this, "invalid 1", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Cartdetails.this, "Invalid", Toast.LENGTH_SHORT).show();

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", authToken);
                    return params;
                }
            };

            Volley.newRequestQueue(getApplicationContext()).add(jsonRequest);

        } else {

            Toast.makeText(this, "Ooops! Internet Connection Error", Toast.LENGTH_SHORT).show();

        }


    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }


    public void logout() {


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();


            JSONObject params = new JSONObject();

            try {
                params.put("loginId", loginId);
                params.put("customerId", cid);
                params.put("userType", "customer");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, AllUrl.Logout, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    boolean success = result.getBoolean("success");
                    if (success) {

                        Toast.makeText(Cartdetails.this, msg, Toast.LENGTH_SHORT).show();

                        sessionManager.logoutUser();
                        LoginManager.getInstance().logOut();
                        signOut();
                        SharedPreferences settings = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                        settings.edit().clear().apply();
                        SharedPreferences remove = getSharedPreferences(SHARED_PREFS_LOCATION, Context.MODE_PRIVATE);
                        remove.edit().clear().apply();
                        startActivity(new Intent(Cartdetails.this, Login.class));
                        finish();


                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Cartdetails.this, "Login not successfull", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Cartdetails.this, "Invalid", Toast.LENGTH_SHORT).show();

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


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


        int id = menuItem.getItemId();

        //to prevent current item select over and over
        if (menuItem.isChecked()) {
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }


        if (id == R.id.nav_profile) {
            // Handle the camera action


            Intent intent = new Intent(Cartdetails.this, Editprofile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }

        if (id == R.id.nav_fav) {
            // Handle the camera action
            startActivity(new Intent(Cartdetails.this, Myfavourite.class));

        }
//
        if (id == R.id.nav_orders) {
            // Handle the camera action
            startActivity(new Intent(Cartdetails.this, Myorders.class));

        }
//
        if (id == R.id.nav_address) {
            // Handle the camera action
            Intent intent = new Intent(Cartdetails.this, Myaddress.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
//
        if (id == R.id.nav_issue) {
            // Handle the camera action
            startActivity(new Intent(Cartdetails.this, Issue.class));

        }
        if (id == R.id.nav_payment) {
            // Handle the camera action

            Intent intent = new Intent(Cartdetails.this, Payment.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }


        if (id == R.id.nav_logout) {
            // Handle the camera action

            // TODO Auto-generated method stub
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            // builder.setCancelable(false);
            builder.setMessage("Logout. Continue?");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {

                    logout();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                    dialog.cancel();

                }
            });

            AlertDialog alert = builder.create();
            alert.show();


        }


        return false;
    }


}
