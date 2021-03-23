package com.gobrito.youtubevideoinfo.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gobrito.youtubevideoinfo.R;
import com.gobrito.youtubevideoinfo.Tuples.SearchThumbnailTuple;
import com.gobrito.youtubevideoinfo.Tuples.VideoThumbnailTuple;
import com.gobrito.youtubevideoinfo.ViewHolders.VideoInfoHolder;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoContentDetails;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter {
    private final Context context;
    private final List<SearchResult> videoList;

    public VideoListAdapter(Context context, List<SearchResult> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_info, parent, false);
        return new VideoInfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchResult result = videoList.get(position);

        ((VideoInfoHolder)holder).bind(result);
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
