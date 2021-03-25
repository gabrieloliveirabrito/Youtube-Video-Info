package com.gobrito.youtubevideoinfo;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.gobrito.youtubevideoinfo.Enums.GServicesInitResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeRequestInitializer;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.common.io.BaseEncoding;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class GoogleServices {
    public static final int REQUEST_ACCOUNT_PICKER = 1000;
    public static final int REQUEST_AUTHORIZATION = 1001;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    public static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private Context context;
    private Activity mainActivity;
    private GoogleAccountCredential mCredential;
    private String[] SCOPES = {YouTubeScopes.YOUTUBE_READONLY};
    private YouTube youtubeService;

    public GoogleServices(Context context) {
        this.context = context;

        mCredential = GoogleAccountCredential
                .usingOAuth2(context, Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    public GServicesInitResult init(Activity mainActivity) {
        return init(mainActivity, true);
    }

    public GServicesInitResult init(Activity mainActivity, boolean chooseAccount) {
        this.mainActivity = mainActivity;

        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
            return GServicesInitResult.FAILED;
        } else if (!isDeviceOnline()) {
            return GServicesInitResult.NO_NETWORK;
        } else {
            return GServicesInitResult.OK;
        }
    }

    private String getSHA1(String packageName) {
        try {
            Signature[] signatures = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES).signatures;
            for (Signature signature : signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA-1");
                md.update(signature.toByteArray());
                return BaseEncoding.base16().encode(md.digest());
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initializeYoutubeService() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        youtubeService = new YouTube.Builder(transport, jsonFactory, null)
                .setApplicationName("Youtube Video Info")
                .setHttpRequestInitializer(request -> {
                    String packageName = context.getPackageName();
                    String SHA1 = getSHA1(packageName);

                    request.getHeaders().set("X-Android-Package", packageName);
                    request.getHeaders().set("X-Android-Cert", SHA1);
                })
                .setYouTubeRequestInitializer(new YouTubeRequestInitializer(Constants.GoogleApiKey))
                .build();
    }

    public YouTube getYoutubeService() {
        return youtubeService;
    }

    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    public GServicesInitResult chooseAccount() {
        if (EasyPermissions.hasPermissions(mainActivity, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = AppController.getPreferences().getAccountName();
            if (accountName != null) {
                mCredential = mCredential.setSelectedAccountName(accountName);
                return GServicesInitResult.OK;
            } else {
                mainActivity.startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
                return GServicesInitResult.WAITING_FOR_ACCOUNT;
            }
        } else {
            EasyPermissions.requestPermissions(
                    mainActivity,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
            return GServicesInitResult.FAILED;
        }
    }

    public void setAccountName(String accountName) {
        mCredential.setSelectedAccountName(accountName);
    }

    public boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    public boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    public void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(context);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }

    public void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                mainActivity,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }
}
