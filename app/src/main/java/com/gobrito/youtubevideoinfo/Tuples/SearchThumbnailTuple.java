package com.gobrito.youtubevideoinfo.Tuples;

import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.VideoContentDetails;

public class SearchThumbnailTuple {
    private SearchResult searchResult;
    private ThumbnailDetails thumbnailDetails;

    public SearchThumbnailTuple() {

    }

    public SearchThumbnailTuple(SearchResult searchResult, ThumbnailDetails thumbnailDetails) {
        this.searchResult = searchResult;
        this.thumbnailDetails = thumbnailDetails;
    }

    public SearchResult getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(SearchResult searchResult) {
        this.searchResult = searchResult;
    }

    public ThumbnailDetails getThumbnailDetails() {
        return thumbnailDetails;
    }

    public void setThumbnailDetails(ThumbnailDetails thumbnailDetails) {
        this.thumbnailDetails = thumbnailDetails;
    }
}
