package com.example.youtubevideosearchapp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class YouTubeApiHelper {

    private static final String API_KEY = "AIzaSyAEk7F_bbhTFUWxwJXDn5fzxviwCJYk7EY";
    public static Exception lastException = null;

    public static List<VideoItem> searchVideos(String query) {
        List<VideoItem> results = new ArrayList<>();
        lastException = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;

        try {
            String encodedQuery = URLEncoder.encode(query, "UTF-8");
            String urlStr = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&q=" +
                    encodedQuery + "&maxResults=10&key=" + API_KEY;

            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                inputStream = conn.getInputStream();
            } else {
                inputStream = conn.getErrorStream();
                if (inputStream == null) {
                    throw new RuntimeException("HTTP Error " + responseCode + ", no error details available.");
                }
                try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(inputStream))) {
                    StringBuilder errorBuilder = new StringBuilder();
                    String line;
                    while ((line = errorReader.readLine()) != null) {
                        errorBuilder.append(line);
                    }
                    throw new RuntimeException("HTTP Error " + responseCode + ": " + errorBuilder.toString());
                }
            }

            // قراءة الاستجابة الناجحة
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }

                JSONObject jsonResponse = new JSONObject(jsonBuilder.toString());
                JSONArray items = jsonResponse.getJSONArray("items");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject snippet = items.getJSONObject(i).getJSONObject("snippet");
                    VideoItem video = new VideoItem();
                    video.title = snippet.getString("title");
                    video.description = snippet.getString("description");
                    video.publishTime = snippet.getString("publishedAt");
                    video.channelTitle = snippet.getString("channelTitle");
                    video.thumbnailUrl = snippet.getJSONObject("thumbnails")
                            .getJSONObject("medium")
                            .getString("url");
                    results.add(video);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            lastException = e;
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return results;
    }
}
