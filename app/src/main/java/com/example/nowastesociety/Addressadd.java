package com.example.nowastesociety;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.example.nowastesociety.utils.CommonMethod.isRefreshNeeded;

public class Addressadd extends AppCompatActivity {

    private static final String TAG = "Myapp";
    EditText etaddressType, etflatOrHouse, etLandmark, currentAddress;
    String addresstype, flatorhouse, landmark, authToken, addressfull;
    private static final String SHARED_PREFS = "sharedPrefs";
    SessionManager sessionManager;
    Button btnAddaddress;
    ImageView btn_back;
    String Lat, Long, currentLat, currentLong, currentfullAddress, item;
    Geocoder geocoder;
    List<Address> addresses;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    String changeLat = "", changeLong = "";
    TextView imgSearch;
    PlacesClient placesClient;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;

    Spinner spType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addressadd);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key));
        }
        placesClient = Places.createClient(this);
        initiliaze();


    }

    private void initiliaze() {

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        authToken = sharedPreferences.getString("authToken", "");

        Log.d(TAG, "authToken-->" + authToken);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        geocoder = new Geocoder(this, Locale.getDefault());


        fetchLocation();

        etaddressType = (EditText) findViewById(R.id.etaddressType);
        currentAddress = (EditText) findViewById(R.id.currentAddress);
        etflatOrHouse = (EditText) findViewById(R.id.etflatOrHouse);
        etLandmark = (EditText) findViewById(R.id.etLandmark);
        btnAddaddress = (Button) findViewById(R.id.btnAddaddress);
        btn_back = (ImageView) findViewById(R.id.btn_back);
        imgSearch = (TextView) findViewById(R.id.imgSearch);
        spType = (Spinner) findViewById(R.id.spType);


        btnAddaddress.setOnClickListener(new View.OnClickListener() {
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

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openSearchBar();

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
//        if (genderType.equals("Female")) {
//
//            spGender.setSelection(2);
//        } else if (genderType.equals("Male")) {
//
//            spGender.setSelection(1);
//        } else {
//
//            spGender.setSelection(0);
//        }


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
                currentLat = lati;
                currentLong = longi;
                currentAddress.setText(address);


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
        landmark = etLandmark.getText().toString();
        addressfull = currentAddress.getText().toString();

        if (addressfull.length() == 0) {
            Toast.makeText(this, "Enter Full Address", Toast.LENGTH_LONG).show();

        } else if (flatorhouse.length() == 0) {

            Toast.makeText(this, "Enter Flat or House", Toast.LENGTH_LONG).show();

        } else if (landmark.length() == 0) {

            Toast.makeText(this, "Enter Landmark", Toast.LENGTH_LONG).show();

        } else if (spType.getSelectedItem().toString().trim().equals("Please Select")) {

            Toast.makeText(this, "Please Select Address Type", Toast.LENGTH_SHORT).show();

        } else {

            addaddress();

        }


    }

    private void addaddress() {

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


            showProgressDialog();

            JSONObject params = new JSONObject();

            try {
                params.put("addressType", spType.getSelectedItem().toString());
                params.put("landmark", landmark);
                params.put("isDefault", "1");
                params.put("latitude", currentLat);
                params.put("longitude", currentLong);
                params.put("fullAddress", addressfull);
                params.put("flatOrHouseOrBuildingOrCompany", flatorhouse);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, AllUrl.AddAddress, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    boolean success = result.getBoolean("success");
                    if (success) {
                        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                isRefreshNeeded = true;
                            }
                        }, 800);

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
                    //  Toast.makeText(Addressadd.this, "Invalid", Toast.LENGTH_SHORT).show();
                    parseVolleyError(error);
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

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            JSONArray errors = data.getJSONArray("errors");
            JSONObject jsonMessage = errors.getJSONObject(0);
            String message = jsonMessage.getString("message");
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
        } catch (UnsupportedEncodingException errorr) {
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
                    currentLat = String.valueOf(currentLocation.getLatitude());
                    currentLong = String.valueOf(currentLocation.getLongitude());
                    try {
                        addresses = geocoder.getFromLocation(Double.parseDouble(currentLat), Double.parseDouble(currentLong), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String address = addresses.get(0).getAddressLine(0);
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    currentfullAddress = address + "," + city + "," + state + "," + country + "," + postalCode;

                    currentAddress.setText(currentfullAddress);


                }
            }
        });

    }


}
