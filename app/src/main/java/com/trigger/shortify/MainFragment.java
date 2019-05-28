package com.trigger.shortify;

import android.arch.lifecycle.ViewModelProviders;
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
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.trigger.shortify.history.LinkViewModel;
import com.trigger.shortify.http.RequestProcessor;

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
        this.viewModel = ViewModelProviders.of(this).get(LinkViewModel.class);

        Button shortenBtn = view.findViewById(R.id.shorten_btn);

        Spinner spinner = view.findViewById(R.id.services_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getContext(),
                R.array.services, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ImageButton shareBtn = view.findViewById(R.id.shareBtn);
        TextView textView = view.findViewById(R.id.short_url);

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
                                        shareBtn.setVisibility(View.INVISIBLE);                                        textView.setVisibility(View.VISIBLE);
                                        textView.setVisibility(View.INVISIBLE);
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
