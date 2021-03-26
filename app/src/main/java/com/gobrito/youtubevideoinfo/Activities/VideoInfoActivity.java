package com.gobrito.youtubevideoinfo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.gobrito.youtubevideoinfo.AsyncTasks.YoutubeVideoByIdTask;
import com.gobrito.youtubevideoinfo.R;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;

import java.text.DateFormat;
import java.util.Date;

public class VideoInfoActivity extends AppCompatActivity {
    TextView videoInfoId, videoInfoEtag, videoInfoSnippetPublishedAt;
    TextView videoInfoSnippetChannel, videoInfoSnippetTitle, videoInfoSnippetDescription;
    TextView videoInfoSnippetTags;
    Video video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);

        videoInfoId = findViewById(R.id.video_info_id);
        videoInfoEtag = findViewById(R.id.video_info_etag);
        videoInfoSnippetPublishedAt = findViewById(R.id.video_info_snippet_published_at);
        videoInfoSnippetChannel = findViewById(R.id.video_info_snippet_channel);
        videoInfoSnippetTitle = findViewById(R.id.video_info_snippet_title);
        videoInfoSnippetDescription = findViewById(R.id.video_info_snippet_description);
        videoInfoSnippetTags = findViewById(R.id.video_info_snippet_tags);

        videoInfoSnippetDescription.setOnClickListener(v -> {
            if(video != null) {
                String description = video.getSnippet().getDescription();
                String currentText = videoInfoSnippetDescription.getText().toString();

                if(currentText.length() == 90)
                    videoInfoSnippetDescription.setText(description);
                else
                    videoInfoSnippetDescription.setText(description.substring(0, 30) + "...");
            }
        });

        Intent intent = getIntent();
        String videoId = intent.getStringExtra("videoId");

        YoutubeVideoByIdTask task = new YoutubeVideoByIdTask(videoId, video -> {
            VideoInfoActivity.this.video = video;

            VideoSnippet snippet = video.getSnippet();
            Date publishDate = new Date(snippet.getPublishedAt().getValue());

            videoInfoId.setText("ID: " + video.getId());
            videoInfoEtag.setText("ETag: " + video.getEtag());
            videoInfoSnippetPublishedAt.setText("Publicado em: " + DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(publishDate));

            videoInfoSnippetChannel.setText(String.format("Canal: %s - %s", snippet.getChannelId(), snippet.getChannelTitle()));
            videoInfoSnippetTitle.setText("Título: " + snippet.getTitle());
            videoInfoSnippetDescription.setText("Descrição: " + snippet.getDescription().substring(0, 30) + "...");
            videoInfoSnippetTags.setText("Tags: " + TextUtils.join(",", snippet.getTags()));
        });
        task.execute();
    }
}