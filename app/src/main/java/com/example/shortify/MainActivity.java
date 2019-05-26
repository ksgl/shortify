package com.example.shortify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.shortify.history.HistoryActivity;
import com.example.shortify.http.RequestGenerator;
import com.example.shortify.http.POST;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shortenBtn = findViewById(R.id.shorten_btn);

        Spinner spinner = (Spinner) findViewById(R.id.services_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        shortenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText url = findViewById(R.id.url_et);
//                JSONObject jsonPayload = new JSONObject();
                try {
                    new RequestGenerator(getApplicationContext())
                            .SetParams(spinner.getSelectedItem().toString(), url.getText().toString())
                            .CreateRequest()
                            .Send();
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