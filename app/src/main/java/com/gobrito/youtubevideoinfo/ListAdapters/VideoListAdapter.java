package com.gobrito.youtubevideoinfo.ListAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gobrito.youtubevideoinfo.R;
import com.gobrito.youtubevideoinfo.Tuples.SearchVideoChannelTuple;
import com.gobrito.youtubevideoinfo.ViewHolders.ChannelInfoHolder;
import com.gobrito.youtubevideoinfo.ViewHolders.VideoInfoHolder;

import java.util.List;

public class VideoListAdapter extends RecyclerView.Adapter {
    private final int ITEM_TYPE_VIDEO = 1;
    private final int ITEM_TYPE_CHANNEL = 2;

    private final Context context;
    private final List<SearchVideoChannelTuple> videoList;

    public VideoListAdapter(Context context, List<SearchVideoChannelTuple> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @Override
    public int getItemViewType(int position) {
        SearchVideoChannelTuple tuple = videoList.get(position);

        if(tuple.getVideo() != null)
            return ITEM_TYPE_VIDEO;
        else if(tuple.getChannel() != null)
            return ITEM_TYPE_CHANNEL;
        else
            return super.getItemViewType(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType == ITEM_TYPE_VIDEO) {
            View view = inflater.inflate(R.layout.item_video_info, parent, false);
            return new VideoInfoHolder(view);
        } else if(viewType == ITEM_TYPE_CHANNEL) {
            View view = inflater.inflate(R.layout.item_channel_info, parent, false);
            return new ChannelInfoHolder(view);
        } else return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchVideoChannelTuple result = videoList.get(position);

        switch (holder.getItemViewType()) {
            case ITEM_TYPE_VIDEO:
                ((VideoInfoHolder)holder).bind(result);
                break;
            case ITEM_TYPE_CHANNEL:
                ((ChannelInfoHolder)holder).bind(result);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}
