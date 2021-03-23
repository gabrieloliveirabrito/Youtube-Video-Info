package com.gobrito.youtubevideoinfo.Tuples;

import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.VideoContentDetails;

public class VideoThumbnailTuple {
    private VideoContentDetails videoDetails;
    private ThumbnailDetails thumbnailDetails;

    public VideoThumbnailTuple() {

    }

    public VideoThumbnailTuple(VideoContentDetails videoDetails, ThumbnailDetails thumbnailDetails) {
        this.videoDetails = videoDetails;
        this.thumbnailDetails = thumbnailDetails;
    }

    public VideoContentDetails getVideoDetails() {
        return videoDetails;
    }

    public void setVideoDetails(VideoContentDetails videoDetails) {
        this.videoDetails = videoDetails;
    }

    public ThumbnailDetails getThumbnailDetails() {
        return thumbnailDetails;
    }

    public void setThumbnailDetails(ThumbnailDetails thumbnailDetails) {
        this.thumbnailDetails = thumbnailDetails;
    }
}
