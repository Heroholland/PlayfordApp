package com.holland.playfordapp;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.List;
import okhttp3.RequestBody;
import okhttp3.MediaType;

public class NetworkUtils {

    private static final OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();
    private static final Gson gson = new Gson();
    private static final Calendar calendar = Calendar.getInstance();

    public static String fetchToken() throws IOException {
        String url = "https://www.houstonisd.org/Generator/TokenGenerator.ashx/ProcessRequest";

        // Assuming the request is a POST request. If it's a GET request, adjust accordingly.
        RequestBody body = RequestBody.create("", MediaType.get("application/json; charset=utf-8")); // Empty body for example

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                // Add other headers here
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String jsonResponse = response.body().string();

            // Parse the JSON response to extract the token
            JsonObject responseJson = gson.fromJson(jsonResponse, JsonObject.class);
            return responseJson.get("Token").getAsString();
        }
    }

    public static String fetchEvents() throws IOException {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH); // Note: January is 0, December is 11
        int firstDay = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String token = fetchToken();
        Log.w("myApp", "token: " + token);
        String startDate = String.format("%d-%02d-%02d", year, month + 1, firstDay); // Adding 1 to month as it starts from 0
        String endDate = String.format("%d-%02d-%02d", year, month + 1, lastDay);
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://awsapieast1-prod22.schoolwires.com/REST/api/v4/CalendarEvents/GetEvents/1").newBuilder()
                .addQueryParameter("StartDate", startDate)
                .addQueryParameter("EndDate", endDate)
                .addQueryParameter("ModuleInstanceFilter", "")
                .addQueryParameter("CategoryFilter", "")
                .addQueryParameter("IsDBStreamAndShowAll", "true");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + token) // Replace <your_token_here> with your actual token
                // Add other headers here
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<Event> parseEvents(String jsonResponse) {
        Type eventType = new TypeToken<List<Event>>(){}.getType();
        List<Event> eventList = gson.fromJson(jsonResponse, eventType);
        return eventList;
    }

}
