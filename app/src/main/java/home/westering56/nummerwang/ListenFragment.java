package home.westering56.nummerwang;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Locale;


public class ListenFragment extends DialogFragment {

    private static final String TAG = "ListenFragment";


    private TestModel mTestModel;
    private EditText mNumberEditText;
    private TextToSpeech mTextToSpeech;

    public ListenFragment() {
        // Required empty public constructor
    }

    static ListenFragment newInstance() {
        return new ListenFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestModel = ViewModelProviders.of(this).get(TestModel.class);
        mTextToSpeech = new TextToSpeech(requireContext(), status -> {
            Log.d(TAG, "TextToSpeech initialised: " + status);
            int langStatus = mTextToSpeech.setLanguage(Locale.GERMANY);
            Log.d(TAG, "Setting text to speech language to German, status: " + langStatus);
            nextQuestion();
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listen, container, false);
        mNumberEditText = v.findViewById(R.id.numberEditText);
        mNumberEditText.setOnEditorActionListener((v1, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                checkAnswerAndNextQuestion();
                return true;
            } else { return false; }
        });
        Button submit = v.findViewById(R.id.submitButton);
        submit.setOnClickListener(v1 -> checkAnswerAndNextQuestion());
        return v;
    }

    private void checkAnswerAndNextQuestion() {
        String text = mNumberEditText.getText().toString();
        if (text.length() > 0) {
            int guess = Integer.parseInt(mNumberEditText.getText().toString());
            showResult(mTestModel.isCorrect(guess), mTestModel.getCorrectAnswer());
        }
        mNumberEditText.setText("");
        nextQuestion();
    }


    private void nextQuestion() {
        Log.d(TAG, "Requesting next question");
        mTestModel.nextQuestion();
        mTestModel.speakQuestion(mTextToSpeech);
        showSoftKeyboard(mNumberEditText);
    }

    private void showResult(boolean isSuccess, int answer) {
        Toast toast = Toast.makeText(requireContext(),
                isSuccess ? getString(R.string.correct_toast, answer) :
                            getString(R.string.incorrect_toast, answer),
                Toast.LENGTH_SHORT);
        toast.setGravity(isSuccess ? Gravity.TOP : Gravity.CENTER, 0, 0);
        toast.show();
    }

    private void showSoftKeyboard(View view) {
        // https://developer.android.com/training/keyboard-input/visibility
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.shutdown();
            mTextToSpeech = null;
        }
        super.onDestroy();
    }

}


