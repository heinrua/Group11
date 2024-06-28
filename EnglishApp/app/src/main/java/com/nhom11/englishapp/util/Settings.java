package com.nhom11.englishapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Settings {
    public static final String SETTING_IN_GAME_SOUND = "sound",
            SETTING_FAVORITES = "favorites",
            SETTING_LIST_VIEW_TYPE = "list_view_type",
            SETTING_VOCAB_NOTIFICATION = "vocab_notification",
            SETTING_MAX_HEARTS = "max_hearts",
            SETTING_MAX_PROGRESS = "max_progress",
            SETTING_LEARNED_WORDS = "learned_words";

    private DatabaseReference mDatabase;
    private List<String> mLearnedWords;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEd;

    private Settings(Context context) {
        mSp = context.getSharedPreferences("dictionary", Context.MODE_PRIVATE);
        mEd = mSp.edit();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("user_words").child("3").child("learned_words");
        mLearnedWords = new ArrayList<>();

        // Load learned words from Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mLearnedWords.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String word = snapshot.getKey();
                    mLearnedWords.add(word);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }

    public static Settings from(Context context) {
        return new Settings(context);
    }

    public boolean getInGameSound() {
        return mSp.getBoolean(SETTING_IN_GAME_SOUND, true);
    }

    public void setInGameSound(boolean inGameSound) {
        mEd.putBoolean(SETTING_IN_GAME_SOUND, inGameSound).apply();
    }

    public int getListViewType() {
        return mSp.getInt(SETTING_LIST_VIEW_TYPE, DictionaryEntry.VIEW_TYPE_FLASHCARD);
    }

    public void setListViewType(int listViewType) {
        mEd.putInt(SETTING_LIST_VIEW_TYPE, listViewType).apply();
    }

    public int getMaxHearts() {
        return mSp.getInt(SETTING_MAX_HEARTS, 5);
    }

    public void setMaxHearts(int maxHearts) {
        mEd.putInt(SETTING_MAX_HEARTS, maxHearts).apply();
    }

    public int getMaxProgress() {
        return mSp.getInt(SETTING_MAX_PROGRESS, 10);
    }

    public void setMaxProgress(int maxProgress) {
        mEd.putInt(SETTING_MAX_PROGRESS, maxProgress).apply();
    }

    public boolean getVocabNotification() {
        return mSp.getBoolean(SETTING_VOCAB_NOTIFICATION, true);
    }

    public void setVocabNotification(boolean vocabNotification) {
        mEd.putBoolean(SETTING_VOCAB_NOTIFICATION, vocabNotification).apply();
    }

    public List<String> getLearnedWords() {
        return mLearnedWords;
    }

    public void addLearnedWord(String learnedWord) {
        mDatabase.child(learnedWord).setValue(true);
    }

    public void removeLearnedWord(String learnedWord) {
        mDatabase.child(learnedWord).removeValue();
    }

    public List<String> getFavorites() {
        return new ArrayList<>(mSp.getStringSet(SETTING_FAVORITES, new HashSet<>()));
    }

    public void setFavorites(List<String> favorites) {
        mEd.putStringSet(SETTING_FAVORITES, new HashSet<>(favorites)).apply();
    }

    public void addFavorite(String favorite) {
        List<String> favorites = getFavorites();
        favorites.add(favorite);
        mEd.putStringSet(SETTING_FAVORITES, new HashSet<>(favorites)).apply();
    }

    public void removeFavorite(String favorite) {
        List<String> favorites = getFavorites();
        favorites.remove(favorite);
        mEd.putStringSet(SETTING_FAVORITES, new HashSet<>(favorites)).apply();
    }

    public void set(String setting, int value) {
        mEd.putInt(setting, value).apply();
    }

    public void get(String setting, int defaultValue) {
        mSp.getInt(setting, defaultValue);
    }

    public void clear() {
        mEd.clear().apply();
    }
}
