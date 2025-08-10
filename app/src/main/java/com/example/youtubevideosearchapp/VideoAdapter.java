package com.example.youtubevideosearchapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoItem> videoList;

    // ✅ المنشئ الجديد: ضروري لاستخدام new VideoAdapter(new ArrayList<>())
    public VideoAdapter(List<VideoItem> videoList) {
        this.videoList = videoList != null ? videoList : new ArrayList<>();
    }

    // دالة لتحديث القائمة
    public void updateList(List<VideoItem> newList) {
        this.videoList = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoItem video = videoList.get(position);
        holder.title.setText(video.title);
        holder.channel.setText(video.channelTitle);
        holder.date.setText(video.publishTime.substring(0, 10)); // فقط التاريخ
        holder.description.setText(video.description);

        Glide.with(holder.thumbnail.getContext())
                .load(video.thumbnailUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_dialog_alert)
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    // ViewHolder
    static class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView title, channel, date, description;
        ImageView thumbnail;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.video_title);
            channel = itemView.findViewById(R.id.video_channel);
            date = itemView.findViewById(R.id.video_date);
            description = itemView.findViewById(R.id.video_description);
            thumbnail = itemView.findViewById(R.id.video_thumbnail);
        }
    }
}