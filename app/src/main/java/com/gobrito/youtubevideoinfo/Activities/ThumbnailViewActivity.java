package com.gobrito.youtubevideoinfo.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.gobrito.youtubevideoinfo.AsyncTasks.DownloadBitmapTask;
import com.gobrito.youtubevideoinfo.R;

public class ThumbnailViewActivity extends AppCompatActivity {
    private SubsamplingScaleImageView thumbnailImage;
    private Toolbar appBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnail_view);

        appBar = findViewById(R.id.video_thumbnail_view_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        thumbnailImage = findViewById(R.id.thumbnail_view_image);
        String thumbnailUrl = getIntent().getStringExtra("thumbnailUrl");

        new DownloadBitmapTask(bitmap -> thumbnailImage.setImage(ImageSource.bitmap(bitmap)))
                .execute(thumbnailUrl);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.thumbnail_view_save_item: {
                String thumbnailUrl = getIntent().getStringExtra("thumbnailUrl");
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(thumbnailUrl)));
            }
            return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.thumbnail_view_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
}