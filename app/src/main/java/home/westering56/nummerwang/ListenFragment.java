package home.westering56.nummerwang;

import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListenFragment extends DialogFragment {

    private static final String TAG = "ListenFragment";


    private TestModel mTestModel;
    private EditText mNumberEditText;
    private TextToSpeech mTextToSpeech;
    private Button mSubmit;

    public ListenFragment() {
        // Required empty public constructor
    }

    public static ListenFragment newInstance() {
        ListenFragment fragment = new ListenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestModel = ViewModelProviders.of(this).get(TestModel.class);
        mTextToSpeech = new TextToSpeech(requireContext(), status -> {
            Log.d(TAG, "TextToSpeech initialised: " + status);
            int langStatus = mTextToSpeech.setLanguage(Locale.GERMANY);
            Log.d(TAG, "Setting text to speech language to German, status: " + langStatus);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_listen, container, false);
        mNumberEditText = v.findViewById(R.id.numberEditText);
        mSubmit = v.findViewById(R.id.submitButton);
        mSubmit.setOnClickListener(this::onSubmitClick);
        return v;
    }

    private void onSubmitClick(View view) {
        mNumberEditText.setText("");
        nextQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        nextQuestion();
    }

    void nextQuestion() {
        Log.d(TAG, "Requesting next question");
        mTestModel.nextQuestion();
        mTestModel.speakQuestion(mTextToSpeech);
        showSoftKeyboard(mNumberEditText);
    }

    public void showSoftKeyboard(View view) {
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


