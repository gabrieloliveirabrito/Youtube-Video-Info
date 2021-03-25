package com.gobrito.youtubevideoinfo.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gobrito.youtubevideoinfo.AppController;
import com.gobrito.youtubevideoinfo.GoogleServices;
import com.gobrito.youtubevideoinfo.R;

public class SplashActivity extends AppCompatActivity {
    GoogleServices googleServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        googleServices = AppController.getGoogleServices();
        new Handler().postDelayed(() -> initialize(), 2000);
    }

    void initialize() {
        switch (googleServices.init(this)) {
            case OK:
                googleServices.initializeYoutubeService();
                startActivity(new Intent(this, VideoListActivity.class));
                break;
            case FAILED:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case NO_NETWORK:
                new AlertDialog.Builder(this)
                        .setTitle("AVISO")
                        .setMessage("Falha ao se conectar com os serviços da Google. Verifique se você tem rede no dispositivo!")
                        .setPositiveButton("Tentar novamente", (dialog, which) -> initialize())
                        .setNegativeButton("Sair", (dialog, which) -> finish())
                        .show();
                break;
        }
    }
}