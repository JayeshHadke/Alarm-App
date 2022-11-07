package com.example.my_alarm;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
    Button set_Alarm, clearAlarm;
    TextClock text_clock;
    ListView listView;
    long alarms = 1;
    String time = "";
    final int timeGap = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        set_Alarm = findViewById(R.id.set_alarm);
        clearAlarm = findViewById(R.id.clear_alarm);
        text_clock = findViewById(R.id.textClock);
        text_clock.setFormat12Hour("hh:mm aa");
        listView = findViewById(R.id.list_view);
        LinkedList<String> list = new LinkedList<String>();

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        set_Alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                long time_ = System.currentTimeMillis();
                if (!sdf.format(time_).equalsIgnoreCase(time)) {
                    alarms = 1;
                }
                long modifiedTime = time_ + alarms++ * timeGap * 60 * 1000;
                list.add(sdf.format(modifiedTime));
                time = sdf.format(time_);
                update(list);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 100, new Intent(MainActivity.this, MyBroadcastReceiver.class), PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP, modifiedTime, pendingIntent);
            }
        });
        clearAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size() == 0) {
                    Toast.makeText(MainActivity.this, "No Alarm Set!", Toast.LENGTH_SHORT).show();
                }
                list.clear();
                update(list);
            }
        });
        text_clock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (list.size() != 0 && text_clock.getText().toString().equalsIgnoreCase(list.getFirst())) {
                    System.out.println("In side ---------------");
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