package com.example.nowastesociety;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.nowastesociety.allurl.AllUrl;
import com.example.nowastesociety.internet.CheckConnectivity;
import com.example.nowastesociety.session.SessionManager;
import com.example.nowastesociety.utils.DirectionPointListener;
import com.example.nowastesociety.utils.GetPathFromLocation;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Tracking extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private static final String TAG = "Myapp";
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    NavigationView navigationView;
    ImageView navprofilePic;
    TextView navuserfirstName, navuserlastName, navuserEmail, tv_count;
    LinearLayoutCompat btnHome, btnProfile, btnList, btnFav;
    LinearLayout  Receipt, status;
    RelativeLayout btnCart;
    TextView tvName, tvDescription, tvDistance, tvTiming, navTitle, tvStatus, btnStatus, btnReceipt;
    ImageView imgLogo, imgBanner;
    String firstname, lastname, email, imgurl, cid, authToken, profilepic, loginId, orderNo, deliveryAddressLatitude, deliveryAddressLongitude, updateLat, updateLong, restaurantLatitude, restaurantLongitude;
    SessionManager sessionManager;
    String cartTotal;
    ActionBarDrawerToggle toggle;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SHARED_PREFS_LOCATION = "addresspref";
    String cartAmaount;


    Context context;
    private GoogleApiClient mGoogleApiClient;
    ImageView imgSearch;
    LinearLayout btn_bottom_sheet;
    private BottomSheetBehavior mBottomSheetBehavior1;
    Button btnOrderdetails;
    TextView tvSubtotal, tvCupon, tvDelivery, tvTotaltax, tvTotalamount;


    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        fetchLocation();

        sessionManager = new SessionManager(getApplicationContext());


        SharedPreferences LocationsharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS_LOCATION, MODE_PRIVATE);
        updateLat = LocationsharedPreferences.getString("Lat", "");
        updateLong = LocationsharedPreferences.getString("Long", "");

        Log.d(TAG, "LatLong-->" + updateLat + ", " + updateLong);



        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        firstname = sharedPreferences.getString("firstName", "");
        lastname = sharedPreferences.getString("lastName", "");
        email = sharedPreferences.getString("email", "");
        imgurl = sharedPreferences.getString("imgurl", "");
        cid = sharedPreferences.getString("id", "");
        authToken = sharedPreferences.getString("authToken", "");
        profilepic = sharedPreferences.getString("profilepic", "");
        loginId = sharedPreferences.getString("loginId", "");

        Log.d(TAG, "firstname-->" + firstname);


        Intent intent = getIntent();
        orderNo = intent.getStringExtra("orderNo");
        deliveryAddressLongitude = intent.getStringExtra("deliveryAddressLongitude");
        deliveryAddressLatitude = intent.getStringExtra("deliveryAddressLatitude");
        restaurantLatitude = intent.getStringExtra("restaurantLatitude");
        restaurantLongitude = intent.getStringExtra("restaurantLongitude");
        cartAmaount = intent.getStringExtra("cartTotal");
//        cartTotal = intent.getStringExtra("cartTotal");



        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();


        setSupportActionBar(toolbar);


        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        toggle = new ActionBarDrawerToggle(
                Tracking.this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorHighlight));
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navuserfirstName = header.findViewById(R.id.navuserfirstName);
        navuserlastName = header.findViewById(R.id.navuserlastName);
        navuserEmail = header.findViewById(R.id.navuserEmail);
        navprofilePic = header.findViewById(R.id.navprofilePic);
        tvName = (TextView) findViewById(R.id.tvName);
        tvDescription = (TextView) findViewById(R.id.tvDescription);
        tvDistance = (TextView) findViewById(R.id.tvDistance);
        tvTiming = (TextView) findViewById(R.id.tvTiming);
        imgBanner = (ImageView) findViewById(R.id.imgBanner);
        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        btnHome = (LinearLayoutCompat) findViewById(R.id.btnHome);
        btnCart = findViewById(R.id.btnCart);
        btnFav = (LinearLayoutCompat) findViewById(R.id.btnFav);
        btnList = (LinearLayoutCompat) findViewById(R.id.btnList);
        btnProfile = (LinearLayoutCompat) findViewById(R.id.btnProfile);
        tv_count = (TextView) findViewById(R.id.tv_count);
        imgSearch = (ImageView) findViewById(R.id.imgSearch);
        navTitle = (TextView) findViewById(R.id.navTitle);
        btn_bottom_sheet = (LinearLayout) findViewById(R.id.btn_bottom_sheet);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        btnOrderdetails = (Button) findViewById(R.id.btnOrderdetails);
        tvSubtotal = (TextView) findViewById(R.id.tvSubtotal);
        tvCupon = (TextView) findViewById(R.id.tvCupon);
        tvDelivery = (TextView) findViewById(R.id.tvDelivery);
        tvTotaltax = (TextView) findViewById(R.id.tvTotaltax);
        tvTotalamount = (TextView) findViewById(R.id.tvTotalamount);
        btnReceipt = (TextView)findViewById(R.id.btnReceipt);
        btnStatus = (TextView)findViewById(R.id.btnStatus);
        status = (LinearLayout)findViewById(R.id.status);
        Receipt = (LinearLayout)findViewById(R.id.Receipt);


        navigationView.setNavigationItemSelectedListener(this);

        navuserfirstName.setText(firstname);
        navuserlastName.setText(lastname);
        navuserEmail.setText(email);
        navTitle.setText(orderNo);

        tvSubtotal.setText("$" + cartAmaount);
        tvTotalamount.setText("$" + cartAmaount);

        Glide.with(Tracking.this)
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

                startActivity(new Intent(Tracking.this, HomeActivity.class));
                finish();

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Tracking.this, Editprofile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Tracking.this, ResturantList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Tracking.this, Myfavourite.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Tracking.this, Cartdetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Tracking.this, Search.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


        btn_bottom_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mBottomSheetBehavior1.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED);
                    tvStatus.setText("Expand");
                } else {
                    mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    tvStatus.setText("Close");
                }
            }
        });


        btnOrderdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                finish();

            }
        });


        btnStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                status.setVisibility(View.VISIBLE);
                Receipt.setVisibility(View.GONE);


            }
        });


        btnReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Receipt.setVisibility(View.VISIBLE);
                status.setVisibility(View.GONE);
            }
        });


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

    @Override
    protected void onResume() {
        super.onResume();
        tv_count.setText(new SessionManager(this).getBatchcount());

    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();
        }
    }


    private void fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
//                    Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
                    SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.current_location);
                    assert supportMapFragment != null;
                    supportMapFragment.getMapAsync(Tracking.this);
                }
            }
        });

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

                        Toast.makeText(Tracking.this, msg, Toast.LENGTH_SHORT).show();

                        sessionManager.logoutUser();
                        LoginManager.getInstance().logOut();
                        signOut();
                        SharedPreferences settings = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                        settings.edit().clear().apply();
                        SharedPreferences remove = getSharedPreferences(SHARED_PREFS_LOCATION, Context.MODE_PRIVATE);
                        remove.edit().clear().apply();
                        startActivity(new Intent(Tracking.this, Login.class));
                        finish();


                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Tracking.this, "Login not successfull", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Tracking.this, "Invalid", Toast.LENGTH_SHORT).show();

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


            Intent intent = new Intent(Tracking.this, Editprofile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }

        if (id == R.id.nav_fav) {
            // Handle the camera action
            startActivity(new Intent(Tracking.this, Myfavourite.class));

        }
//
        if (id == R.id.nav_orders) {
            // Handle the camera action
            startActivity(new Intent(Tracking.this, Myorders.class));

        }
//
        if (id == R.id.nav_address) {
            // Handle the camera action
            startActivity(new Intent(this, Myaddress.class));

        }
//
        if (id == R.id.nav_issue) {
            // Handle the camera action
            startActivity(new Intent(Tracking.this, Issue.class));

        }


        if (id == R.id.nav_payment) {
            // Handle the camera action

            Intent intent = new Intent(Tracking.this, Payment.class);
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

    @Override
    public void onMapReady(GoogleMap googleMap) {


        LatLng latLng = new LatLng(Double.parseDouble(restaurantLatitude), Double.parseDouble(restaurantLongitude));

        LatLng destination = new LatLng(Double.parseDouble(deliveryAddressLatitude), Double.parseDouble(deliveryAddressLongitude));

        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("Restaurant Location");
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
        googleMap.addMarker(markerOptions);
        MarkerOptions markerOptions1 = new MarkerOptions().position(destination).title("Delivery Location!");
        googleMap.addMarker(markerOptions1);

        drawRouteInMap(latLng, destination, googleMap);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation();
                }
                break;
        }
    }

    private void drawRouteInMap(LatLng source, LatLng destination, GoogleMap mGoogleMap) {
        // LatLng source = new LatLng(xx.xxxx, yy.yyyy);
        //  LatLng destination = new LatLng(xx.xxxx, yy.yyyy);
        new GetPathFromLocation(source, destination, new DirectionPointListener() {
            @Override
            public void onPath(PolylineOptions polyLine) {
                mGoogleMap.addPolyline(polyLine);
            }
        }).execute();
    }

}