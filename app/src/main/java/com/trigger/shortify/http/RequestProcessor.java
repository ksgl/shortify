package com.trigger.shortify.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.trigger.shortify.MainActivity;
import com.trigger.shortify.R;
import com.trigger.shortify.common.Util;
import com.trigger.shortify.database.LinkModel;
import com.trigger.shortify.history.LinkViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// RequestProcessor generates API request counting on service selected
final public class RequestProcessor extends MainActivity {
    private String longUrl = "";
    private String shortUrl = "";
    private String service = "";
    private Request APIRequest;
    private Context ctx;
    private TextView view_url;
    private LinkViewModel viewModel;

    public RequestProcessor(Context ctx, TextView view_url, LinkViewModel viewModel) {
        this.ctx = ctx;
        this.view_url = view_url;
        this.viewModel = viewModel;
    }

    public RequestProcessor SetParams(String service, String longUrl) {
        this.service = service;
        this.longUrl = longUrl;
        return this;
    }

    public RequestProcessor CreateRequest() throws IllegalArgumentException {
        String bitlyAPIToken = "fc11278ca50671dbd19332c8698026c7a9cd4123";
        String rebrandlyAPIToken = "02fcb586ced64eafa4436815b6667b91";

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
            case "rebrandly":
                reqUrl = "https://api.rebrandly.com/v1/links/new"
                        + "?apikey=" + rebrandlyAPIToken
                        + "&destination=" + longUrl;
                this.APIRequest = request.url(reqUrl).build();
                break;
            case "clck.ru":
                reqUrl = "https://clck.ru/--" + "?url=" + longUrl;
                this.APIRequest = request.url(reqUrl).build();
                break;
            case "random":
                reqUrl = "http://tiny-url.info/api/v1/random";
                RequestBody body = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("url", this.longUrl)
                        .addFormDataPart("format", "json")
                        .build();
                this.APIRequest = request.post(body).url(reqUrl).build();
                break;
            default:
                throw new IllegalArgumentException("Incorrect service " + this.service);
        }

        return this;
    }

    public String ParseResponse(Response res) throws IllegalArgumentException {
        JSONObject json = null;

        if (service.equals("clck.ru")) {
            if (!res.header("Content-Type").equals("text/javascript; charset=utf-8")) {
                showReqErrorToast();
                return "";
            }

            try {
                this.shortUrl = res.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return processShortUrl();
        }

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
                        showReqErrorToast();
                        return "";
                    }
                } catch (JSONException e) {
                    Util.ShowToast(ctx, "Something went wrong");
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
                    showReqErrorToast();
                    return "";
                }
                try {
                    shortUrl = json.get("result_url").toString();
                } catch (JSONException e) {
                    Log.e("CRITICAL", "unexpected JSON exception while getting cleanuri data");
                }
                break;
            case "rebrandly":
                if (res.code() != 200) {
                    showReqErrorToast();
                    return "";
                }
                try {
                    shortUrl = json.get("shortURL").toString();
                    String isSSL = json.get("https").toString();
                    shortUrl = "https://" + shortUrl;
                } catch (JSONException e) {
                    Log.e("CRITICAL", "unexpected JSON exception while getting rebrandly data");
                }
                break;
            case "random":
                try {
                    shortUrl = json.get("shorturl").toString();
                    if (shortUrl.equals("")) {
                        showReqErrorToast();
                        return "";
                    }
                } catch (JSONException e) {
                    Util.ShowToast(ctx, "Something went wrong");
                    Log.e("CRITICAL", "unexpected JSON exception while getting random status_code");
                }
                break;
            default:
                throw new IllegalArgumentException("Incorrect service " + this.service);

        }

        return processShortUrl();
    }

    private final String processShortUrl() {
        addToHistory();
        setOnTouchListener();
        showUrl(this.shortUrl);
        return  shortUrl;
    }

    private final void showReqErrorToast() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Util.ShowToast(ctx, ctx.getResources().getString(R.string.invalid_url));
            }
        });
    }

    public interface OnShortenListener {
        void onShorten(String shortStr);
    }

    public RequestProcessor Send(OnShortenListener listener) {
        final Handler handler = new Handler();
        OkHttpClient client = new OkHttpClient();

        client.newCall(this.APIRequest).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                String body = response.body().toString();
                String res = ParseResponse(response);
                listener.onShorten(res);
            }

            @Override
            public void onFailure(Call call, IOException e) {
                String exception = e.toString();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Util.ShowToast(ctx, ctx.getResources().getString(R.string.network_error));
                        Log.d("EXCEPTION: ", exception); // must be inside run()
                    }
                });
            }
        });
        return this;
    }

    public String getShortUrl() { return shortUrl; }

    private void showUrl(String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                view_url.setText(url);
            }
        });
    }

    private void addToHistory() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM, HH:mm");
        String longUrlEllipsize;
        if (this.longUrl.length() > 28) {
            longUrlEllipsize = this.longUrl.substring(0,28) + "...";
        } else {
            longUrlEllipsize = this.longUrl;
        }
        this.viewModel.add(new LinkModel(this.longUrl, longUrlEllipsize, this.shortUrl,false, date));
    }

    private void setOnTouchListener() {
        this.view_url.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Util.CopyToClipboard(ctx, shortUrl);
                Util.ShowToast(ctx, ctx.getResources().getString(R.string.copied_to_clipboard));
                return true;
            }
        });
    }

}
