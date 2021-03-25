package com.gobrito.youtubevideoinfo.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.gobrito.youtubevideoinfo.AppController;
import com.gobrito.youtubevideoinfo.Runnables.OnVideoListLoadedListener;
import com.gobrito.youtubevideoinfo.Tuples.SearchVideoChannelTuple;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static com.gobrito.youtubevideoinfo.GoogleServices.REQUEST_AUTHORIZATION;

public class YoutubeSearchListTask extends AsyncTask<Void, Void, List<SearchVideoChannelTuple>> {
    private String globalFilter;
    private OnVideoListLoadedListener listener;
    private YouTube.Search.List query;
    private Activity activity;

    public YoutubeSearchListTask(Activity activity, String globalFilter, @NotNull OnVideoListLoadedListener listener) {
        this.globalFilter = globalFilter;
        this.listener = listener;
        this.activity = activity;
        this.query = null;
    }

    public YoutubeSearchListTask(Activity activity, String globalFilter, YouTube.Search.List query, @NotNull OnVideoListLoadedListener listener) {
        this(activity, globalFilter, listener);
        this.query = query;
    }

    @Override
    protected List<SearchVideoChannelTuple> doInBackground(Void... voids) {
        try {
            YouTube service = AppController.getGoogleServices().getYoutubeService();
            YouTube.Search.List query = this.query;
            if(query == null)
                query = service.search().list("snippet");

            query.setQ(this.globalFilter);
            query.setMaxResults((long) 10);
            query.setType("video,channel");

            SearchListResponse response = query.execute();
            List<SearchResult> searchResults = response.getItems();
            List<SearchVideoChannelTuple> searchTuples = new LinkedList<>();

            for(SearchResult searchResult : searchResults) {
                SearchVideoChannelTuple tuple = new SearchVideoChannelTuple();
                tuple.setSearchResult(searchResult);

                String kind = searchResult.getId().getKind();
                if(kind.equals("youtube#video")) {
                    Video video = retrieveVideo(service, searchResult.getId().getVideoId());
                    tuple.setVideo(video);
                } else if(kind.equals("youtube#channel")) {
                    Channel channel = retrieveChannel(service, searchResult.getId().getChannelId());
                    tuple.setChannel(channel);
                }

                searchTuples.add(tuple);
            }

            return searchTuples;
        } catch (UserRecoverableAuthIOException e) {
            activity.startActivityForResult(e.getIntent(), REQUEST_AUTHORIZATION);
            return doInBackground(voids);
        } catch (Exception ex) {
            ex.printStackTrace();
            cancel(true);

            return null;
        }
    }

    Video retrieveVideo(YouTube service, String id) throws IOException {
        YouTube.Videos.List query = service.videos().list("id,snippet,contentDetails,player,recordingDetails,statistics,status,topicDetails");
        query.setId(id);

        VideoListResponse response = query.execute();
        return response.getItems().get(0);
    }

    Channel retrieveChannel(YouTube service, String id) throws  IOException {
        YouTube.Channels.List query = service.channels().list("id,snippet,brandingSettings,contentDetails,statistics,topicDetails");
        query.setId(id);

        ChannelListResponse response = query.execute();
        return response.getItems().get(0);
    }

    @Override
    protected void onPostExecute(List<SearchVideoChannelTuple> list) {
        if (list != null)
            listener.accept(list);
    }
}
