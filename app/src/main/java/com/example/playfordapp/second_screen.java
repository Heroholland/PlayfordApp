package com.example.playfordapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class second_screen extends AppCompatActivity {

    private TextView timerTextView;
    private Button startStopButton;
    private Button backButton;
    private CountDownTimer countDownTimer;
    private boolean timerRunning = false;
    private long timeLeftInMillis = 60 * 10 * 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen); // Make sure to replace "your_layout_name" with the actual name of your XML layout file.

        Intent intent = getIntent();
        Log.w("myApp", "2: " + String.valueOf(intent.getIntExtra("timeLeftMillis", 60*1000)));
        timerTextView = findViewById(R.id.timerTextView);
        startStopButton = findViewById(R.id.startStopButton);
        backButton = findViewById(R.id.backScreenButton);

        startStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timerRunning) {
                    stopTimer();
                } else {
                    startTimer();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(second_screen.this, MainActivity.class);
                intent.putExtra("timeLeftMillis", timeLeftInMillis);
                startActivity(intent);
            }
        });



        updateTimerText();
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimerText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                startStopButton.setText("Commencer");
                timeLeftInMillis = 60000; // Reset to 1 minute
                updateTimerText();
            }
        }.start();

        timerRunning = true;
        startStopButton.setText("Pause");//
    }

    private void stopTimer() {
        countDownTimer.cancel();
        timerRunning = false;
        startStopButton.setText("Commencer");
    }

    private void updateTimerText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);
        timerTextView.setText(timeFormatted);
    }
}