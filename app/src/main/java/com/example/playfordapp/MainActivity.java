package com.example.playfordapp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.playfordapp.NetworkUtils;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    // Assuming you have a method to get events already, let's add a method to check for holidays
    public static boolean isTodayAHoliday(List<Event> events) {
        String todayStr = sdf.format(new Date());
        for (Event event : events) {
            if (event.getCategoryTitle().toLowerCase().contains("holiday")) {
                String eventDateStr = event.getStart().split("T")[0]; // Assuming the date format is "yyyy-MM-ddTHH:mm:ss"
                if (todayStr.equals(eventDateStr)) {
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            String response = null;
            try {
                response = NetworkUtils.fetchEvents();
                String finalResponse = response;
                handler.post(() -> {
                    // Update the UI with `response`
                    //TextView textView = findViewById(R.id.textView);
                    //textView.setText(response);
                    if (finalResponse != null) {
                        List<Event> events = NetworkUtils.parseEvents(finalResponse);
                        Log.w("myApp","Holiday: " + isTodayAHoliday(events));
                        for (Event e : events) {
                            Log.w("myApp", e.toString());
                        }
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}