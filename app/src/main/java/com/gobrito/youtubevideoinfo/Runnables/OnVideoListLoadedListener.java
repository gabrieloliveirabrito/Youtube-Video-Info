package com.gobrito.youtubevideoinfo.Runnables;

import androidx.core.util.Consumer;

import com.gobrito.youtubevideoinfo.Tuples.SearchVideoChannelTuple;

import java.util.List;

public interface OnVideoListLoadedListener extends Consumer<List<SearchVideoChannelTuple>> {
}
