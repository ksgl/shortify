package com.example.shortify.history;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.support.design.widget.FloatingActionButton;

import com.example.shortify.R;
import com.example.shortify.database.LinkModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private LinkViewModel viewModel;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;

    private Date date;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        calendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, HistoryActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        viewModel = ViewModelProviders.of(this).get(LinkViewModel.class);

//        viewModel.add(new LinkModel("ddddddddddddd","short",date,true));
//        viewModel.add(new LinkModel("lonassafdasfsdfg","shodsfrt",date,true));
        viewModel.add(new LinkModel("bubu","shaaaaaort",date,true));

//        Toast toast = Toast.makeText(this, viewModel.toString(), Toast.LENGTH_SHORT);
//        toast.show();

        recyclerView = findViewById(R.id.recycler_view);
        recyclerViewAdapter = new RecyclerViewAdapter(new ArrayList<LinkModel>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerViewAdapter);

        viewModel.getLinkList().observe(HistoryActivity.this, new Observer<List<LinkModel>>() {
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        date = calendar.getTime();
    }
}
