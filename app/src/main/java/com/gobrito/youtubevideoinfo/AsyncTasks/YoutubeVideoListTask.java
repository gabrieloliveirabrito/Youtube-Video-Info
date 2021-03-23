package com.gobrito.youtubevideoinfo.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.gobrito.youtubevideoinfo.AppController;
import com.gobrito.youtubevideoinfo.Runnables.OnVideoListLoadedListener;
import com.gobrito.youtubevideoinfo.Tuples.VideoThumbnailTuple;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoListResponse;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

import static com.gobrito.youtubevideoinfo.GoogleServices.REQUEST_AUTHORIZATION;

public class YoutubeVideoListTask extends AsyncTask<Void, Void, List<SearchResult>> {
    private String query;
    private OnVideoListLoadedListener listener;
    Activity activity;

    public YoutubeVideoListTask(Activity activity, String query, @NotNull OnVideoListLoadedListener listener) {
        this.query = query;
        this.listener = listener;
        this.activity = activity;
    }

    @Override
    protected List<SearchResult> doInBackground(Void... voids) {
        try {
            YouTube service = AppController.getGoogleServices().getYoutubeService();

            YouTube.Search.List query = service.search().list("snippet");
            query.setQ(this.query);
            query.setMaxResults((long) 10);
            SearchListResponse response = query.execute();

            return response.getItems();
        } catch (UserRecoverableAuthIOException e) {
            activity.startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            return doInBackground(voids);
        } catch (Exception ex) {
            ex.printStackTrace();
            cancel(true);

            return null;
        }
    }

    @Override
    protected void onPostExecute(List<SearchResult> list) {
        if (list != null)
            listener.accept(list);
    }
}
