package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // Load API key from environment variable
        String apiKey = System.getenv("OPENWEATHER_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            System.out.println("Error: OPENWEATHER_API_KEY environment variable is not set.");
            return;
        }

        // Weather Dashboard
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("---Weather Dashboard---");
            System.out.println("Enter your choice (1-5) from the following list: ");
            System.out.println("1. Charlotte");
            System.out.println("2. Chicago");
            System.out.println("3. Seattle");
            System.out.println("4. Austin");
            System.out.println("5. Exit");

            int choice;
            try {
                choice = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                return;
            }

            String city = "";
            switch (choice) {
                case 1:
                    city = "Charlotte";
                    break;
                case 2:
                    city = "Chicago";
                    break;
                case 3:
                    city = "Seattle";
                    break;
                case 4:
                    city = "Austin";
                    break;
            }
            if (choice == 5) {
                System.out.println("Exiting the program. Goodbye!");
                running = false;
                continue;
            } else if (city != "") {
                System.out.println("You selected: " + city);
            }
            try {
                WeatherAPI weatherAPI = new WeatherAPI(apiKey);
                WeatherData data = weatherAPI.fetchCurrentWeather(city);
                System.out.println(data);
            } catch (Exception e) {
                System.out.println("Error fetching weather data: " + e.getMessage());
            }
        }
        scanner.close();
    }
}