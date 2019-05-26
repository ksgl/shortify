package com.example.shortify.http;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class POST {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
    private static final String LOG_TAG = "OkHTTPServer:";
    final Handler handler = new Handler();

    public void post(String json) throws IOException {
        String uri = "https://api-ssl.bitly.com/v3/shorten?access_token=fc11278ca50671dbd19332c8698026c7a9cd4123&longUrl=" + json;
        Request request = new Request.Builder()
                .url(uri)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                String body = response.toString();
                Log.i(LOG_TAG, body); // no need inside run()
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("BODY: ", body); // must be inside run()
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String exception = e.toString();
                Log.e(LOG_TAG, exception); // no need inside run()
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("EXCEPTION: ", exception); // must be inside run()
                    }
                });
            }
        });
    }
}

