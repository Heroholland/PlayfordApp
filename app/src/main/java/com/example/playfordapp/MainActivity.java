package com.example.playfordapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private final List<Date> holidays = new ArrayList<>(); //List of all holidays in the current month

    private void setupPeriodForToday(String dayType) {
        TextView periodTexts[] = {findViewById(R.id.p1Text), findViewById(R.id.p2Text), findViewById(R.id.p3Text), findViewById(R.id.p4Text)};
        if (dayType.equalsIgnoreCase("A")) {
            //A day
            for (int i = 0; i < 4; i++) {
                periodTexts[i].setText(String.valueOf(i+1) + "st Period");
                Log.w("myApp", String.valueOf(periodTexts[i]));
            }
        }

        if (dayType.equalsIgnoreCase("B")) {
            //A day
            for (int i = 0; i < 4; i++) {
                periodTexts[i].setText(String.valueOf(i+5) + "st Period");
                Log.w("myApp", String.valueOf(periodTexts[i]));
            }
        }
        if (dayType.equalsIgnoreCase("No School")) {
            //A day
            for (int i = 0; i < 4; i++) {
                periodTexts[i].setText("No School Today");
                Log.w("myApp", String.valueOf(periodTexts[i]));
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String response = null;
            try {
                response = NetworkUtils.fetchEvents();
                String finalResponse = response;
                handler.post(() -> {

                    if (finalResponse != null) {
                        List<Event> events = NetworkUtils.parseEvents(finalResponse);
                        for (Event e : events) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date edate;
                            try {
                                edate = formatter.parse(e.getEnd());
                                if (CalendarUtils.isDateHoliday(e.getCategoryTitle(), e.getTitle())) {
                                    holidays.add(edate);
                                    Log.w("myApp", String.valueOf(holidays));
                                    return;
                                }
                            } catch (Exception ex) {
                                Log.w("Exception", ex);
                            }
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        CalendarUtils cal = new CalendarUtils(holidays);
        Calendar today = Calendar.getInstance();
        //today.add(Calendar.DATE, 3); // For debugging purposes
        String dayType = cal.getDayType(today.getTime());
        Log.w("myApp", dayType);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Calendar calendar = Calendar.getInstance();
        String dateStr = calendar.getTime().toString();
        TextView dateTxt = (TextView) findViewById(R.id.dateText);
        dateTxt.setText(dateStr); //Display the current date
        setupPeriodForToday(dayType);
        TextView dateTypeTxt = (TextView) findViewById(R.id.dayTypeText);
        dateTypeTxt.setText("Today is an: " + dayType + " day");

        Button button = (Button) findViewById(R.id.nextScreenButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intentSend = new Intent(MainActivity.this, second_screen.class);
                startActivity(intentSend);
            }
        });


    }

}