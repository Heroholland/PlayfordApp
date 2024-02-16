package com.example.playfordapp;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class NetworkUtils {

    private static final OkHttpClient client = new OkHttpClient();

    public static String fetchEvents() {
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
                .addHeader("Authorization", "Bearer eyJhbGciOiJBMjU2S1ciLCJlbmMiOiJBMjU2Q0JDLUhTNTEyIn0.7iTWJWHehD8WFF_9UDrAjwxo4tXmYJhPKF04rB264j4dEseCyINNqlfFTPnKroBBO9lMpuXGYhT49kURAw1hDxxv-WhJjlfK.zzgpc5wvVcnEZNuwk-RcgQ.cg7XAqu4jXiTP4C0hEAyTvsrhEmM29t8ss8I1w6KzRJ12x96B89JlOl_QsO1Er3nB-Vz7RPlULdJmVqw7794BGt62CtHVJlEgHGLMynf7h2ElRZQPLmEplprpxGpRNxt2u3uR3iynWWUa6mXOgDBK8EYQnPMkASAh8RnmsQGVrcNLS4NJ60UEPoyYl6YeuSr_OhATOUvz_99zO7mwnDNDUVN1G7CmZVE9GrxbX-i3p8StK1kAB50kfeNhXyIjukXvJ7D9cvoL2i9XCPhOpIkzA.l3jZlJtYa7X_LWLvrAf3skcnBWMA4tTMHvyjTMhEVyA") // Replace <your_token_here> with your actual token
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
        Gson gson = new Gson();
        Type eventType = new TypeToken<List<Event>>(){}.getType();
        List<Event> eventList = gson.fromJson(jsonResponse, eventType);
        return eventList;
    }

}
