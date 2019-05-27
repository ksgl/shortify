package com.example.shortify.history;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.shortify.R;
import com.example.shortify.database.LinkModel;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private LinkViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    private boolean favsClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Slidr.attach(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        this.viewModel = ViewModelProviders.of(this).get(LinkViewModel.class);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<LinkModel>(), this.viewModel, this);


        final RecyclerView.LayoutManager layout = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel.getToShow().observe(HistoryActivity.this, new Observer<List<LinkModel>>() {
            @Override
            public void onChanged(@Nullable List<LinkModel> l) {
                recyclerViewAdapter.addItems(l);
            }
        });

        FloatingActionButton remove = findViewById(R.id.fab);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.removeAll();
            }
        });

        FloatingActionButton fav = findViewById(R.id.favourites_fab);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    viewModel.getFavouritesList().observe(HistoryActivity.this, new Observer<List<LinkModel>>() {
                        @Override
                        public void onChanged(@Nullable List<LinkModel> l) {
                            if (l == null || l.isEmpty()) {
                                Toast.makeText(view.getContext(), "No favourites", Toast.LENGTH_SHORT).show();
                                viewModel.getFavouritesList().removeObserver(this);
                            } else {
                                viewModel.switchToFavorite();
                            }
                        }
                    });
            }
        });
    }
}
