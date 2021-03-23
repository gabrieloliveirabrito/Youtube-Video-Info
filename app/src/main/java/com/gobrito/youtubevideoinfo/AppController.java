package com.gobrito.youtubevideoinfo;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gobrito.youtubevideoinfo.Enums.ToastLength;
import com.google.api.services.youtube.YouTube;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.util.Date;

public class AppController extends Application {
    private static Context context;
    private static Gson gson;
    private static RequestQueue queue;
    private static GoogleServices googleServices;
    private static Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        gson = new GsonBuilder().registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, ctx) -> new Date(json.getAsJsonPrimitive().getAsLong()))
                .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
                .create();
        queue = Volley.newRequestQueue(context);
        googleServices = new GoogleServices(context);
        preferences = new Preferences(context);
    }

    public static Gson getGson() {
        return gson;
    }

    public static Preferences getPreferences() { return preferences; }

    public static GoogleServices getGoogleServices() { return googleServices; }

    public static void showToast(String message, ToastLength length) {
        Toast.makeText(context, message , length == ToastLength.LONG ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT).show();
    }
}
