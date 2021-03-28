package com.gobrito.youtubevideoinfo.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.gobrito.youtubevideoinfo.AsyncTasks.DownloadImageTask;
import com.gobrito.youtubevideoinfo.AsyncTasks.YoutubeVideoByIdTask;
import com.gobrito.youtubevideoinfo.R;
import com.google.api.services.youtube.model.ThumbnailDetails;

public class ThumbnailsActivity extends AppCompatActivity {
    ImageView thumbnailMaxRes, thumbnailStandard, thumbnailHigh;
    ImageView thumbnailMedium, thumbnailLow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnails);

        Toolbar appBar = findViewById(R.id.video_thumbnail_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thumbnailMaxRes = findViewById(R.id.thumbnail_maxres);
        thumbnailStandard = findViewById(R.id.thumbnail_standard);
        thumbnailHigh = findViewById(R.id.thumbnail_high);
        thumbnailMedium = findViewById(R.id.thumbnail_medium);
        thumbnailLow = findViewById(R.id.thumbnail_low);

        thumbnailMaxRes.setOnClickListener(v -> previewThumbnail(thumbnailMaxRes));
        thumbnailStandard.setOnClickListener(v -> previewThumbnail(thumbnailStandard));
        thumbnailHigh.setOnClickListener(v -> previewThumbnail(thumbnailHigh));
        thumbnailMedium.setOnClickListener(v -> previewThumbnail(thumbnailMedium));
        thumbnailLow.setOnClickListener(v -> previewThumbnail(thumbnailLow));

        Drawable notFoundDrawable = getResources().getDrawable(R.drawable.ic_baseline_clear_24red);

        String videoId = getIntent().getStringExtra("videoId");
        YoutubeVideoByIdTask videoByIdTask = new YoutubeVideoByIdTask(videoId, video -> {
            ThumbnailDetails thumbnails = video.getSnippet().getThumbnails();

            if(thumbnails.getMaxres() != null)
                new DownloadImageTask(thumbnailMaxRes).execute(thumbnails.getMaxres().getUrl());
            else
                thumbnailMaxRes.setImageDrawable(notFoundDrawable);

            if(thumbnails.getStandard() != null)
                new DownloadImageTask(thumbnailStandard).execute(thumbnails.getStandard().getUrl());
            else
                thumbnailStandard.setImageDrawable(notFoundDrawable);

            if(thumbnails.getHigh() != null)
                new DownloadImageTask(thumbnailHigh).execute(thumbnails.getHigh().getUrl());
            else
                thumbnailHigh.setImageDrawable(notFoundDrawable);

            if(thumbnails.getMedium() != null)
                new DownloadImageTask(thumbnailMedium).execute(thumbnails.getMedium().getUrl());
            else
                thumbnailMedium.setImageDrawable(notFoundDrawable);

            if(thumbnails.getDefault() != null)
                new DownloadImageTask(thumbnailLow).execute(thumbnails.getDefault().getUrl());
            else
                thumbnailLow.setImageDrawable(notFoundDrawable);
        });
        videoByIdTask.execute();
    }

    private void previewThumbnail(ImageView thumbnailImage) {
        Object tag = thumbnailImage.getTag();
        if(tag != null) {
            Intent intent = new Intent(this, ThumbnailViewActivity.class);
            intent.putExtra("thumbnailUrl", tag.toString());

            startActivity(intent);
        } else
            Toast.makeText(this, "Nenhuma imagem de thumbnail foi carregada!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}