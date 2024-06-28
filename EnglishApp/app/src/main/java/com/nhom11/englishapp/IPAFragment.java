package com.nhom11.englishapp;

import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IPAFragment extends Fragment {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private IPAItemsAdapter adapter;
    private List<IPAItem> ipaItemList = new ArrayList<>();
    private ImageButton repeatIPA, reListen;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String currentAudioFilePath; // Lưu trữ đường dẫn file âm thanh hiện tại

    private boolean isRecording = false, isPlaying = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ipa, container, false);
        recyclerView = view.findViewById(R.id.recycle_ipa);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        repeatIPA= view.findViewById(R.id.btn_repeat_IPA);
        reListen = view.findViewById(R.id.btn_relisten);
        db = FirebaseFirestore.getInstance();
        CollectionReference ipasRef = db.collection("ipas");

        ipasRef.addSnapshotListener((snapshot, e) -> {
            if (e != null) {
                // Xử lý lỗi
                return;
            }

            ipaItemList.clear();
            for (DocumentSnapshot document : snapshot.getDocuments()) {
                String nameIPA = document.getString("nameIPA");
                String soundURL = document.getString("soundURL");
                String videoURL = document.getString("video");

                IPAItem ipaItem = new IPAItem(nameIPA, soundURL, videoURL);
                ipaItemList.add(ipaItem);
            }

            adapter.notifyDataSetChanged();
        });

        adapter = new IPAItemsAdapter(ipaItemList);
        recyclerView.setAdapter(adapter);

        repeatIPA.setOnClickListener(v -> {
            if (!isRecording) {
                repeatIPA.setImageResource(R.drawable.pause);
                startRecording();
            } else {
                stopRecording();
                repeatIPA.setImageResource(R.drawable.speak);
            }
        });

        reListen.setOnClickListener(v -> {
            if (!isPlaying) {
                playAudio(currentAudioFilePath);
            } else {
                stopPlaying();
            }
        });


        return view;
    }
    public void startRecording() {
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            currentAudioFilePath = getContext().getExternalCacheDir().getAbsolutePath() + "/recording.3gp";
            mediaRecorder.setOutputFile(currentAudioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            isRecording = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopRecording() {
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        isRecording = false;
    }

    private void playAudio(String filePath) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stopPlaying() {
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
        isPlaying = false;
    }

}
