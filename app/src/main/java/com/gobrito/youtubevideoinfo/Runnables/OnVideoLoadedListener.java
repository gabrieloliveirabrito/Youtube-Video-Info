package com.gobrito.youtubevideoinfo.Runnables;

import androidx.core.util.Consumer;

import com.google.api.services.youtube.model.Video;


public interface OnVideoLoadedListener extends Consumer<Video> {
}
