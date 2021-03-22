package com.gobrito.youtubevideoinfo;

import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.util.Date;

public class AppController extends Application {
    private static Gson gson;
    private static RequestQueue queue;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        gson = new GsonBuilder().registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, ctx) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
                .create();
        queue = Volley.newRequestQueue(context);
    }

    public static Gson getGson() {
        return gson;
    }
}
