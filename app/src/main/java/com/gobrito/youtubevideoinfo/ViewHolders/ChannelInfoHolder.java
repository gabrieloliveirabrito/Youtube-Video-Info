package com.gobrito.youtubevideoinfo.ViewHolders;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gobrito.youtubevideoinfo.AppController;
import com.gobrito.youtubevideoinfo.AsyncTasks.DownloadImageTask;
import com.gobrito.youtubevideoinfo.R;
import com.gobrito.youtubevideoinfo.Tuples.SearchVideoChannelTuple;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelStatistics;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Video;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.unbescape.html.HtmlEscape;

import java.text.DateFormat;
import java.util.Date;

public class ChannelInfoHolder extends RecyclerView.ViewHolder {
    MaterialCardView itemView;
    CircularImageView photoView;
    MaterialTextView titleTextView, infoTextView, subscribersTextView;
    SearchResult result;

    public ChannelInfoHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = (MaterialCardView)itemView;

        this.itemView.setOnClickListener(v -> {
            if(result != null) {
                String kind = result.getId().getKind();
                String channelId = result.getId().getChannelId();

                if(kind.equals("youtube#video")) {
                    Intent channelIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://user/" + channelId));
                    channelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    Context context = AppController.getContext();
                    try {
                        context.startActivity(channelIntent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Youtube não instalado no sistema!! Tentando iniciar versão web", Toast.LENGTH_SHORT).show();

                        channelIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/channel/" + channelId));
                        context.startActivity(channelIntent);
                    }
                }
            }
        });

        photoView = itemView.findViewById(R.id.item_channel_photo);
        titleTextView = itemView.findViewById(R.id.item_channel_title);
        infoTextView = itemView.findViewById(R.id.item_channel_info);
        subscribersTextView = itemView.findViewById(R.id.item_channel_subscribers);
    }

    public void bind(SearchVideoChannelTuple tuple) {
        SearchResult result = tuple.getSearchResult();
        Channel channel = tuple.getChannel();
        ChannelStatistics statistics = channel.getStatistics();

        this.result = result;
        SearchResultSnippet snippet = result.getSnippet();

        String infoText = String.format("%d inscritos - %d vídeos", statistics.getSubscriberCount(), statistics.getVideoCount());

        titleTextView.setText(HtmlEscape.unescapeHtml(snippet.getTitle()));
        infoTextView.setText(infoText);

        new DownloadImageTask(photoView).execute(snippet.getThumbnails());
    }
}
