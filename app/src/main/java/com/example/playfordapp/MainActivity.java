package com.example.playfordapp;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
    private static final List<Date> holidays = new ArrayList<>(); //List of all holidays in the current month
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static final Calendar cal = Calendar.getInstance();

    // Assuming you have a method to get events already, let's add a method to check for holidays
    public static boolean isDateHoliday(String title, String catTitle) {
            if (catTitle.toLowerCase().contains("holiday") || catTitle.toLowerCase().contains("no classes") || title.toLowerCase().contains("holiday") || title.toLowerCase().contains("no classes")) {
                return true;
            }
        return false;
    }
    private boolean isHoliday(Date date) {
        for (Date holiday : holidays) {
            if (sdf.format(date).equals(sdf.format(holiday))) {
                return true;
            }
        }
        return false;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static boolean isWeekday(Date dateOfMonth) {
        Calendar cal=Calendar.getInstance();
        //LocalDate localDate = LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), dateOfMonth);
        //Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        cal.setTime(dateOfMonth);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1 || dayOfWeek == 7) {
            return false;
        }
        return true;
    }

    private String getDayType(Date date) {
        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(date);
        int count = 0;

        while (cal.before(currentCalendar)) {
            cal.add(Calendar.DATE, 1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!isWeekday(cal.getTime()) || isHoliday(cal.getTime())) {
                    continue; // Skip weekends and holidays
                }
            }
            count++;
        }

        return count % 2 == 0 ? "A" : "B";
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
                                if (isDateHoliday(e.getCategoryTitle(), e.getTitle())) {
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
        Log.w("myApp", getDayType(cal.getTime()));
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Calendar calendar = Calendar.getInstance();
        String dateStr = calendar.getTime().toString();
        TextView date = (TextView) findViewById(R.id.dateText);
        date.setText(dateStr);


    }
}