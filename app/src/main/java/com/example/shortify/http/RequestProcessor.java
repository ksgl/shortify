package com.example.shortify.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.OkHttpClient;
import okhttp3.MultipartBody;
import okhttp3.Response;
import android.util.Log;
import android.widget.Toast;

import org.json.*;

// RequestProcessor generates API request counting on service selected
final public class RequestProcessor {
    private String longUrl = "";
    private String shortUrl = "";
    private String service = "";
    private Request APIRequest;
    private Context ctx;

    public RequestProcessor(Context ctx) {
        this.ctx = ctx;
    }

    public RequestProcessor SetParams(String service, String longUrl) {
        this.service = service;
        this.longUrl = longUrl;
        return this;
    }

    public RequestProcessor CreateRequest() throws IllegalArgumentException {
        String bitlyAPIToken = "fc11278ca50671dbd19332c8698026c7a9cd4123";
        String reqUrl ="";
        Request.Builder request = new Request.Builder();

        switch (this.service) {
            case "bit.ly":
                reqUrl = "https://api-ssl.bitly.com/v3/shorten" +"?access_token="
                        + bitlyAPIToken + "&longUrl=" + this.longUrl;
                this.APIRequest = request.url(reqUrl).build();
                break;
            case "cleanuri":
                reqUrl = "https://cleanuri.com/api/v1/shorten";
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("url", this.longUrl)
                        .build();
                this.APIRequest = request.post(requestBody).url(reqUrl).build();
                break;
            default:
                throw new IllegalArgumentException("Incorrect service " + this.service);
        }

        return this;
    }

    public String ParseResponse(Response res) throws IllegalArgumentException {
        String shortUrl = "";
        JSONObject json = null;

        try {
            String jsonData = res.body().string();
            json = new JSONObject(jsonData);
        } catch (JSONException e) {
            Log.e("CRITICAL", "unexpected JSON exception while creating jsonObj", e);
        } catch (IOException e) {
            e.printStackTrace();
        }



        switch (this.service) {
            case "bit.ly":
                try {
                    if (Integer.parseInt(json.get("status_code").toString()) != 200) {
                        showToast("Invlaid URL!");
                        return "";
                    }
                } catch (JSONException e) {
                    showToast("Something went wrong");
                    Log.e("CRITICAL", "unexpected JSON exception while getting bitly status_code");
                }
                try {
                    JSONObject data = (JSONObject)json.get("data");
                    shortUrl = data.get("url").toString();
                } catch (JSONException e) {
                    Log.e("CRITICAL", "unexpected JSON exception while getting bitly data");
                }
                break;
            case  "cleanuri":
                if (res.code() != 200) {
                    showToast("Invlaid URL!");
                    return "";
                }
                try {
                    shortUrl = json.get("result_url").toString();
                } catch (JSONException e) {
                    Log.e("CRITICAL", "unexpected JSON exception while getting cleanuri data");
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect service " + this.service);

        }

        this.shortUrl = shortUrl;
        Toast.makeText(this.ctx, shortUrl, Toast.LENGTH_SHORT).show();
        return this.shortUrl;
    }

    public void Send() {
        final Handler handler = new Handler();
        OkHttpClient client = new OkHttpClient();

        client.newCall(this.APIRequest).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                String body = response.body().toString();
//                Log.i(); // no need inside run()
                handler.post(new Runnable() {
                    @Override
                    public void run() {
//                        Log.d("BODY: ", body); // must be inside run()
                        String res = ParseResponse(response);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String exception = e.toString();
//                Log.e(LOG_TAG, exception); // no need inside run()
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast("Network error");
                        Log.d("EXCEPTION: ", exception); // must be inside run()
                    }
                });
            }
        });

    }

    private final void showToast(String text) {
        Toast.makeText(this.ctx, text, Toast.LENGTH_SHORT).show();
    }

}
