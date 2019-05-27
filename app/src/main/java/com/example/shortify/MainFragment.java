package com.example.shortify;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.shortify.history.LinkViewModel;
import com.example.shortify.http.RequestProcessor;

public class MainFragment extends Fragment {

    LinkViewModel viewModel;

    public MainFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        Button shortenBtn = view.findViewById(R.id.shorten_btn);

        Spinner spinner = view.findViewById(R.id.services_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ImageButton shareBtn = view.findViewById(R.id.shareBtn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "kek";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Short link");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        TextView textView = view.findViewById(R.id.short_url);

        shortenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText url = view.findViewById(R.id.url_et);
                shortenBtn.onEditorAction(EditorInfo.IME_ACTION_DONE);
                try {
                    new RequestProcessor(getContext(), textView, viewModel)
                        .SetParams(spinner.getSelectedItem().toString(), url.getText().toString())
                        .CreateRequest()
                        .Send(shortStr -> {
                            getActivity().runOnUiThread(new Runnable() {
                                final public void run() {
                                    if (!shortStr.isEmpty()){
                                        shareBtn.setVisibility(View.VISIBLE);
                                    } else {
                                        shareBtn.setVisibility(View.INVISIBLE);
                                    }
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
