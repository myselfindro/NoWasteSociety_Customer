package com.example.nowastesociety;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.nowastesociety.allurl.AllUrl;
import com.example.nowastesociety.internet.CheckConnectivity;
import com.example.nowastesociety.session.SessionManager;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Editaddress extends AppCompatActivity {

    private static final String TAG = "Myapp";
    EditText etaddressType, etflatOrHouse, etareaOrColony, etpinCode, ettownOrCity, etLandmark;
    String addresstype, flatorhouse, areaorcolony, pincode, townorcity, landmark, authToken;
    String addressId, Landmark, flatOrHouseOrBuildingOrCompany, areaOrColonyOrStreetOrSector, pinCode, townOrCity, item, addressType;
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    Button btnEditaddress;
    ImageView btn_back;
    String Lat, Long;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    PlacesClient placesClient;
    Spinner spType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editaddress);
        sessionManager = new SessionManager(getApplicationContext());
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        placesClient = Places.createClient(this);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        authToken = sharedPreferences.getString("authToken", "");

        Intent intent = getIntent();
        addressId = intent.getStringExtra("addressId");
        Landmark = intent.getStringExtra("landmark");
        flatOrHouseOrBuildingOrCompany = intent.getStringExtra("flatOrHouseOrBuildingOrCompany");
        areaOrColonyOrStreetOrSector = intent.getStringExtra("areaOrColonyOrStreetOrSector");
        pinCode = intent.getStringExtra("pinCode");
        townOrCity = intent.getStringExtra("townOrCity");
        addressType = intent.getStringExtra("addressType");
        Lat = intent.getStringExtra("latitude");
        Long = intent.getStringExtra("longitude");


        Log.d(TAG, "addressId-->" + addressId);


        etaddressType = (EditText) findViewById(R.id.etaddressType);
        etflatOrHouse = (EditText) findViewById(R.id.etflatOrHouse);
        etareaOrColony = (EditText) findViewById(R.id.etareaOrColony);
        etpinCode = (EditText) findViewById(R.id.etpinCode);
        ettownOrCity = (EditText) findViewById(R.id.ettownOrCity);
        etLandmark = (EditText) findViewById(R.id.etLandmark);
        btnEditaddress = (Button) findViewById(R.id.btnEditaddress);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        spType = (Spinner) findViewById(R.id.spType);



        etareaOrColony.setText(areaOrColonyOrStreetOrSector);
        etflatOrHouse.setText(flatOrHouseOrBuildingOrCompany);
        etpinCode.setText(pinCode);
        ettownOrCity.setText(townOrCity);
        etLandmark.setText(Landmark);


        etareaOrColony.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openSearchBar();

            }
        }); btnEditaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkblank();

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        onclick();
        sptype();


    }


    private void onclick() {

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {


                    item = adapterView.getItemAtPosition(i).toString();
                    Log.d(TAG, "gender-->" + item);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void sptype() {

        List<String> gender = new ArrayList<String>();

        gender.add("Please Select");
        gender.add("Work");
        gender.add("Home");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spType.setAdapter(arrayAdapter);
        if (addressType.equals("Home")) {

            spType.setSelection(2);
        } else if (addressType.equals("Work")) {

            spType.setSelection(1);
        } else {

            spType.setSelection(0);
        }


    }

    private void openSearchBar() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                .build(this);
        startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                String lati = place.getLatLng().latitude + "";
                String longi = place.getLatLng().longitude + "";
                String address = place.getName();
                Lat = lati;
                Long = longi;
                etareaOrColony.setText(address);

                Log.i(TAG, "Place: " + lati + ", " + longi);

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




    public void checkblank() {

        flatorhouse = etflatOrHouse.getText().toString();
        areaorcolony = etareaOrColony.getText().toString();
        pincode = etpinCode.getText().toString();
        townorcity = ettownOrCity.getText().toString();
        landmark = etLandmark.getText().toString();

        if (spType.getSelectedItem().toString().trim().equals("Please Select")) {

            Toast.makeText(this, "Select Address Type", Toast.LENGTH_LONG).show();

        } else if (flatorhouse.length() == 0) {

            Toast.makeText(this, "Enter Flat or House", Toast.LENGTH_LONG).show();

        } else if (areaorcolony.length() == 0) {

            Toast.makeText(this, "Enter Area or Colony", Toast.LENGTH_LONG).show();

        }else if (landmark.length() == 0) {

            Toast.makeText(this, "Enter Landmark", Toast.LENGTH_LONG).show();

        } else {

            editaddress();
        }


    }


    public void editaddress() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();



            JSONObject params = new JSONObject();

            try {
                params.put("addressType", spType.getSelectedItem().toString());
                params.put("flatOrHouseOrBuildingOrCompany", flatorhouse);
                params.put("fullAddress", areaorcolony);
                params.put("landmark", landmark);
                params.put("isDefault", "0");
                params.put("addressId", addressId);
                params.put("latitude", Lat);
                params.put("longitude", Long);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, AllUrl.EditAddress, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    boolean success = result.getBoolean("success");
                    if (success) {

                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, Myaddress.class);
                        startActivity(intent);
                        finish();

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
                    Toast.makeText(Editaddress.this, "Invalid", Toast.LENGTH_SHORT).show();

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
