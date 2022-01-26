package com.example.nowastesociety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Paymentsuccess extends AppCompatActivity {

    TextView orderId;
    String orderid , ordernumber;
    ImageView imgSearch;
    ImageView btn_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paymentsuccess);
        orderId = (TextView)findViewById(R.id.orderId);
        imgSearch = (ImageView)findViewById(R.id.imgSearch);
        btn_back = (ImageView)findViewById(R.id.btn_back);


        Intent intent = getIntent();
        orderid = intent.getStringExtra("orderId");
        ordernumber = intent.getStringExtra("orderNo");

        orderId.setText("Your Order ID is "+ordernumber);


        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Paymentsuccess.this, Orderdetails.class);
                intent.putExtra("orderId", orderid);
                startActivity(intent);

            }
        });


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Paymentsuccess.this, Cartdetails.class);
                startActivity(intent);
            }
        });


        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Paymentsuccess.this, Search.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}