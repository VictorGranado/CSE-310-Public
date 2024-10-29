package com.example.hydroponicmonitor;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.content.Intent;
import android.util.Log;

public class SecondActivity extends AppCompatActivity {

    private TextView temperatureView, humidityView, waterLevelView;
    private Button showHistoryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        temperatureView = findViewById(R.id.temperature);
        humidityView = findViewById(R.id.humidity);
        waterLevelView = findViewById(R.id.water_level);
        showHistoryButton = findViewById(R.id.show_history_button);

        // Fetch data from ESP32 server
        new FetchDataTask().execute("http://10.34.142.182/getStats");

        showHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to HistoryActivity
                Intent intent = new Intent(SecondActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    private class FetchDataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5000); // Set connection timeout
                urlConnection.setReadTimeout(5000); // Set read timeout

                InputStream in = urlConnection.getInputStream();
                byte[] buffer = new byte[1024];
                int bytesRead;
                StringBuilder response = new StringBuilder();
                while ((bytesRead = in.read(buffer)) != -1) {
                    response.append(new String(buffer, 0, bytesRead));
                }
                return response.toString();
            } catch (Exception e) {
                Log.e("FetchDataTask", "Error fetching data", e);
                return null; // Return null on error
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                temperatureView.setText("Unable to fetch temperature");
                humidityView.setText("Unable to fetch humidity");
                waterLevelView.setText("Unable to fetch water level");
                Log.e("FetchDataTask", "Result is null; possibly network or JSON error.");
                return;
            }

            try {
                JSONObject json = new JSONObject(result);
                double temperature = json.getDouble("temperature");
                double humidity = json.getDouble("humidity");
                int waterLevel = json.getInt("water_level");

                temperatureView.setText("Temperature: " + temperature + "°C");
                humidityView.setText("Humidity: " + humidity + "%");
                waterLevelView.setText("Water Level: " + waterLevel + "%");
            } catch (Exception e) {
                Log.e("FetchDataTask", "JSON parsing error: " + e.getMessage(), e);
                temperatureView.setText("Error parsing temperature data");
                humidityView.setText("Error parsing humidity data");
                waterLevelView.setText("Error parsing water level data");
            }
        }

    }
}
