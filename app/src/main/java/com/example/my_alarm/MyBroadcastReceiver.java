package com.example.my_alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.logging.Handler;

public class MyBroadcastReceiver extends BroadcastReceiver {
    // instance variable
    MediaPlayer mediaPlayer;

    @Override
    public void onReceive(Context context, Intent intent) {
        String content = String.valueOf(new SimpleDateFormat("hh:mm aa").format(intent.getLongExtra("TIME", System.currentTimeMillis())));
        String subText = "Alarm for " + new SimpleDateFormat("hh:mm aa").format(intent.getLongExtra("TIME", System.currentTimeMillis()));

        mediaPlayer = mediaPlayerInstance.getMediaPlayerInstance();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        mediaPlayer.start();


        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Drawable drawable
                = ResourcesCompat.getDrawable(context.getResources(), R.drawable.alarm, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap largeBitmap = bitmapDrawable.getBitmap();
        Notification notification;

        Intent snoozeIntent = new Intent(context, onActionReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1001, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context)
                    .setLargeIcon(largeBitmap)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.alarm)
                    .setContentText(content)
                    .setSubText(subText)
                    .setActions(new Notification.Action(R.drawable.alarm, "Snooze", pendingIntent))
                    .setChannelId(MainActivity.CHANNEL_ID)
                    .build();
            nm.createNotificationChannel(new NotificationChannel(MainActivity.CHANNEL_ID, "New Channel", NotificationManager.IMPORTANCE_HIGH));
        } else {
            notification = new Notification.Builder(context)
                    .setLargeIcon(largeBitmap)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.alarm)
                    .setContentText(content)
                    .setSubText(subText)
                    .build();
        }
        nm.notify(MainActivity.NOTIFICATION_ID, notification);

    }
}
