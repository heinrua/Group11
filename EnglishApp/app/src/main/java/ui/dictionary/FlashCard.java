package com.nhom11.englishapp.ui.dictionary;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.nhom11.englishapp.R;
import com.nhom11.englishapp.databinding.FlashcardBackBinding;

import com.nhom11.englishapp.databinding.FlashcardBinding;
import com.nhom11.englishapp.databinding.FlashcardFrontBinding;
import com.nhom11.englishapp.util.AnimatorEntry;
import com.nhom11.englishapp.util.DictionaryEntry;
import com.nhom11.englishapp.util.Speaker;

import java.io.IOException;


public class FlashCard extends DictionaryListItem<FlashcardBinding> {
    private final FlashcardFrontBinding mFB;
    private final FlashcardBackBinding mBB;

    public FlashCard(@NonNull Context context, DictionaryEntry entry) {
        super(context, entry);
        mB = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.flashcard, null, false);
        mB.setListeners(this);
        mB.setEntry(entry);
        mFB = mB.cardFront;
        mBB = mB.cardBack;
        mBB.dictionaryItemTranslationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                toggleDetails(view);
            }
        });
        mBB.dictionaryItemTranslationList.setAdapter(mPosAdapter);
        mBB.dictionaryItemSpeak.setOnClickListener(new View.OnClickListener() {
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
    public void toggleDetails(View view) {
        super.toggleDetails(view);
        AnimatorSet zoomOutIn1 = new AnimatorEntry(R.animator.zoom_out_in, mFB.getRoot()).toSet(mCtx);
        AnimatorSet zoomOutIn2 = new AnimatorEntry(R.animator.zoom_out_in, mBB.getRoot()).toSet(mCtx);
        zoomOutIn1.start();
        zoomOutIn2.start();
    }

    @Override
    protected void collapse() {
        AnimatorSet showAnim = new AnimatorEntry(R.animator.card_show, mFB.getRoot()).toSet(mCtx);
        AnimatorSet hideAnim = new AnimatorEntry(R.animator.card_hide, mBB.getRoot()).toSet(mCtx);
        hideAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                mFB.getRoot().setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                mBB.getRoot().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        showAnim.start();
        hideAnim.start();
    }

    @Override
    protected void expand() {
        AnimatorSet showAnimRev = new AnimatorEntry(R.animator.card_show_rev, mBB.getRoot()).toSet(mCtx);
        AnimatorSet hideAnimRev = new AnimatorEntry(R.animator.card_hide_rev, mFB.getRoot()).toSet(mCtx);
        hideAnimRev.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(@NonNull Animator animator) {
                mBB.getRoot().setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(@NonNull Animator animator) {
                mFB.getRoot().setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationCancel(@NonNull Animator animator) {

            }

            @Override
            public void onAnimationRepeat(@NonNull Animator animator) {

            }
        });
        hideAnimRev.start();
        showAnimRev.start();

        loadImage(mB.cardBack.dictionaryItemImg, mB.cardBack.dictionaryItemNoInternet.getRoot());
    }

    public static class Factory implements DictionaryListItem.Factory {
        @Override
        public DictionaryListItem<FlashcardBinding> create(Context context, DictionaryEntry entry) {
            return new FlashCard(context, entry);
        }
    }
}