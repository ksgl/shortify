package com.example.shortify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.shortify.history.HistoryActivity;
import com.example.shortify.http.POST;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shortenBtn = findViewById(R.id.shorten_btn);

        shortenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText url = findViewById(R.id.url_et);
//                JSONObject jsonPayload = new JSONObject();
                try {
//                    jsonPayload.put("access_token", "fc11278ca50671dbd19332c8698026c7a9cd4123");
//                    jsonPayload.put("longUrl", url.getText().toString());

//                    new POST().post(jsonPayload.toString());

                    new POST().post(url.getText().toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button historyBtn = findViewById(R.id.history_btn);

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                startActivity(intent);
            }
        });
    }
}