package com.example.shortify.history;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.shortify.R;
import com.example.shortify.database.LinkModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private LinkViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    private Date date;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
// hey

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        this.viewModel = ViewModelProviders.of(this).get(LinkViewModel.class);

        Context ctx = getApplicationContext();


//        this.viewModel.add(new LinkModel("yandex.ru","y.r", "23",true));
//        this.viewModel.add(new LinkModel("mmmmmmm.ru","m.r","55",true));

        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<LinkModel>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

        this.viewModel.getLinkList().observe(HistoryActivity.this, new Observer<List<LinkModel>>() {
            @Override
            public void onChanged(@Nullable List<LinkModel> l) {
                recyclerViewAdapter.addItems(l);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewModel.removeAll();
            }
        });
    }

    public void addLink(String longUrl, String shortUrl) {
        Date date = new Date();
        String strDate = date.toString();
        this.viewModel.add(new LinkModel(longUrl,shortUrl, strDate, false));

    }
}