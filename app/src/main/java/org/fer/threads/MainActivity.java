package org.fer.threads;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText input;
    Button button;
    TextView errorMessage;
    public static final String VALUE_EXTRA = "VALUE EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input = findViewById(R.id.numero);
        button = findViewById(R.id.button);
        errorMessage = findViewById(R.id.error);
    }

    private boolean isDataValid(String userInput) {
        boolean isValid;
        try {
            int inputValue = Integer.parseInt(userInput);
            isValid = inputValue > 0 && inputValue <= 60;
        } catch (NumberFormatException e) {
            isValid = false;
        }
        return isValid;
    }

    public void startButton(View view) {
        String userInput = input.getText().toString();
        if (isDataValid(userInput)) {
            errorMessage.setVisibility(View.GONE);
            Intent intent = new Intent(this, Chrono.class);
            intent.putExtra(VALUE_EXTRA, userInput);
            startActivity(intent);
        } else {
            errorMessage.setVisibility(View.VISIBLE);
        }
    }

}
