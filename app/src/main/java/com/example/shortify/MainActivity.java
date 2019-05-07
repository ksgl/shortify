package com.example.shortify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.shortify.history.HistoryActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shortenBtn = findViewById(R.id.shorten_btn);

        shortenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), HistoryActivity.class);
                startActivity(intent);

//                Toast toast = Toast.makeText(v.getContext(), "ah!", Toast.LENGTH_SHORT);
//                toast.show();
            }
        });
    }
}
