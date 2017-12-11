package com.example.user.logintest;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chentingyu on 2017/12/11.
 */

public class AsyncRecommendationTask extends AsyncTask<String, Integer, String> {
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    @Override
    protected String doInBackground(String... params) {
        Response response = null;
        RequestBody formBody ;
        formBody = new FormBody.Builder()
                    .add("userId", params[0])
                    .add("lat", params[1])
                    .add("lng", params[2]).build();

        Request request = new Request.Builder().url("http://140.112.107.125:47155/html/python/uservar.php")
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
