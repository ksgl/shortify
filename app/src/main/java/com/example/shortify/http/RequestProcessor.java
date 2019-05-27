package com.example.shortify.http;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.example.shortify.R;
import com.example.shortify.common.Util;
import com.example.shortify.database.LinkModel;
import com.example.shortify.history.LinkViewModel;

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
final public class RequestProcessor {
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
                        Util.ShowToast(ctx, ctx.getResources().getString(R.string.invalid_url));
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
                    Util.ShowToast(ctx, ctx.getResources().getString(R.string.invalid_url));
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
        addToHistory();
        setOnTouchListener();
        showUrl(this.shortUrl);
        return this.shortUrl;
    }

    public interface OnShortenListener {
        void onShorten(String shortStr);
    }

    public void Send(OnShortenListener listener) {
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

    }

    private  final void showUrl(String url) {
        this.view_url.setText(url);
    }

    private final void addToHistory() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd MMM, HH:mm");
        String strDate = dateFormat.format(date);
        this.viewModel.add(new LinkModel(this.longUrl, this.shortUrl,false, strDate));
    }

//    private final void setShareListener() {
//        this.shareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Util.ShowToast(ctx, "kek");
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = shortUrl;
////                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Short link");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
////                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                ctx.startActivity(Intent.createChooser(sharingIntent, ctx.getResources().getString(R.string.share_via)));
//            }
//        });
//    }

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
