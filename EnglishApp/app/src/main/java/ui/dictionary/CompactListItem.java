package com.nhom11.englishapp.ui.dictionary;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.nhom11.englishapp.R;
import com.nhom11.englishapp.databinding.ItemCompactBinding;
import com.nhom11.englishapp.util.AnimatorEntry;
import com.nhom11.englishapp.util.DictionaryEntry;
import com.nhom11.englishapp.util.Speaker;

import java.io.IOException;


public class CompactListItem extends DictionaryListItem<ItemCompactBinding> {
    public CompactListItem(@NonNull Context context, DictionaryEntry entry) {
        super(context, entry);
        mB = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_compact, null, false);
        mB.setListeners(this);
        mB.setEntry(entry);
        mB.dictionaryItemTranslationList.setEnabled(false);
        mB.dictionaryItemTranslationList.setAdapter(mPosAdapter);
        mB.dictionaryItemSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(entry.getSound());
            }
        });
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



    @Override
    protected void collapse() {
        AnimatorSet slideUp = new AnimatorEntry(R.animator.card_slide_up, mB.dictionaryItemDetails).toSet(mCtx);
        slideUp.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                mB.dictionaryItemDetails.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        slideUp.start();
    }

    @Override
    protected void expand() {
        AnimatorSet slideDown = new AnimatorEntry(R.animator.card_slide_down, mB.dictionaryItemDetails).toSet(mCtx);
        slideDown.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                mB.dictionaryItemDetails.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        slideDown.start();

        loadImage(mB.dictionaryItemImg, mB.dictionaryItemNoInternet.getRoot());
    }

    public static class Factory implements DictionaryListItem.Factory {

        @Override
        public DictionaryListItem<ItemCompactBinding> create(Context context, DictionaryEntry entry) {
            return new CompactListItem(context, entry);
        }
    }
}
