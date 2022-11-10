package com.example.my_alarm;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;

public class mediaPlayerInstance {
    private static MediaPlayer mediaPlayer;

    public mediaPlayerInstance(MainActivity mainActivity, Uri defaultRingtoneUri) {
        mediaPlayer = MediaPlayer.create(mainActivity, defaultRingtoneUri);
    }

    public static MediaPlayer getMediaPlayerInstance() {
        return mediaPlayer;
    }

}
