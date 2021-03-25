package com.gobrito.youtubevideoinfo.Tuples;

import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Video;

public class SearchVideoChannelTuple {
    private SearchResult searchResult;
    private Video video;
    private Channel channel;

    public SearchVideoChannelTuple() {

    }

    public SearchVideoChannelTuple(SearchResult searchResult, Video video) {
        this.searchResult = searchResult;
        this.video = video;
    }

    public SearchVideoChannelTuple(SearchResult searchResult, Video video, Channel channel) {
        this.searchResult = searchResult;
        this.video = video;
        this.channel = channel;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
