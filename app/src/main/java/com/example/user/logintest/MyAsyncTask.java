package com.example.user.logintest;

/**
 * Created by chentingyu on 2017/8/17.
 */


import android.os.AsyncTask ;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.concurrent.TimeUnit ;


public class MyAsyncTask extends AsyncTask<String, Integer, String>{

private static final OkHttpClient client = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build();
@Override
protected String doInBackground(String... params) {
        try {
        RequestBody formBody = new FormBody.Builder()
        .add("lat", params[0])
        .add("lng", params[1]).build();

        Request request = new Request.Builder().url(params[2])
        .post(formBody)
        .build();

        Response response = client.newCall(request).execute();
final String resStr = response.body().string();

        return resStr;
        } catch (Exception e) {
        return e.toString();
        }
        }
@Override
protected void onPostExecute(String result) {
        super.onPostExecute(result);
        }

        }
