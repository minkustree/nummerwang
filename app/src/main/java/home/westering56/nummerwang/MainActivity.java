package home.westering56.nummerwang;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "NummerWangMain";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.speakButton).setOnClickListener(v -> {
            Intent intent = buildSpeechRecognizerIntent();
            startActivityForResult(intent, 0);
        });
        findViewById(R.id.listenButton).setOnClickListener(this::onListenClick);

    }

    private Intent buildSpeechRecognizerIntent() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "de-DE");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "42");
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 0) {
            return;
        }
        if (resultCode != RESULT_OK) {
            Log.d(TAG, "Speech recognition failed: " + resultCode);
            return;
        }
        if (data == null) {
            Log.w(TAG, "No speech data returned");
            return;
        }
        ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        float[] confidences = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
        for (int i = 0; i < results.size(); i++) {
            Log.d(TAG, results.get(i) + ": " + confidences[i]);
        }
    }

    /**
     * Show the listening exercise dialog.
     */
    public void onListenClick(View v) {
        ListenFragment fragment = ListenFragment.newInstance();
        fragment.show(getSupportFragmentManager(), "listen");
    }
}
