package com.example.shortify.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shortify.R;
import com.example.shortify.database.LinkModel;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<LinkModel> linkModelList;
    private LinkViewModel viewModel;


    public RecyclerViewAdapter(List<LinkModel> linkModelList, LinkViewModel viewModel) {
        this.linkModelList = linkModelList;
        this.viewModel = viewModel;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = inflater.inflate(R.layout.number_card, viewGroup, false);
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        LinkModel linkModel = linkModelList.get(position);

        holder.originalURLTextView.setText(linkModel.getOriginalURL());
        holder.shortURLTextView.setText(linkModel.getShortURL());
        holder.dateTextView.setText(linkModel.getDate());

        if (linkModel.getStarred()) {
            holder.starredImageButton.setBackgroundResource(R.drawable.ic_starred);
        } else {
            holder.starredImageButton.setBackgroundResource(R.drawable.ic_unstarred);
        }

        holder.starredImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                boolean starred = linkModel.getStarred();
                if (starred) {
                    holder.starredImageButton.setBackgroundResource(R.drawable.ic_unstarred);
                } else {
                    holder.starredImageButton.setBackgroundResource(R.drawable.ic_starred);
                }

                linkModel.setStarred(!starred);
                viewModel.changeStarred(linkModel);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return linkModelList.size();
    }

    public void addItems(List<LinkModel> linkModelList) {
        this.linkModelList = linkModelList;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView originalURLTextView;
        private TextView shortURLTextView;
        private TextView dateTextView;
        private ImageButton starredImageButton;

        RecyclerViewHolder(View view) {
            super(view);
            originalURLTextView = view.findViewById(R.id.original_url_tv);
            shortURLTextView = view.findViewById(R.id.short_url_tv);
            dateTextView = view.findViewById(R.id.date_tv);
            starredImageButton = view.findViewById(R.id.starred_ib);
        }
    }
}