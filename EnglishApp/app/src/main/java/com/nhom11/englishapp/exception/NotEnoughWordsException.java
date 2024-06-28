package com.nhom11.englishapp.exception;

public class NotEnoughWordsException extends RuntimeException {
    public static final int UNKNOWN = -1;
    private final int mMinimumWordsToLearn, mCurrentLearnedWords;

    public NotEnoughWordsException(int minimumWordsToLearn, int currentLearnedWords) {
        super("Expected minimum of " + (minimumWordsToLearn == UNKNOWN ? "UNKNOWN" : minimumWordsToLearn) + ", got " + currentLearnedWords);
        mMinimumWordsToLearn = minimumWordsToLearn;
        mCurrentLearnedWords = currentLearnedWords;
    }

    public NotEnoughWordsException(String message, int minimumWordsToLearn, int currentLearnedWords) {
        super(message);
        mMinimumWordsToLearn = minimumWordsToLearn;
        mCurrentLearnedWords = currentLearnedWords;
    }

    public int getMinimumWordsToLearn() {
        return mMinimumWordsToLearn;
    }

    public int getCurrentLearnedWords() {
        return mCurrentLearnedWords;
    }
}
