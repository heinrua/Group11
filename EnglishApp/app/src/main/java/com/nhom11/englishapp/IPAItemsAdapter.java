package com.nhom11.englishapp;



import android.media.MediaPlayer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.List;

public class IPAItemsAdapter extends RecyclerView.Adapter<IPAItemsAdapter.ViewHolder> {
    private List<IPAItem> ipaItems;

    public IPAItemsAdapter(List<IPAItem> ipaItems){
        this.ipaItems = ipaItems;
    }


    @NonNull
    @Override
    public IPAItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ipa_expan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IPAItem ipaItem = ipaItems.get(position);
        holder.bin(ipaItem);

    }

    @Override
    public int getItemCount() {
        return ipaItems.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView nameIPA;
        private ImageButton soundIPA ;
        private WebView video;
        private View linearLayout;
        private boolean isPlayingAudio = false;

        public ViewHolder (@NonNull View view){
            super(view);
            nameIPA = view.findViewById(R.id.name_sound);
            soundIPA = view.findViewById(R.id.btn_listen_ipa);
            video = view.findViewById(R.id.video_ipa);
            linearLayout = view.findViewById(R.id.layout_expand);
            // Ẩn LinearLayout ngay từ ban đầu
            linearLayout.setVisibility(View.GONE);
        }
        public void bin(IPAItem ipaItem){
            nameIPA.setText("/" + ipaItem.getNameIPA()+ "/");
            nameIPA.setOnClickListener(v -> {
                if (linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }
            });
            soundIPA.setOnClickListener(v -> {
                if (!isPlayingAudio) {
                    playAudio(ipaItem.getSoundUrl());
                    soundIPA.setImageResource(R.drawable.pause);
                } else {
                    soundIPA.setImageResource(R.drawable.speak);
                }
                isPlayingAudio = !isPlayingAudio;
            });



            video.setWebViewClient(new WebViewClient());
            video.getSettings().setJavaScriptEnabled(true);
            video.loadUrl(ipaItem.getVideoUrl());
        }


        private void playAudio(String audioUrl) {
            // Sử dụng MediaPlayer để phát file audio
            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }




    }


}
