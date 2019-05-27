package com.example.shortify;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shortify.history.HistoryActivity;
import com.example.shortify.history.LinkViewModel;
import com.example.shortify.http.RequestProcessor;

public class MainActivity extends AppCompatActivity {

    LinkViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.viewModel = ViewModelProviders.of(this).get(LinkViewModel .class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button shortenBtn = findViewById(R.id.shorten_btn);

        Spinner spinner = findViewById(R.id.services_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ImageButton shareBtn = findViewById(R.id.shareBtn);
//        shareBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
//                sharingIntent.setType("text/plain");
//                String shareBody = "kek";
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Short link");
//                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                startActivity(Intent.createChooser(sharingIntent, "Share via"));
//            }
//        });

        TextView textView = findViewById(R.id.short_url);

        shortenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText url = findViewById(R.id.url_et);
                shortenBtn.onEditorAction(EditorInfo.IME_ACTION_DONE);
                try {
                    new RequestProcessor(getApplicationContext(), textView, viewModel)
                        .SetParams(spinner.getSelectedItem().toString(), url.getText().toString())
                        .CreateRequest()
                        .Send(shortStr -> {
                            runOnUiThread(new Runnable() {
                                final public void run() {
                                    shareBtn.setVisibility(View.VISIBLE);
                                    textView.setText(shortStr);
                                    shareBtn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                                            sharingIntent.setType("text/plain");
                                            String shareBody = shortStr;
                                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                                        }
                                    });
                                }
                            });
                        });
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