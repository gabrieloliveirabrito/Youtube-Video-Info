package com.gobrito.youtubevideoinfo.Activities;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.gobrito.youtubevideoinfo.Activities.VideoListActivity;
import com.gobrito.youtubevideoinfo.AppController;
import com.gobrito.youtubevideoinfo.GoogleServices;
import com.gobrito.youtubevideoinfo.R;
import com.google.android.gms.common.SignInButton;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class LoginActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    TextView lblMainDescription;
    SignInButton btnSignin;
    GoogleServices googleServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleServices = AppController.getGoogleServices();

        lblMainDescription = findViewById(R.id.lblMainDescription);
        btnSignin = findViewById(R.id.main_signin_button);
        btnSignin.setOnClickListener(v -> {
            switch (googleServices.init(this)) {
                case OK:
                    Toast.makeText(this, "Google Services has been initialized!", Toast.LENGTH_LONG).show();

                    goToVideoList();
                    break;
                case FAILED:
                    Toast.makeText(this, "Google Services has failed to initialize!", Toast.LENGTH_LONG).show();
                    break;
                case NO_NETWORK:
                    Toast.makeText(this, "No network to initialize Google Services!", Toast.LENGTH_LONG).show();
                    break;
            }
        });
    }

    void goToVideoList() {
        googleServices.initializeYoutubeService();
        startActivity(new Intent(this, VideoListActivity.class));
    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GoogleServices.REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    new AlertDialog.Builder(this)
                            .setTitle(getString(R.string.common_google_play_services_install_title))
                            .setMessage(getString(R.string.common_google_play_services_install_text))
                            .show();

                    finish();
                } else {
                    goToVideoList();
                }
                break;
            case GoogleServices.REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        AppController.getPreferences().setGoogleAccountName(accountName);
                        AppController.getGoogleServices().setAccountName(accountName);

                        lblMainDescription.setText(accountName);
                       goToVideoList();
                    }
                }
                break;
            case GoogleServices.REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    goToVideoList();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }
}