package com.example.nowastesociety;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.nowastesociety.adapter.AddresschangeAdapter;
import com.example.nowastesociety.adapter.AddressviewAdapter;
import com.example.nowastesociety.allurl.AllUrl;
import com.example.nowastesociety.internet.CheckConnectivity;
import com.example.nowastesociety.model.AddressviewModel;
import com.example.nowastesociety.session.SessionManager;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.nowastesociety.utils.CommonMethod.isRefreshNeeded;

public class Changeaddress extends AppCompatActivity {


    ImageView btn_back, imgSearch;
    SearchView searchView;
    String firstname, lastname, email, imgurl, cid, authToken, profilepic, loginId;
    LinearLayoutCompat btnHome, btnProfile, btnList, btnFav;
    RelativeLayout btnCart;
    TextView tv_count;
    SessionManager sessionManager;
    private static final String SHARED_PREFS = "sharedPrefs";
    private static final String SHARED_PREFS_LOCATION = "addresspref";

    private static final String TAG = "Myapp";

    ArrayList<AddressviewModel> addressviewModelArrayList;
    private AddresschangeAdapter addresschangeAdapter;
    private RecyclerView rv_changeAddressList;
    Button btnAdd, btncurrentLocation;
    PlacesClient placesClient;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changeaddress);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        placesClient = Places.createClient(this);
        initialize();

    }

    private void initialize(){
        btn_back = (ImageView) findViewById(R.id.btn_back);
        searchView = (SearchView) findViewById(R.id.searchView);
        tv_count = (TextView) findViewById(R.id.tv_count);
        btnHome = (LinearLayoutCompat) findViewById(R.id.btnHome);
        btnCart = (RelativeLayout) findViewById(R.id.btnCart);
        btnFav = (LinearLayoutCompat) findViewById(R.id.btnFav);
        btnList = (LinearLayoutCompat) findViewById(R.id.btnList);
        btnProfile = (LinearLayoutCompat) findViewById(R.id.btnProfile);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btncurrentLocation = (Button)findViewById(R.id.btncurrentLocation);
        rv_changeAddressList = (RecyclerView) findViewById(R.id.rv_changeAddressList);
        imgSearch = (ImageView)findViewById(R.id.imgSearch);


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

        tv_count.setText(new SessionManager(this).getBatchcount());


        clickListner();
        addresslist();
    }

    private void clickListner(){

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Changeaddress.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Changeaddress.this, Editprofile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Changeaddress.this, ResturantList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Changeaddress.this, Myfavourite.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });


        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Changeaddress.this, Cartdetails.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);


            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(Changeaddress.this, Addressadd.class);
                startActivity(intent);


            }
        });


        btncurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences remove = getSharedPreferences(SHARED_PREFS_LOCATION, Context.MODE_PRIVATE);
                remove.edit().clear().apply();
                Intent intent = new Intent(Changeaddress.this, HomeActivity.class);
                startActivity(intent);


            }
        });


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(Changeaddress.this, Searchaddress.class);
//                startActivity(intent);
                openSearchBar();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if( isRefreshNeeded ){
            isRefreshNeeded = false;
            addresslist();
        }
    }

    private void openSearchBar(){
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
//                String lati=place.getLatLng().latitude+"";
//                String longi=place.getLatLng().longitude+"";
//                String address = place.getName();
                Intent intent = new Intent(Changeaddress.this, HomeActivity.class);
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS_LOCATION, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("addressType", place.getName());
                editor.putString("Lat", place.getLatLng().latitude+"");
                editor.putString("Long", place.getLatLng().longitude+"");
                editor.apply();
                startActivity(intent);

                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void addresslist() {

        //ADDRESS LIST


        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();


            JSONObject params = new JSONObject();


            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, AllUrl.AddressList, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    boolean success = result.getBoolean("success");
                    if (success) {

//                        if (msg.equals("No address found.")){
//
//                            ll_address.setVisibility(View.GONE);
//                            rl_no_address.setVisibility(View.VISIBLE);
//
//                            hideProgressDialog();
//
//                            return;
//                        }

//                        Toast.makeText(Changeaddress.this, msg, Toast.LENGTH_SHORT).show();

                        addressviewModelArrayList = new ArrayList<>();
                        JSONArray response_data = result.getJSONArray("response_data");
                        for (int i = 0; i < response_data.length(); i++) {

                            AddressviewModel addressviewModel = new AddressviewModel();
                            JSONObject responseobj = response_data.getJSONObject(i);
//                            locationobj = responseobj.getJSONObject("location");
                            addressviewModel.setTvaddressType(responseobj.getString("addressType"));
                            addressviewModel.setAddressId(responseobj.getString("_id"));
                            addressviewModel.setLandmark(responseobj.getString("landmark"));
                            addressviewModel.setFlatOrHouseOrBuildingOrCompany(responseobj.getString("fullAddress"));
                            addressviewModel.setUserId(responseobj.getString("userId"));
                            addressviewModel.setLat(responseobj.getString("latitude"));
                            addressviewModel.setLong(responseobj.getString("longitude"));


                            addressviewModelArrayList.add(addressviewModel);

                        }

//                        JSONArray coordinatesArray = locationobj.getJSONArray("coordinates");
//                        for (int j = 0; j<coordinatesArray.length(); j++){
//
//                            AddressviewModel addressviewModel = new AddressviewModel();
//                            JSONObject coordinateobj = coordinatesArray.getJSONObject(j);


//
//
//                        }







                        setupRecycler();


                    } else {

                        Log.d(TAG, "unsuccessfull - " + "Error");
                        Toast.makeText(Changeaddress.this, "invalid 1", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Changeaddress.this, "Invalid", Toast.LENGTH_SHORT).show();

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

        addresschangeAdapter = new AddresschangeAdapter(this, addressviewModelArrayList);
        rv_changeAddressList.setAdapter(addresschangeAdapter);
        rv_changeAddressList.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

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
