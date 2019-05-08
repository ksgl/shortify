package com.example.shortify.http;

import android.os.AsyncTask;
import android.util.Log;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class POST {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public void post(String json) {
        new postAsyncTask(client).execute(json);
    }

    public void showResponse(String response) {
    }

    private static class postAsyncTask extends AsyncTask<String, Void, String> {
        private OkHttpClient c;

        postAsyncTask(OkHttpClient client) {
            c = client;
        }

        @Override
        protected String doInBackground(String... json) {
//            RequestBody body = RequestBody.create(JSON, json[0]);
            String temp = "https://api-ssl.bitly.com/v3/shorten?access_token=fc11278ca50671dbd19332c8698026c7a9cd4123&longUrl=" + json[0];
            Request request = new Request.Builder()
                            .url(temp)
//                            .post(body)
                            .build();


            try (Response response = c.newCall(request).execute()) {
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String result) {
            Log.i("response body = ", result);
        }
    }
}