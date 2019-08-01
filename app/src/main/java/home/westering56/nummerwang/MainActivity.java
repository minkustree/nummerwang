package home.westering56.nummerwang;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static class MainActivityViewModel extends ViewModel {
        public MainActivityViewModel() {
        }

    }

    private MainActivityViewModel mViewModel;
    private TextView mLogTextView;
    Button mListenButton;
    private TextToSpeech mTextToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        mLogTextView = findViewById(R.id.logTextView);

        findViewById(R.id.speakButton).setOnClickListener(this::onSpeakingClick);

        mListenButton = findViewById(R.id.listenButton);
        mListenButton.setEnabled(false);
        mListenButton.setOnClickListener(this::onListenClick);

        mTextToSpeech = new TextToSpeech(this, status -> {
            mLogTextView.append("TextToSpeech initialised: " + status);
            int langStatus = mTextToSpeech.setLanguage(Locale.GERMANY);
            mLogTextView.append("Setting text to speech language to German: " + langStatus + '\n');
            mListenButton.setEnabled(status == TextToSpeech.SUCCESS);
        });
    }

    private void onSpeakingClick(@NonNull View view) {
        mLogTextView.setText("");
        Intent intent = buildSpeechRecognizerIntent("42");
        startActivityForResult(intent, 0);
    }

    private Intent buildSpeechRecognizerIntent(@NonNull String prompt) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, prompt);
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0) {
            return;
        }
        if (resultCode != RESULT_OK) {
            mLogTextView.append("Speech recognition failed: " + resultCode + '\n');
            return;
        }
        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        float[] confidences = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
        for (int i = 0; i < results.size(); i++) {
            mLogTextView.append(results.get(i) + ": " + confidences[i] + '\n');
        }
    }

    @Override
    protected void onDestroy() {
        if (mTextToSpeech != null) {
            mTextToSpeech.shutdown();
            mTextToSpeech = null;
        }
        super.onDestroy();
    }

    public void onListenClick(View v) {
        mTextToSpeech.speak("42", TextToSpeech.QUEUE_ADD, null, "42");
    }
}
