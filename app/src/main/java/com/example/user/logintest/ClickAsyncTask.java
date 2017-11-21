package com.example.user.logintest;

import android.os.AsyncTask;

import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chentingyu on 2017/11/17.
 */

public class ClickAsyncTask  extends AsyncTask<String, Integer, String> {

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();
    @Override
    protected String doInBackground(String... params) {
        Response response = null;
        RequestBody formBody ;
        if (params[3]=="-1"){
            formBody = new FormBody.Builder()
                    .add("userId", params[0])
                    .add("audioId", params[1])
                    .add("clicks", params[2]).build();
        }else {
            formBody = new FormBody.Builder()
                    .add("userId", params[0])
                    .add("audioId", params[1])
                    .add("clicks", params[2])
                    .add("ratings", params[3]).build();

        }

        Request request = new Request.Builder().url(params[4])
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

