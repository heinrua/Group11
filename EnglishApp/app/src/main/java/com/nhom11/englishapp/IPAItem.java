package com.nhom11.englishapp;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.nhom11.englishapp.util.DictionaryEntry;

import java.util.List;

public class IPAItem {
    private String nameIPA;
    private String soundUrl;
    private String videoUrl;

    public IPAItem() {
    }
    public IPAItem(String nameIPA, String soundUrl, String videoUrl){
        this.nameIPA = nameIPA;
        this.soundUrl = soundUrl;
        this.videoUrl = videoUrl;
    }

    public String getNameIPA() {
        return nameIPA;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getSoundUrl() {
        return soundUrl;
    }


    public void setNameIPA(String nameIPA) {
        this.nameIPA = nameIPA;
    }



}
