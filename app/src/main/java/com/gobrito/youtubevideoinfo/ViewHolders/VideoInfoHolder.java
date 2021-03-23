package com.gobrito.youtubevideoinfo.ViewHolders;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gobrito.youtubevideoinfo.AsyncTasks.DownloadImageTask;
import com.gobrito.youtubevideoinfo.R;
import com.google.android.material.textview.MaterialTextView;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.VideoContentDetails;

import java.text.DateFormat;
import java.util.Date;

public class VideoInfoHolder extends RecyclerView.ViewHolder {
    View itemView;
    ImageView thumbnailView;
    MaterialTextView titleTextView, authorTextView, publishedInTextView;

    public VideoInfoHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;

        thumbnailView = itemView.findViewById(R.id.item_video_thumbnail);
        titleTextView = itemView.findViewById(R.id.item_video_title);
        authorTextView = itemView.findViewById(R.id.item_video_author);
        publishedInTextView = itemView.findViewById(R.id.item_video_published_in);
    }

    public void bind(VideoContentDetails model, ThumbnailDetails thumbnailModel) {
        new DownloadImageTask(thumbnailView).execute(thumbnailModel);
    }

    public void bind(SearchResult result) {
        SearchResultSnippet snippet = result.getSnippet();
        titleTextView.setText(snippet.getTitle());
        authorTextView.setText(snippet.getChannelTitle());
        publishedInTextView.setText(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(new Date(snippet.getPublishedAt().getValue())));

        new DownloadImageTask(thumbnailView).execute(snippet.getThumbnails());
    }
}
