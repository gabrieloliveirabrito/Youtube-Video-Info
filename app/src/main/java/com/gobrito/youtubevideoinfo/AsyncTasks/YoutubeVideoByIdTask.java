package com.gobrito.youtubevideoinfo.AsyncTasks;

import android.os.AsyncTask;

import com.gobrito.youtubevideoinfo.AppController;
import com.gobrito.youtubevideoinfo.GoogleServices;
import com.gobrito.youtubevideoinfo.Runnables.OnVideoLoadedListener;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

public class YoutubeVideoByIdTask extends AsyncTask<Void, Void, Video> {
    private String videoId;
    private OnVideoLoadedListener listener;

    public YoutubeVideoByIdTask(String videoId, OnVideoLoadedListener listener) {
        this.videoId = videoId;
        this.listener = listener;
    }

    @Override
    protected Video doInBackground(Void... voids) {
        Video video = null;
        try {
            GoogleServices services = AppController.getGoogleServices();
            YouTube service = services.getYoutubeService();

            YouTube.Videos.List query = service.videos().list("id,snippet,contentDetails,player,recordingDetails,statistics,status,topicDetails");
            query.setId(videoId);
            VideoListResponse response = query.execute();

            video = response.getItems().get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return video;
        }
    }

    @Override
    protected void onPostExecute(Video video) {
       listener.accept(video);
    }
}
