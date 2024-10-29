package com.example.hydroponicmonitor;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // This is where you would load data from the server and display the history of stats
        // Use graph libraries like MPAndroidChart to visualize the data
    }
}
