package org.fer.threads;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Chrono extends AppCompatActivity {

    ProgressBar progressBar;
    TextView countDown;
    Button button;
    TextView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chrono);
        progress = findViewById(R.id.progress);
        countDown = findViewById(R.id.countDown);
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);
        int inputValue = getValue();
    }

    private int getValue() {
        String value = getIntent().getStringExtra(MainActivity.VALUE_EXTRA);
        try {
            if (value == null) value = "0";
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public class CountDownActivity extends AppCompatActivity {
        private class CountDownAsyncTask extends AsyncTask<Integer, Integer, Integer> {
            @Override
            protected Integer doInBackground(Integer... integers) {
                if (integers.length == 0) return 0;
                int value = integers[0];
                for (int i = 0; i < value; i++) {
                    try {
                        publishProgress(value, i);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return 0;
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
                if (values.length > 1) {
                    int max = values[0];
                    int current = values[1];
                    int progressValue = current * 100 / max;
                    progress.setText(String.valueOf(progressValue));
                    progressBar.setProgress(progressValue);
                    countDown.setText(String.valueOf(max - current));
                }
            }

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                countDown.setText(String.valueOf(0));
                progress.setText(String.valueOf(100));
                progressBar.setProgress(100);
                button.setVisibility(View.VISIBLE);
            }

        }

        public void closeActivity(View view) {
            finish();
        }

    }

}