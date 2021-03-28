package com.gobrito.youtubevideoinfo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.gobrito.youtubevideoinfo.AsyncTasks.YoutubeVideoByIdTask;
import com.gobrito.youtubevideoinfo.R;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoContentDetails;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;

import org.joda.time.Period;
import org.joda.time.format.ISOPeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.DateFormat;
import java.util.Date;

public class VideoInfoActivity extends AppCompatActivity {
    TextView videoInfoId, videoInfoEtag, videoInfoSnippetPublishedAt;
    TextView videoInfoSnippetChannel, videoInfoSnippetTitle, videoInfoSnippetDescription;
    TextView videoInfoSnippetTags, videoInfoContentDetailsDuration, videoInfoContentDetailsDimension;
    TextView videoInfoContentDetailsDefinition, videoInfoContentDetailsCaption;
    TextView videoInfoContentDetailsLicensedContent, videoInfoContentDetailsProjection;
    TextView videoInfoStatiticsViewCount, videoInfoStatiticsLikeCount, videoInfoStatiticsDislikeCount;
    TextView videoInfoStatiticsFavoriteCount, videoInfoStatiticsCommentCount;
    Video video;
    PeriodFormatter formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_info);

        Toolbar appBar = findViewById(R.id.video_info_appbar);
        setSupportActionBar(appBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        formatter = new PeriodFormatterBuilder()
                .appendDays().appendSuffix(" dia", " dias").appendSeparator(", ")
                .appendHours().appendSuffix(" hora", " horas").appendSeparator(", ")
                .appendMinutes().appendSuffix(" minuto", " minutos").appendSeparator(", ")
                .appendSeconds().appendSuffix(" segundo", " segundos").appendSeparator(", ")
                .appendMillis().appendSuffix(" ms")
                .toFormatter();

        videoInfoId = findViewById(R.id.video_info_id);
        videoInfoEtag = findViewById(R.id.video_info_etag);
        videoInfoSnippetPublishedAt = findViewById(R.id.video_info_snippet_published_at);
        videoInfoSnippetChannel = findViewById(R.id.video_info_snippet_channel);
        videoInfoSnippetTitle = findViewById(R.id.video_info_snippet_title);
        videoInfoSnippetDescription = findViewById(R.id.video_info_snippet_description);
        videoInfoSnippetTags = findViewById(R.id.video_info_snippet_tags);
        videoInfoContentDetailsDuration = findViewById(R.id.video_info_contentDetails_duration);
        videoInfoContentDetailsDimension = findViewById(R.id.video_info_contentDetails_dimension);
        videoInfoContentDetailsDefinition = findViewById(R.id.video_info_contentDetails_definition);
        videoInfoContentDetailsCaption = findViewById(R.id.video_info_contentDetails_caption);
        videoInfoContentDetailsLicensedContent = findViewById(R.id.video_info_contentDetails_licensedContent);
        videoInfoContentDetailsProjection = findViewById(R.id.video_info_contentDetails_projection);
        videoInfoStatiticsViewCount = findViewById(R.id.video_info_statistics_viewCount);
        videoInfoStatiticsLikeCount = findViewById(R.id.video_info_statistics_likeCount);
        videoInfoStatiticsDislikeCount = findViewById(R.id.video_info_statistics_dislikeCount);
        videoInfoStatiticsFavoriteCount = findViewById(R.id.video_info_statistics_favoriteCount);
        videoInfoStatiticsCommentCount = findViewById(R.id.video_info_statistics_commentCount);

        videoInfoSnippetDescription.setOnClickListener(v -> {
            if(video != null) {
                String description = video.getSnippet().getDescription();
                String currentText = videoInfoSnippetDescription.getText().toString().substring("Descrição: ".length());

                if(currentText.length() == 90)
                    videoInfoSnippetDescription.setText("Descrição: "+ description);
                else
                    videoInfoSnippetDescription.setText("Descrição: "+ description.substring(0, 30) + "...");
            }
        });

        Intent intent = getIntent();
        String videoId = intent.getStringExtra("videoId");

        YoutubeVideoByIdTask task = new YoutubeVideoByIdTask(videoId, video -> {
            VideoInfoActivity.this.video = video;

            VideoSnippet snippet = video.getSnippet();
            Date publishDate = new Date(snippet.getPublishedAt().getValue());

            videoInfoId.setText("ID: " + video.getId());
            videoInfoEtag.setText("ETag: " + video.getEtag());
            videoInfoSnippetPublishedAt.setText("Publicado em: " + DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(publishDate));

            videoInfoSnippetChannel.setText(String.format("Canal: %s - %s", snippet.getChannelId(), snippet.getChannelTitle()));
            videoInfoSnippetTitle.setText("Título: " + snippet.getTitle());
            videoInfoSnippetDescription.setText("Descrição: " + snippet.getDescription().substring(0, 30) + "...");
            videoInfoSnippetTags.setText("Tags: " + TextUtils.join(",", snippet.getTags()));

            VideoContentDetails contentDetails = video.getContentDetails();
            Period period = ISOPeriodFormat.standard().parsePeriod(contentDetails.getDuration());

            videoInfoContentDetailsDuration.setText("Duração: " + formatter.print(period));
            videoInfoContentDetailsDimension.setText("Dimensão: " + contentDetails.getDimension());
            videoInfoContentDetailsDefinition.setText("Definição: " + contentDetails.getDefinition().toUpperCase());
            videoInfoContentDetailsCaption.setText("Legendas: " + (contentDetails.getCaption().equals("true") ? "Sim" : "Não"));
            videoInfoContentDetailsLicensedContent.setText("Conteúdo licenciado: " + (contentDetails.getLicensedContent() ? "Sim" : "Não"));
            videoInfoContentDetailsProjection.setText("Projeção: " + contentDetails.getProjection());

            VideoStatistics statistics = video.getStatistics();
            videoInfoStatiticsViewCount.setText("Visualizações:" + statistics.getViewCount());
            videoInfoStatiticsLikeCount.setText("Total de likes: " + statistics.getLikeCount());
            videoInfoStatiticsDislikeCount.setText("Total de dislikes: " + statistics.getDislikeCount());
            videoInfoStatiticsFavoriteCount.setText("Usuários que favoritaram: " + statistics.getFavoriteCount());
            videoInfoStatiticsCommentCount.setText("Total de comentários: " + statistics.getCommentCount());
        });
        task.execute();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}