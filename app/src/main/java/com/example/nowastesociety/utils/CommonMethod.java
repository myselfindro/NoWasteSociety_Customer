package com.example.nowastesociety.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.nowastesociety.session.SessionManager;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommonMethod {
    public static boolean isRefreshNeeded=false;
    public static void AddBatchcount(Context mContext, TextView textView, String count, int type) {
        SessionManager manager = new SessionManager(mContext);
        String tc = count;
//        if(manager.getBatchcount().length()>0 && count.length()>0){
//             tc = (Integer.parseInt(manager.getBatchcount())+Integer.parseInt(count))+"";
//        }else{
//            tc = count;
//        }
        switch (type) {
            case 1:
                if (manager.getBatchcount().length() > 0 && count.length() > 0) {
                    tc = (Integer.parseInt(manager.getBatchcount()) + Integer.parseInt(count)) + "";
                } else {
                    tc = count;
                }
                break;
            case 2:
                tc = count;
                break;
            case 3:
                if (manager.getBatchcount().length() > 0 && count.length() > 0) {
                    tc = (Integer.parseInt(manager.getBatchcount()) - Integer.parseInt(count)) + "";
                } else {
                    tc = count;
                }
                break;
        }
        if(tc.equals("0"))tc= "";
        manager.setBatchcount(tc);
        textView.setText(tc);
    }

    public static void delAllCart(Context mContext, TextView textView, String count) {
        SessionManager manager = new SessionManager(mContext);
        String tc = count;
        manager.setBatchcount(tc);
        textView.setText(tc);
    }

    public static void removeBatchcount(Context mContext, TextView textView, String count) {
        SessionManager manager = new SessionManager(mContext);
        String tc = "";
        if (count.length() > 0 && count.equals("0")) {
            tc = "";
        } else {
            tc = count;
        }
        if(count.equals("0"))tc= "";
        manager.setBatchcount(tc);
        textView.setText(tc);
    }

    public static void test(){

        AsyncTask<String,String,String> tets = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "userType=customer&customerId=5ee80e9b59451837fa60d3d7&vendorId=5f219079af26827ae127bb2e&price=85&specialInstruction=&discount=&finalPrice=85&promocodeId=&offerId=&items=[{\"name\":\"Large Box\",\"quantity\":1,\"price\":\"85\",\"itemId\":\"5f241875e5458d26ccb33a60\"}]&latitude=22.7285332&longitude=88.4758555&appType=ANDROID&deliveryPreference=PICKUP&addressId=5f474ae74ea6724b82561e03&orderType=NORMAL&cartId=5f47d1f09f19ed2b721aab87");
                Request request = new Request.Builder()
                        .url("https://nodeserver.mydevfactory.com:2100/api/customer/postOrder")
                        .method("POST", body)
                        .addHeader("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWJqZWN0IjoiNWVlODBlOWI1OTQ1MTgzN2ZhNjBkM2Q3IiwidXNlciI6IkNVU1RPTUVSIiwiaWF0IjoxNTk4NTQ5ODAxLCJleHAiOjE2MDExNDE4MDF9.xtyfjGzPchklK5VU-HpVwdMUp5zmu_f3g-MSnSrc-HY")
                        .addHeader("Content-Type", "application/x-www-form-urlencoded")
                        .addHeader("Cookie", "id=s%3ABzIBHlCQfdxcDps70fMUZWGGnbeZMLgM.urz4DRBm8R2z1fUIxJ73jjbS8pgH%2Bgv868ceOTsRFfI")
                        .build();
                try {
                    Response response = client.newCall(request).execute();
                    Log.v("response",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        tets.execute();

    }

}
