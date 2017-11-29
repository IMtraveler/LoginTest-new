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


public class MyMusicAsyncTask extends AsyncTask<String, Integer, String>{

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    @Override
    protected String doInBackground(String... params) {
        Response response = null;

        RequestBody formBody = new FormBody.Builder()
                .add("account", params[0]).build();

        Request request = new Request.Builder().url(params[1])
                .post(formBody)
                .build();
        try {
            response = client.newCall(request).execute();
            final String resStr = response.body().string();

            return resStr;
        } catch (Exception e) {
            return e.toString();
        }finally {
            response.close();
            response.body().close();
        }
    }
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }


}
