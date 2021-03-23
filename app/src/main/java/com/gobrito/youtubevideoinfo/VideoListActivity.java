package com.gobrito.youtubevideoinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.gobrito.youtubevideoinfo.AsyncTasks.YoutubeVideoListTask;
import com.gobrito.youtubevideoinfo.ListAdapters.VideoListAdapter;
import com.gobrito.youtubevideoinfo.Tuples.SearchThumbnailTuple;
import com.gobrito.youtubevideoinfo.Tuples.VideoThumbnailTuple;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.api.services.youtube.model.SearchResult;

import java.util.LinkedList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {
    List<SearchResult> videoList;
    VideoListAdapter videoListAdapter;

    AppBarLayout appBarLayout;
    Toolbar appBar;
    RecyclerView videoListRecycler;
    TextInputEditText videoListSearchField;
    MaterialButton videoListSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);
        videoList = new LinkedList<>();

        appBarLayout = findViewById(R.id.video_list_appbar_layout);
        appBar = findViewById(R.id.video_list_appbar);
        setSupportActionBar(appBar);

        videoListRecycler = findViewById(R.id.video_list_recycler);
        videoListAdapter = new VideoListAdapter(this, videoList);
        videoListRecycler.setAdapter(videoListAdapter);
        videoListRecycler.setLayoutManager(new LinearLayoutManager(this));
        videoListRecycler.setHasFixedSize(true);
        videoListRecycler.setItemViewCacheSize(20);
        videoListRecycler.setDrawingCacheEnabled(true);
        videoListRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        videoListSearchField = findViewById(R.id.video_list_search_edittext);
        videoListSearchButton = findViewById(R.id.video_list_search_button);
        videoListSearchButton.setOnClickListener(v -> {
            String query = videoListSearchField.getText().toString();

            if(!query.isEmpty()) {
                videoList.clear();
                videoListAdapter.notifyDataSetChanged();

                YoutubeVideoListTask task = new YoutubeVideoListTask(this, query, list -> {
                    videoList.clear();
                    videoList.addAll(list);

                    videoListAdapter.notifyDataSetChanged();
                });
                task.execute();
            }
        });
    }
}