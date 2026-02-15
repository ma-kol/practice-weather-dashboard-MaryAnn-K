package org.example;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {

    private String apiKey;

    public WeatherAPI(String apiKey) {
        this.apiKey = apiKey;
    }

    public WeatherData fetchCurrentWeather(String city) throws Exception {

        // Checks to make sure API key exists before making API call
        if (apiKey == null || apiKey.isEmpty()) {
            throw new Exception("API key is missing. Make sure OPENWEATHER_API_KEY is set.");
        }

        // Build the API URL
        String urlString = "https://api.openweathermap.org/data/2.5/weather?q="
                + city + "&units=imperial&appid=" + apiKey;

        // String turns into URL and opens connection to API
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int status = conn.getResponseCode();

        InputStream stream = (status == 200)
                ? conn.getInputStream()
                : conn.getErrorStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();
        conn.disconnect();

        String json = response.toString();

        double temp = getDouble(json, "\"temp\":");
        int humidity = (int) getDouble(json, "\"humidity\":");
        String description = getString(json, "\"description\":\"");

        return new WeatherData(city, temp, humidity, description);
    }

    private double getDouble(String json, String key) {
        int index = json.indexOf(key);
        if (index == -1)
            return 0;

        int start = index + key.length();
        int end = start;

        while (end < json.length() &&
                (Character.isDigit(json.charAt(end)) ||
                        json.charAt(end) == '.' ||
                        json.charAt(end) == '-')) {
            end++;
        }

        try {
            return Double.parseDouble(json.substring(start, end));
        } catch (Exception e) {
            return 0;
        }
    }

    private String getString(String json, String key) {
        int index = json.indexOf(key);
        if (index == -1)
            return "";

        int start = index + key.length();
        int end = start;

        while (end < json.length() && json.charAt(end) != '"') {
            end++;
        }

        return json.substring(start, end);
    }
}