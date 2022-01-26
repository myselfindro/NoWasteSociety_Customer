package com.example.nowastesociety;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Paymentwebview extends AppCompatActivity {

    ImageView btn_back;
    private static final String TAG = "Myapp";
    private WebView myWebView;
    String authToken;
    SessionManager sessionManager;
    private static final String SHARED_PREFS = "sharedPrefs";
    private Timer mTimer;



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentwebview);
        btn_back = (ImageView)findViewById(R.id.btn_back);
        myWebView = (WebView) findViewById(R.id.webview);

        sessionManager = new SessionManager(getApplicationContext());
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        authToken = sharedPreferences.getString("authToken", "");
        Log.d(TAG, "authToken-->" + authToken);


        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        myWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);

        myWebView.loadDataWithBaseURL("https://nodeserver.mydevfactory.com:2100/payment/generalInfo", url, "text/html", "UTF-8", null);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });

        checkPaymentStatus();
    }

    private void checkPaymentStatus(){
        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        PaymentStatus();
                    }
                });
            }
        },1000,15000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void PaymentStatus(){

        if (CheckConnectivity.getInstance(getApplicationContext()).isOnline()) {


           // showProgressDialog();


            JSONObject params = new JSONObject();

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, AllUrl.Paymentstatuscheck, params, response -> {

                Log.i("Response-->", String.valueOf(response));

                try {
                    JSONObject result = new JSONObject(String.valueOf(response));
                    String msg = result.getString("message");
                    Log.d(TAG, "msg-->" + msg);
                    boolean success = result.getBoolean("success");
                    if (success) {

                        JSONObject response_data = result.getJSONObject("response_data");
                        String paymentStatus = response_data.getString("paymentStatus");
                        JSONObject orderId = response_data.getJSONObject("orderId");
                        String orderid = orderId.getString("_id");
                        String orderNo = orderId.getString("_id");
                        if (paymentStatus.equals("2")){

                            if(mTimer!=null){
                                mTimer.cancel();
                            }
                            Intent intent = new Intent(Paymentwebview.this, Paymentsuccess.class);
                            intent.putExtra("orderId", orderid);
                            intent.putExtra("orderNo", orderNo);
                            startActivity(intent);

                        }




                    } else {


                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                hideProgressDialog();

                //TODO: handle success
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Paymentwebview.this, "Invalid", Toast.LENGTH_SHORT).show();

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