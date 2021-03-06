package com.trigger.shortify.history;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trigger.shortify.R;
import com.trigger.shortify.database.LinkModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryFragment extends Fragment {

    private LinkViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    public HistoryFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_history, container, false);

        this.viewModel = ViewModelProviders.of(this).get(LinkViewModel.class);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<LinkModel>(), this.viewModel, this.getContext());


        final RecyclerView.LayoutManager layout = new GridLayoutManager(this.getContext(), 1);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel.getLinkList().observe(HistoryFragment.this, new Observer<List<LinkModel>>() {
            @Override
            public void onChanged(@Nullable List<LinkModel> l) {
                recyclerViewAdapter.addItems(l);
            }
        });

        return view;
    }
}
