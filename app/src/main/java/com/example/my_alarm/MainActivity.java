package com.example.my_alarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ListView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    // Static Elements
    static final int NOTIFICATION_ID = 1;
    static final String CHANNEL_ID = "general";

    // Variable Declaration
    Button set_Alarm;
    TextClock text_clock;
    ListView listView;

    long alarms = 1;
    String time = "";
    final int timeGap = 1;
    int requestCode = 100;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        // Initializing values to variable
        set_Alarm = findViewById(R.id.set_alarm);
        text_clock = findViewById(R.id.textClock);
        listView = findViewById(R.id.list_view);

        text_clock.setFormat12Hour("hh:mm aa");
        LinkedList<String> list = new LinkedList<String>();
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        new mediaPlayerInstance(this, Settings.System.DEFAULT_RINGTONE_URI);

        // set alarm onclick listener
        set_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // current time in millis
                long time_ = System.currentTimeMillis();

                // if the button is not pressed continuously in one minute then the count of 5 minutes restart
                if (!sdf.format(time_).equalsIgnoreCase(time)) {
                    alarms = 1;
                }

                // if user press button at 1:01:22PM then next time will be for 1:06:00PM
                long modifiedTime = time_ + alarms++ * timeGap * 60 * 1000 - ((time_ / 1000) % 60) * 1000;
                list.add(sdf.format(modifiedTime));
                time = sdf.format(time_);
                update(list);
                System.out.println("SET FOR " + new SimpleDateFormat("hh:mm:ss aa").format(modifiedTime));
                // creating intent for alarm broadcast
                Intent intent = new Intent(MainActivity.this, MyBroadcastReceiver.class);
                intent.putExtra("TIME", modifiedTime);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, requestCode++, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, modifiedTime, pendingIntent);
            }
        });

        // on clock text change listener
        text_clock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /* this part deals with the deletion of alarms from displayed list
             * when time changes, and if current time and alarm set time is same then the set alarm will get removed from linkedlist*/

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (list.size() != 0 && text_clock.getText().toString().equalsIgnoreCase(list.getFirst())) {
                    Toast.makeText(MainActivity.this, "Alarm for " + text_clock.getText() + " is Active!", Toast.LENGTH_SHORT).show();
                    list.removeFirst();
                    update(list);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    // this method helps to update the list of alarms and display in linearlist view
    void update(LinkedList<String> list) {
        listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView textView1 = v.findViewById(android.R.id.text1);
                textView1.setText(list.get(position));
                textView1.setClickable(false);
                textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                return v;
            }
        });
    }
}