package com.gobrito.youtubevideoinfo.Activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gobrito.youtubevideoinfo.AppController;
import com.gobrito.youtubevideoinfo.AsyncTasks.YoutubeSearchListTask;
import com.gobrito.youtubevideoinfo.Dialogs.VideoAdvancedSearch;
import com.gobrito.youtubevideoinfo.GoogleServices;
import com.gobrito.youtubevideoinfo.ListAdapters.VideoListAdapter;
import com.gobrito.youtubevideoinfo.R;
import com.gobrito.youtubevideoinfo.Tuples.SearchVideoChannelTuple;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.api.services.youtube.YouTube;

import java.util.LinkedList;
import java.util.List;

public class VideoListActivity extends AppCompatActivity {
    List<SearchVideoChannelTuple> videoList;
    VideoListAdapter videoListAdapter;

    AppBarLayout appBarLayout;
    Toolbar appBar;
    RecyclerView videoListRecycler;
    MaterialTextView videoListResponse;
    ProgressBar videoListProgress;
    String lastQuery = "";

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
        videoListRecycler.setItemViewCacheSize(5);
        videoListRecycler.setDrawingCacheEnabled(true);
        videoListRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        videoListProgress = findViewById(R.id.video_list_progressbar);
        videoListResponse = findViewById(R.id.video_list_response);
    }

    @Override
    public void onBackPressed() {
        GoogleServices services = AppController.getGoogleServices();
        if(services == null)
            super.onBackPressed();
        else
            moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_list_menu, menu);

        MenuItem item = menu.findItem(R.id.video_list_search);
        SearchView view = (SearchView) item.getActionView();
        view.setQueryHint("Pesquisar");

        view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //appBar.collapseActionView();

                lastQuery = query;
                if (query.isEmpty()) {
                    view.setIconified(true);
                    appBar.collapseActionView();
                } else
                    view.setQuery(query, false);

                videoList.clear();
                videoListAdapter.notifyDataSetChanged();
                videoListProgress.setVisibility(View.VISIBLE);
                videoListResponse.setVisibility(View.GONE);

                YoutubeSearchListTask task = new YoutubeSearchListTask(VideoListActivity.this, query, list -> {
                    if (list.isEmpty()) {
                        videoListResponse.setText("Sua pesquisa não retornou nenhum resultado!");
                        videoListResponse.setVisibility(View.VISIBLE);
                    } else {
                        videoList.addAll(list);
                    }

                    videoListProgress.setVisibility(View.GONE);
                    videoListAdapter.notifyDataSetChanged();
                });
                task.execute();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            /*case R.id.video_list_item_advanced_search:
                VideoAdvancedSearch advancedSearch = new VideoAdvancedSearch(query -> {
                    if (query != null) {
                        videoList.clear();
                        videoListAdapter.notifyDataSetChanged();
                        videoListProgress.setVisibility(View.VISIBLE);
                        videoListResponse.setVisibility(View.GONE);

                        YoutubeSearchListTask task = new YoutubeSearchListTask(this, lastQuery, query, list -> {
                            if (list.isEmpty()) {
                                videoListResponse.setText("Sua pesquisa não retornou nenhum resultado!");
                                videoListResponse.setVisibility(View.VISIBLE);
                            } else {
                                videoList.addAll(list);
                            }

                            videoListProgress.setVisibility(View.GONE);
                            videoListAdapter.notifyDataSetChanged();
                        });
                        task.execute();
                    }
                });
                advancedSearch.show(getSupportFragmentManager(), "videoListActivity");
                return true;*/
            case R.id.video_list_item_most_rated:
                YouTube service = AppController.getGoogleServices().getYoutubeService();
                try {
                    YouTube.Search.List query = service.search().list("snippet");
                    query.setOrder("viewCount");

                    videoList.clear();
                    videoListAdapter.notifyDataSetChanged();
                    videoListProgress.setVisibility(View.VISIBLE);
                    videoListResponse.setVisibility(View.GONE);

                    YoutubeSearchListTask task = new YoutubeSearchListTask(this, lastQuery, query, list -> {
                        if (list.isEmpty()) {
                            videoListResponse.setText("Sua pesquisa não retornou nenhum resultado!");
                            videoListResponse.setVisibility(View.VISIBLE);
                        } else {
                            videoList.addAll(list);
                        }

                        videoListProgress.setVisibility(View.GONE);
                        videoListAdapter.notifyDataSetChanged();
                    });
                    task.execute();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(this, "Falha ao carregar os vídeos em alta", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}