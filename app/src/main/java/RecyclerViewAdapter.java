package com.example.shortify;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shortify.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<LinkModel> linkModelList;
    private View.OnLongClickListener longClickListener;

    public RecyclerViewAdapter(List<LinkModel> linkModelList, View.OnLongClickListener longClickListener) {
        this.linkModelList = linkModelList;
        this.longClickListener = longClickListener;
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
        holder.shortlURLTextView.setText(linkModel.getShortURL());
        holder.dateTextView.setText(linkModel.getDate().toString());
        holder.starredTextView.setText(Boolean.toString(linkModel.getStarred()));
        holder.itemView.setOnLongClickListener(longClickListener);
    }

    @Override
    public int getItemCount() {
        return linkModelList.size();
    }

    public void addItems(List<LinkModel> borrowModelList) {
        this.linkModelList = linkModelList;
        notifyDataSetChanged();
    }

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView originalURLTextView;
        private TextView shortlURLTextView;
        private TextView dateTextView;
        private TextView starredTextView;

        RecyclerViewHolder(View view) {
            super(view);
            originalURLTextView = (TextView) view.findViewById(R.id.original_url_tv);
            shortlURLTextView = (TextView) view.findViewById(R.id.short_url_tv);
            dateTextView = (TextView) view.findViewById(R.id.date_tv);
            starredTextView = (TextView) view.findViewById(R.id.starred_tv);
        }
    }
}