package com.gobrito.youtubevideoinfo.Runnables;

import androidx.core.util.Consumer;

import com.gobrito.youtubevideoinfo.Tuples.VideoThumbnailTuple;
import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

public interface OnVideoListLoadedListener extends Consumer<List<SearchResult>> {
}
