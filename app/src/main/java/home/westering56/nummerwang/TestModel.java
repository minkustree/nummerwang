package home.westering56.nummerwang;

import android.speech.tts.TextToSpeech;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import java.util.Random;

public class TestModel extends ViewModel {
    private static final int DEFAULT_BOUND = 100;
    private final Random mRandom;
    private final int mBound;

    int mQuestion;

    public TestModel() {
        this(DEFAULT_BOUND);
    }

    public TestModel(int bound) {
        mRandom = new Random();
        mBound = bound;
    }

    public void nextQuestion() {
        mQuestion = mRandom.nextInt(mBound);
    }

    public boolean isCorrect(int guess) {
        return guess == mQuestion;
    }

    public int getCorrectAnswer() {
        return mQuestion;
    }

    public void speakQuestion(@NonNull TextToSpeech tts) {
        String question = Integer.toString(mQuestion);
        tts.speak(question, TextToSpeech.QUEUE_ADD, null, question);
    }
}
