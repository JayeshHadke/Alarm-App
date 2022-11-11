package com.example.my_alarm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class alarmFullPage extends AppCompatActivity {
    TextView timeLabel;
    Button clear_alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_full_page);
        getSupportActionBar().hide();

        timeLabel = findViewById(R.id.timeLabel);
        clear_alarm = findViewById(R.id.clear_alarm);

        Intent intent
                = getIntent();
        timeLabel.setText(intent.getStringExtra("TIME").trim());
        clear_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer mediaPlayer = mediaPlayerInstance.getMediaPlayerInstance();
                if (mediaPlayer != null) {
                    mediaPlayer.pause();
                }
                finish();
            }
        });
    }
}