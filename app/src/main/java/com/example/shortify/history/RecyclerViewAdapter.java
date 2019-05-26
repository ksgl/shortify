package com.example.shortify.history;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shortify.R;
import com.example.shortify.common.Util;
import com.example.shortify.database.LinkModel;

import java.util.List;

import static android.content.Context.CLIPBOARD_SERVICE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<LinkModel> linkModelList;
    private LinkViewModel viewModel;
    private Context ctx;


    public RecyclerViewAdapter(List<LinkModel> linkModelList, LinkViewModel viewModel, Context ctx) {
        this.linkModelList = linkModelList;
        this.viewModel = viewModel;
        this.ctx = ctx;
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

        holder.shortURLTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final android.content.ClipboardManager clipboardManager = (ClipboardManager) ctx.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Short URL", linkModel.getShortURL());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(ctx, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.ShowToast(ctx, "kek");
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = linkModel.getShortURL();
//                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Short link");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
//                sharingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(Intent.createChooser(sharingIntent, "Share via"));

            }

        holder.copyImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                final android.content.ClipboardManager clipboardManager = (ClipboardManager) ctx.getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Short URL", linkModel.getShortURL());
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(ctx, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
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
        private ImageButton shareBtn;
        private ImageButton copyImageButton;

        RecyclerViewHolder(View view) {
            super(view);
            originalURLTextView = view.findViewById(R.id.original_url_tv);
            shortURLTextView = view.findViewById(R.id.short_url_tv);
            dateTextView = view.findViewById(R.id.date_tv);
            starredImageButton = view.findViewById(R.id.starred_ib);
            shareBtn = view.findViewById(R.id.shareBtn);
            copyImageButton = view.findViewById(R.id.copy_ib);
        }
    }
}