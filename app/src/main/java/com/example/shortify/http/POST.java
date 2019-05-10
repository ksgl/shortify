package com.example.shortify.http;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class POST {
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    public String post(String json) {

        //            RequestBody body = RequestBody.create(JSON, json);
        String temp = "https://api-ssl.bitly.com/v3/shorten?access_token=fc11278ca50671dbd19332c8698026c7a9cd4123&longUrl=" + json;
        Request request = new Request.Builder()
                .url(temp)
//                            .post(body)
                .build();


        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
