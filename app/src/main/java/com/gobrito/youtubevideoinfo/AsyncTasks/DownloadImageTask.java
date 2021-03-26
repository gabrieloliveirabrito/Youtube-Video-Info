package com.gobrito.youtubevideoinfo.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.google.api.services.youtube.model.ThumbnailDetails;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedList;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView target;

    public DownloadImageTask(ImageView target) {
        this.target = target;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        for(String url : urls) {
            try {
                target.setTag(url);

                InputStream downloadStream = new URL(url).openStream();
                return BitmapFactory.decodeStream(downloadStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(bitmap != null)
        target.setImageBitmap(bitmap);
    }

    public void execute(ThumbnailDetails thumbnailModel) {
        LinkedList<String> urls = new LinkedList<>();
        if(thumbnailModel.getMaxres() != null)
            urls.add(thumbnailModel.getMaxres().getUrl());
        if(thumbnailModel.getHigh() != null)
            urls.add(thumbnailModel.getHigh().getUrl());
        if(thumbnailModel.getMedium() != null)
            urls.add(thumbnailModel.getMedium().getUrl());
        if(thumbnailModel.getStandard() != null)
            urls.add(thumbnailModel.getStandard().getUrl());

        execute(urls.toArray(new String[0]));
    }
}
