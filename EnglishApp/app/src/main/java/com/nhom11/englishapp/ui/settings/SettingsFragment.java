//package com.nhom11.englishapp.ui.settings;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.SeekBar;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.databinding.DataBindingUtil;
//import androidx.fragment.app.Fragment;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.nhom11.englishapp.MainActivity;
//import com.nhom11.englishapp.R;
//import com.nhom11.englishapp.SignIn;
//import com.nhom11.englishapp.VocabNotifier;
//import com.nhom11.englishapp.databinding.FragmentSettingsBinding;
//import com.nhom11.englishapp.databinding.LearnedWordsBinding;
//import com.nhom11.englishapp.util.DictionaryEntry;
//import com.nhom11.englishapp.util.Settings;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {
//
//    private FragmentSettingsBinding mB;
//    private Settings mSt;
//    private DatabaseReference mDatabase;
//    private boolean mInit;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        mInit = true;
//        mSt = Settings.from(getContext());
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("user_words").child("user_id_here").child("learned_words"); // Thay user_id_here bằng user_id thực tế của người dùng
//        mB = FragmentSettingsBinding.inflate(inflater, container, false);
//        mB.settingDefault.setOnClickListener(this::confirmResetSettings);
//        mB.settingOpenLearnedWords.setOnClickListener(this::openLearnedWords);
//        mB.settingListViewType.setOnItemSelectedListener(this);
//        mB.settingMaxHearts.setOnSeekBarChangeListener(this);
//        mB.settingMaxProgress.setOnSeekBarChangeListener(this);
//        mB.settingVocabTest.setOnClickListener(this::testNotification);
//        mB.setMaxHearts(mSt.getMaxHearts());
//        mB.setMaxProgress(mSt.getMaxProgress());
//        mB.setSettings(mSt);
//        Activity activity = requireActivity();
//        mB.btnLogout.setOnClickListener(v -> {
//            startActivity(new Intent(activity, SignIn.class));
//        });
//        return mB.getRoot();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        mB.settingListViewType.setSelection(mSt.getListViewType(), false);
//    }
//
//    public void testNotification(View view) {
//        VocabNotifier.notifyWord(getContext());
//    }
//
//    @Override
//    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//        if (!b) return;
//        String[] tags = seekBar.getTag().toString().split(",");
//        String settingName = tags[0];
//        int min = Integer.parseInt(tags[1]),
//                step = Integer.parseInt(tags[2]),
//                value = seekBar.getProgress() * step + min;
//
//        mSt.set(settingName, value);
//
//        mB.setMaxHearts(mSt.getMaxHearts());
//        mB.setMaxProgress(mSt.getMaxProgress());
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//        if (mInit) {
//            mInit = false;
//            return;
//        }
//        mSt.set(adapterView.getTag().toString(), i);
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    public void confirmResetSettings(View view) {
//        new AlertDialog.Builder(getContext())
//                .setTitle(R.string.setting_reset_title)
//                .setMessage(R.string.setting_reset_text)
//                .setPositiveButton(R.string.setting_reset_yes, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        resetSettings();
//                    }
//                })
//                .setNegativeButton(R.string.setting_reset_no, null)
//                .show();
//    }
//
//    public void openLearnedWords(View view) {
//        List<String> allWords = new ArrayList<>();
//        for (DictionaryEntry entry : DictionaryEntry.getAll(getContext()))
//            allWords.add(entry.getName());
//
//        LearnedWordsBinding learnedWords = DataBindingUtil.inflate(getLayoutInflater(), R.layout.learned_words, null, false);
//        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, allWords) {
//            @NonNull
//            @Override
//            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                View view = super.getView(position, convertView, parent);
//                view.setBackgroundColor(
//                        mSt.getLearnedWords().contains(allWords.get(position))
//                                ? getContext().getColor(R.color.game_correct_500)
//                                : Color.TRANSPARENT
//                );
//                return view;
//            }
//        };
//        learnedWords.setLearnedWordsCount(mSt.getLearnedWords().size());
//        learnedWords.settingLearnedWordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String item = adapterView.getItemAtPosition(i).toString();
//                if (mSt.getLearnedWords().contains(item)) {
//                    view.setBackgroundColor(Color.TRANSPARENT);
//                    mDatabase.child(item).removeValue();
//                }
//                else {
//                    view.setBackgroundColor(getContext().getColor(R.color.game_correct_500));
//                    mDatabase.child(item).setValue(true);
//                }
//                learnedWords.setLearnedWordsCount(mSt.getLearnedWords().size());
//            }
//        });
//        learnedWords.settingLearnedWordsList.setAdapter(adapter);
//        dialog.setView(learnedWords.getRoot());
//        final AlertDialog dialog1 = dialog.show();
//        learnedWords.settingLearnedWordsCloseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog1.dismiss();
//            }
//        });
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> adapterView) {
//
//    }
//
//    private void resetSettings() {
//        mSt.clear();
//        restart();
//    }
//
//    private void restart() {
//        getActivity().finish();
//        Intent intent = new Intent(getContext(), MainActivity.class);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        mB = null;
//    }
//}

package com.nhom11.englishapp.ui.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.nhom11.englishapp.MainActivity;
import com.nhom11.englishapp.R;
import com.nhom11.englishapp.SignIn;
import com.nhom11.englishapp.VocabNotifier;
import com.nhom11.englishapp.databinding.FragmentSettingsBinding;
import com.nhom11.englishapp.databinding.LearnedWordsBinding;
import com.nhom11.englishapp.util.DictionaryEntry;
import com.nhom11.englishapp.util.Settings;

import java.util.ArrayList;
import java.util.List;

public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener, SeekBar.OnSeekBarChangeListener {

    private FragmentSettingsBinding mB;
    private Settings mSt;
    private boolean mInit;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInit = true;
        mSt = Settings.from(getContext());
        mB = FragmentSettingsBinding.inflate(inflater, container, false);
        mB.settingDefault.setOnClickListener(this::confirmResetSettings);
        mB.settingOpenLearnedWords.setOnClickListener(this::openLearnedWords);
        mB.settingListViewType.setOnItemSelectedListener(this);
        mB.settingMaxHearts.setOnSeekBarChangeListener(this);
        mB.settingMaxProgress.setOnSeekBarChangeListener(this);
        mB.settingVocabTest.setOnClickListener(this::testNotification);
        mB.setMaxHearts(mSt.getMaxHearts());
        mB.setMaxProgress(mSt.getMaxProgress());
        mB.setSettings(mSt);
//        Activity activity = requireActivity();
//        mB.btnLogout.setOnClickListener(v -> {
//            startActivity(new Intent(activity, SignIn.class));
//        });
        return mB.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        mB.btnLogout.setOnClickListener(v -> {
            logoutAndNavigateToSignIn();
        });
    }
    private void logoutAndNavigateToSignIn() {
        // Đăng xuất người dùng
        mAuth.signOut();
        // Chuyển đến màn hình đăng nhập
        Activity activity = requireActivity();
        Intent intent = new Intent(activity, SignIn.class);
        startActivity(intent);
        // Đóng Activity hiện tại
        activity.finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        mB.settingListViewType.setSelection(mSt.getListViewType(), false);
    }

    public void testNotification(View view) {
        VocabNotifier.notifyWord(getContext());
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        if (!b) return;
        String[] tags = seekBar.getTag().toString().split(",");
        String settingName = tags[0];
        int min = Integer.parseInt(tags[1]),
                step = Integer.parseInt(tags[2]),
                value = seekBar.getProgress() * step + min;

        mSt.set(settingName, value);

        mB.setMaxHearts(mSt.getMaxHearts());
        mB.setMaxProgress(mSt.getMaxProgress());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (mInit) {
            mInit = false;
            return;
        }
        mSt.set(adapterView.getTag().toString(), i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public void confirmResetSettings(View view) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.setting_reset_title)
                .setMessage(R.string.setting_reset_text)
                .setPositiveButton(R.string.setting_reset_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resetSettings();
                    }
                })
                .setNegativeButton(R.string.setting_reset_no, null)
                .show();
    }

    public void openLearnedWords(View view) {
        List<String> allWords = new ArrayList<>();
        for (DictionaryEntry entry : DictionaryEntry.getAll(getContext()))
            allWords.add(entry.getName());

        LearnedWordsBinding learnedWords = DataBindingUtil.inflate(getLayoutInflater(), R.layout.learned_words, null, false);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, allWords) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setBackgroundColor(
                        mSt.getLearnedWords().contains(allWords.get(position))
                                ? getContext().getColor(R.color.game_correct_500)
                                : Color.TRANSPARENT
                );
                return view;
            }
        };
        learnedWords.setLearnedWordsCount(mSt.getLearnedWords().size());
        learnedWords.settingLearnedWordsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                if (mSt.getLearnedWords().contains(item)) {
                    view.setBackgroundColor(Color.TRANSPARENT);
                    mSt.removeLearnedWord(item);
                }
                else {
                    view.setBackgroundColor(getContext().getColor(R.color.game_correct_500));
                    mSt.addLearnedWord(item);
                }
                learnedWords.setLearnedWordsCount(mSt.getLearnedWords().size());
            }
        });
        learnedWords.settingLearnedWordsList.setAdapter(adapter);
        dialog.setView(learnedWords.getRoot());
        final AlertDialog dialog1 = dialog.show();
        learnedWords.settingLearnedWordsCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void resetSettings() {
        mSt.clear();
        restart();
    }

    private void restart() {
        getActivity().finish();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mB = null;
    }
}
