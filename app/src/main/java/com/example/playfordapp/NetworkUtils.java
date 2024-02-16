package com.example.playfordapp;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.RequestBody;
import okhttp3.MediaType;

public class NetworkUtils {

    private static final OkHttpClient client = new OkHttpClient();
    private static final Gson gson = new Gson();
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
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://awsapieast1-prod22.schoolwires.com/REST/api/v4/CalendarEvents/GetEvents/1").newBuilder()
                .addQueryParameter("StartDate", "2024-04-01")
                .addQueryParameter("EndDate", "2024-04-30")
                .addQueryParameter("ModuleInstanceFilter", "")
                .addQueryParameter("CategoryFilter", "")
                .addQueryParameter("IsDBStreamAndShowAll", "true");

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Bearer " + fetchToken()) // Replace <your_token_here> with your actual token
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
