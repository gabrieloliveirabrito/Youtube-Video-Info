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
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import com.google.api.services.youtube.model.Video;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.unbescape.html.HtmlEscape;

public class VideoInfoHolder extends RecyclerView.ViewHolder {
    MaterialCardView itemView;
    ImageView thumbnailView;
    MaterialTextView titleTextView, infoTextView;
    SearchResult result;

    public VideoInfoHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = (MaterialCardView) itemView;

        this.itemView.setOnClickListener(v -> {
            if (result != null) {
                Context context = AppController.getContext();

                String kind = result.getId().getKind();
                if (kind.equals("youtube#video")) {
                    Intent videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + result.getId().getVideoId()));
                    videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    try {
                        context.startActivity(videoIntent);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Youtube não instalado no sistema!! Tentando iniciar versão web", Toast.LENGTH_SHORT).show();

                        videoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + result.getId()));
                        context.startActivity(videoIntent);
                    }
                } else
                    Toast.makeText(context, "Falha, o tipo de conteúdo não é um vídeo!!", Toast.LENGTH_SHORT).show();
            }
        });

        thumbnailView = itemView.findViewById(R.id.item_video_thumbnail);
        titleTextView = itemView.findViewById(R.id.item_video_title);
        infoTextView = itemView.findViewById(R.id.item_video_info);
    }

    public void bind(SearchVideoChannelTuple tuple) {
        SearchResult result = tuple.getSearchResult();
        Video video = tuple.getVideo();

        this.result = result;
        SearchResultSnippet snippet = result.getSnippet();

        DateTime today = new DateTime();
        DateTime publishDate = new DateTime(snippet.getPublishedAt().getValue());

        Period period = new Period(publishDate, today);
        String elapsedTime = "";

        if (period.getYears() > 0)
            elapsedTime = String.format("%d anos", period.getYears());
        else if (period.getMonths() > 0)
            elapsedTime = String.format("%d meses", period.getMonths());
        else if (period.getDays() > 0)
            elapsedTime = String.format("%d dias", period.getDays());
        else if (period.getHours() > 0)
            elapsedTime = String.format("%d horas", period.getHours());
        else if (period.getMinutes() > 0)
            elapsedTime = String.format("%d minutos", period.getMinutes());
        else if (period.getSeconds() > 0)
            elapsedTime = String.format("%d segundos", period.getSeconds());
        else
            elapsedTime = String.format("%d millisegundos", period.getMillis());

        String infoText = String.format("%s - %d Likes - há %s",
                snippet.getChannelTitle(), video.getStatistics().getLikeCount(), elapsedTime);

        titleTextView.setText(HtmlEscape.unescapeHtml(snippet.getTitle()));
        infoTextView.setText(infoText);

        new DownloadImageTask(thumbnailView).execute(snippet.getThumbnails());
    }
}
