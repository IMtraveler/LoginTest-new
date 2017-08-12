package com.example.user.logintest;

import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by chentingyu on 2017/8/1.
 */

public class MySQLConnection {
  public static String executeQuery() {
      String result = "";
      HttpURLConnection urlConnection=null;
      InputStream is =null;
      try {
          URL url=new URL("http://140.112.107.125:47155/html/test.php");
          urlConnection=(HttpURLConnection) url.openConnection();
          urlConnection.setRequestMethod("POST");
          urlConnection.connect();
          is=urlConnection.getInputStream();

          BufferedReader bufReader = new BufferedReader(new InputStreamReader(is, "utf-8"), 8);
          StringBuilder builder = new StringBuilder();
          String line = null;
          while((line = bufReader.readLine()) != null) {
              builder.append(line + "\n");
          }
          is.close();
          result = builder.toString();
      } catch(Exception e) {
          Log.e("log_tag", e.toString());
      }

      return result;
  }

    private static final OkHttpClient client = new OkHttpClient();
    public static String Post(String lat, String lng) {

        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("lat", lat)
                    .add("lng", lng).build();

            Request request = new Request.Builder().url("http://140.112.107.125:47155/html/test.php")
                    .post(formBody)
                    .build();

            Response response = client.newCall(request).execute();
            final String resStr = response.body().string();

            return resStr;
        } catch (Exception e) {
            return e.toString();
        }
    }
}
