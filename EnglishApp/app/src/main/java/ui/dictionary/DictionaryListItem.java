package com.nhom11.englishapp.ui.dictionary;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nhom11.englishapp.PosAdapter;
import com.nhom11.englishapp.R;
import com.nhom11.englishapp.util.DictionaryEntry;
import com.nhom11.englishapp.util.PosEntry;
import com.nhom11.englishapp.util.Settings;
import com.nhom11.englishapp.util.Speaker;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class DictionaryListItem<T extends ViewDataBinding> {
    protected T mB;
    protected final Context mCtx;
    protected final PosAdapter mPosAdapter;
    protected final DictionaryEntry mEntry;
    protected final Settings mSt;

    protected boolean mIsExpanded;

//    public DictionaryListItem(Context context) {
//        this.mCtx = context;
//    }
    protected DictionaryListItem(Context context, DictionaryEntry entry) {
        mCtx = context;
        mSt = Settings.from(context);
        mEntry = entry;

        ArrayList<PosEntry> posArray = new ArrayList<>();
        for (String[] posValue: new String[][] {
                {context.getString(R.string.dictionary_word_type_n), entry.getN()},
                {context.getString(R.string.dictionary_word_type_v), entry.getV()},
                {context.getString(R.string.dictionary_word_type_a), entry.getA()},
                {context.getString(R.string.dictionary_word_type_adv), entry.getAdv()},
                {context.getString(R.string.dictionary_word_type_prep), entry.getPrep()},
                {context.getString(R.string.dictionary_word_type_inj), entry.getInj()},
                {context.getString(R.string.dictionary_word_type_topic), entry.getTopic()}
        }) if (posValue[1] != null) posArray.add(new PosEntry(posValue[0], posValue[1].split("\\|")));
        mPosAdapter = new PosAdapter(context, posArray);
    }

    public View getView() {
        return mB.getRoot();
    }

    /**
     * Bày ra hoặc thu gọn item
     */
    public void saveLearnedWord(View view) {
        if (!mSt.getLearnedWords().contains(mEntry.getName()))
            mSt.addLearnedWord(mEntry.getName());
    }
    public void removeLearnedWord(View view) {
        if (!mSt.getLearnedWords().contains(mEntry.getName()))
            mSt.removeLearnedWord(mEntry.getName());

    }

    public void toggleDetails(View view){
        mIsExpanded = !mIsExpanded;
        if (mIsExpanded) expand(); else collapse();
    }

    /**
     * Thu gọn item, chỉ để lại từ tiếng Anh
     */
    protected abstract void collapse();

    /**
     * Bày ra các chi tiết (từ tiếng Anh, dịch nghĩa, hình ảnh và phiên âm)
     */
    protected abstract void expand();



    /**
     * Phiên âm
     */
//    public void speak(View view) {
//        mSpeaker.speak(view);
//    }

    public void loadImage(ImageView imageView, View noInternetView) {
        if (mEntry.getImg() == null) return;
        Glide.with(mCtx)
                .load(mEntry.getImg())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        noInternetView.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        noInternetView.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imageView);
    }



    public interface Factory {
        DictionaryListItem<? extends ViewDataBinding> create(Context context, DictionaryEntry entry);
    }
}
