package com.gobrito.youtubevideoinfo.AsyncTasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import androidx.core.util.Consumer;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {
    Consumer<Bitmap> listener;
    public DownloadBitmapTask(Consumer<Bitmap> listener) {
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(String... urls) {
        for(String url : urls) {
            InputStream downloadStream = null;
            try {
                downloadStream = new URL(url).openStream();
                return BitmapFactory.decodeStream(downloadStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        if(bitmap != null)
            listener.accept(bitmap);
    }
}
