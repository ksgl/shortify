package com.trigger.shortify;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.trigger.shortify.history.LinkViewModel;
import com.trigger.shortify.http.RequestProcessor;

public class MainFragment extends Fragment {

    LinkViewModel viewModel;
    private SharedPreferences prefs;
    private String shortUrl;
    private Boolean showUrl;

    public MainFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity().getBaseContext());
        shortUrl = prefs.getString("short_url", "");
        showUrl = prefs.getBoolean("show_url", false);
    }

    @Override
    public void onPause() {
        super.onPause();

        SharedPreferences.Editor prefEditor = PreferenceManager
                .getDefaultSharedPreferences(this.getActivity()
                        .getBaseContext()).edit();

        prefEditor.putString("short_url", shortUrl);
        if (shortUrl.isEmpty()) {
            prefEditor.putBoolean("show_url", false);
        } else {
            prefEditor.putBoolean("show_url", true);
        }
        prefEditor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = view.findViewById(R.id.short_url);
        ImageButton shareBtn = view.findViewById(R.id.shareBtn);

        if (showUrl) {
            textView.setText(shortUrl);
            shareBtn.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
        }

        this.viewModel = ViewModelProviders.of(this).get(LinkViewModel.class);

        Button shortenBtn = view.findViewById(R.id.shorten_btn);

        Spinner spinner = view.findViewById(R.id.services_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        shortenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressBar loader = view.findViewById(R.id.loader);
                shareBtn.setVisibility(View.INVISIBLE);
                loader.setVisibility(View.VISIBLE);
                EditText url = view.findViewById(R.id.url_et);
                textView.setVisibility(View.INVISIBLE);
                shortenBtn.onEditorAction(EditorInfo.IME_ACTION_DONE);
                try {
                    new RequestProcessor(getContext(), textView, viewModel)
                        .SetParams(spinner.getSelectedItem().toString(), url.getText().toString())
                        .CreateRequest()
                        .Send(shortStr -> {
                            getActivity().runOnUiThread(new Runnable() {
                                final public void run() {
                                    loader.setVisibility(View.INVISIBLE);
                                    if (!shortStr.isEmpty()){
                                        textView.setVisibility(View.VISIBLE);
                                        shareBtn.setVisibility(View.VISIBLE);
                                    } else {
                                        shareBtn.setVisibility(View.INVISIBLE);
                                        textView.setVisibility(View.INVISIBLE);
                                    }
                                    shortUrl = shortStr;
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

        return view;
    }
}
