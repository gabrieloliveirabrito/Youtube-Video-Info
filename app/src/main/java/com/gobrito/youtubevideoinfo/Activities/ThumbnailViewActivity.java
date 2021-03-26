package com.gobrito.youtubevideoinfo.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.gobrito.youtubevideoinfo.AsyncTasks.DownloadBitmapTask;
import com.gobrito.youtubevideoinfo.R;

public class ThumbnailViewActivity extends AppCompatActivity {
    private SubsamplingScaleImageView thumbnailImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail_view);

        thumbnailImage = findViewById(R.id.thumbnail_view_image);
        String thumbnailUrl = getIntent().getStringExtra("thumbnailUrl");

        new DownloadBitmapTask(bitmap -> thumbnailImage.setImage(ImageSource.bitmap(bitmap)))
                .execute(thumbnailUrl);
    }
}