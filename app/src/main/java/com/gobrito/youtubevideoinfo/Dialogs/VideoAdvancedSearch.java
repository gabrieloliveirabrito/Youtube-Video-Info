package com.gobrito.youtubevideoinfo.Dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.gobrito.youtubevideoinfo.AppController;
import com.gobrito.youtubevideoinfo.R;
import com.google.api.services.youtube.YouTube;

import java.util.function.Consumer;


public class VideoAdvancedSearch extends DialogFragment {
    private Consumer<YouTube.Search.List> onDialogClosed;

    public VideoAdvancedSearch(@NonNull Consumer<YouTube.Search.List> onDialogClose) {
        super();

        this.onDialogClosed = onDialogClose;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_advanced_video_search, null);

        builder
                .setTitle("Preencha abaixo com os filtros que deseja")
                .setView(layout)
                .setPositiveButton("Pesquisar", (dialog, id) -> {
                    dialog.dismiss();
                    onDialogClosed.accept(prepareQuery());
                })
                .setNegativeButton("Cancelar", (dialog, id) -> {
                    dialog.cancel();
                    onDialogClosed.accept(null);
                });

        return builder.create();
    }

    private YouTube.Search.List prepareQuery() {
        YouTube service = AppController.getGoogleServices().getYoutubeService();
        YouTube.Search.List query = null;
        try {
            query = service.search().list("snippet");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return query;
        }
    }
}
