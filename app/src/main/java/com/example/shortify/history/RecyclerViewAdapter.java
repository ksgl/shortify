package com.example.shortify.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shortify.R;
import com.example.shortify.database.LinkModel;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<LinkModel> linkModelList;
    public RecyclerViewAdapter(List<LinkModel> linkModelList) {
        this.linkModelList = linkModelList;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        LinkModel linkModel = linkModelList.get(position);
        holder.originalURLTextView.setText(linkModel.getOriginalURL());
        holder.shortURLTextView.setText(linkModel.getShortURL());
        holder.starredTextView.setText(Boolean.toString(linkModel.getStarred()));
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
        private TextView starredTextView;

        RecyclerViewHolder(View view) {
            super(view);
            originalURLTextView = view.findViewById(R.id.original_url_tv);
            shortURLTextView = view.findViewById(R.id.short_url_tv);
            starredTextView = view.findViewById(R.id.starred_tv);
        }
    }
}