package com.example.EventPlanner.activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.event.EventReport;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventStatsActivity extends AppCompatActivity {
    private BarChart reviewChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_stats);

        reviewChart = findViewById(R.id.review_chart);

        int eventId = getIntent().getIntExtra("EVENT_ID", -1);
        if (eventId != -1) {
            fetchAndDisplayStats(eventId);
        }
    }

    private void fetchAndDisplayStats(int eventId) {
        ClientUtils.eventService.getEventReport(eventId).enqueue(new Callback<EventReport>() {
            @Override
            public void onResponse(Call<EventReport> call, Response<EventReport> response) {
                if (response.isSuccessful() && response.body() != null) {
                    EventReport report = response.body();

                    // Prepare chart data
                    List<BarEntry> entries = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        entries.add(new BarEntry(i + 1, report.getGradeCounts()[i]));
                    }

                    BarDataSet dataSet = new BarDataSet(entries, "Number of Ratings");
                    dataSet.setColors(Color.rgb(75, 192, 192));
                    dataSet.setValueTextColor(Color.WHITE);
                    dataSet.setValueTextSize(14f);

                    BarData barData = new BarData(dataSet);
                    reviewChart.setData(barData);

                    // Customize chart
                    reviewChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    reviewChart.getXAxis().setGranularity(1f);
                    reviewChart.getXAxis().setLabelCount(5);
                    reviewChart.getAxisLeft().setAxisMinimum(0f);
                    reviewChart.getAxisRight().setEnabled(false);
                    reviewChart.getDescription().setEnabled(false);

                    reviewChart.getXAxis().setTextColor(Color.WHITE);

                    reviewChart.getAxisLeft().setTextColor(Color.WHITE);
                    reviewChart.getAxisRight().setTextColor(Color.WHITE);

                    reviewChart.getLegend().setTextColor(Color.WHITE);

                    reviewChart.invalidate();
                }
            }

            @Override
            public void onFailure(Call<EventReport> call, Throwable t) {
                // Handle failure
            }
        });
    }
}
