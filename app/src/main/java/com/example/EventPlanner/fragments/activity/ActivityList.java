package com.example.EventPlanner.fragments.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.EventPlanner.activities.HomeScreen;
import com.example.EventPlanner.activities.LoginScreen;
import com.example.EventPlanner.adapters.activity.ActivityAdapter;
import com.example.EventPlanner.adapters.event.EventTypeAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.FragmentActivityListBinding;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.common.ErrorResponseDto;
import com.example.EventPlanner.model.event.Activity;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class ActivityList extends Fragment {

    private FragmentActivityListBinding activityListBinding;
    private ActivityViewModel activityViewModel;

    private int eventId;

    private String eventTitle = "";

    public ActivityList(int eventId) {
        // Required empty public constructor
        this.eventId = eventId;
    }

    public void generatePdf(int eventId) {
        activityViewModel.getActivities().observe(getViewLifecycleOwner(), activities -> {
            if (activities == null || activities.isEmpty()) {
                Log.e("PDF Generation", "No activities available for the event.");
                return;
            }

            Call<CreatedEventResponse> call1 = ClientUtils.eventService.getById(eventId);
            call1.enqueue(new Callback<CreatedEventResponse>() {
                @Override
                public void onResponse(Call<CreatedEventResponse> call, Response<CreatedEventResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        eventTitle = response.body().getTitle();
                    }
                }

                @Override
                public void onFailure(Call<CreatedEventResponse> call, Throwable throwable) {
                    // Handle network errors
                    Toast.makeText(getContext(), "Network error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            // Path to save the PDF
            String downloadsPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            String fileName = eventTitle.replaceAll("[^a-zA-Z0-9]", "_") + "_Activities.pdf";
            File pdfFile = new File(downloadsPath, fileName);

            try {
                // Create PdfWriter and PdfDocument
                PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                // Add title
                document.add(new Paragraph("Activities for Event: " + eventTitle).setBold().setFontSize(18));
                document.add(new Paragraph("\n"));

                // Create table with headers
                float[] columnWidths = {2, 3, 4, 2, 2}; // Column widths for Title, Address, Description, Start, End
                Table table = new Table(columnWidths);
                table.addHeaderCell("Title");
                table.addHeaderCell("Address");
                table.addHeaderCell("Description");
                table.addHeaderCell("Start");
                table.addHeaderCell("End");

                // Populate table with data
                for (Activity activity : activities) {
                    table.addCell(activity.getTitle());
                    table.addCell(activity.getAddress().getCity() + ", " +
                            activity.getAddress().getStreet() + ", " +
                            activity.getAddress().getNumber());
                    table.addCell(activity.getDescription());
                    table.addCell(activity.getStartTime());
                    table.addCell(activity.getEndTime());
                }

                // Add table to the document
                document.add(table);

                // Close the document
                document.close();
                Log.i("PDF Generation", "PDF created at: " + pdfFile.getAbsolutePath());

                // Optionally, open the PDF after creation (use the `openPdf` method provided earlier)
                openPdf(pdfFile.getAbsolutePath());

            } catch (Exception e) {
                Log.e("PDF Generation", "Error generating PDF: " + e.getMessage());
            }
        });

        // Fetch activities for the event
        activityViewModel.getAll(eventId);
    }

    private void openPdf(String filePath) {
        File pdfFile = new File(filePath);
        Uri pdfUri = FileProvider.getUriForFile(requireContext(), "com.example.EventPlanner.fileprovider", pdfFile);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(pdfUri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(intent, "Open PDF"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activityListBinding = FragmentActivityListBinding.inflate(getLayoutInflater());
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        RecyclerView recyclerView = activityListBinding.activitiesRecyclerViewHorizontal;
        activityViewModel.getActivities().observe(getViewLifecycleOwner(),activities->{
            ActivityAdapter activityAdapter = new ActivityAdapter(requireActivity(), activities, eventId);
            recyclerView.setAdapter(activityAdapter);
            activityAdapter.notifyDataSetChanged();

        });

        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        activityViewModel.getAll(eventId);

        return activityListBinding.getRoot();
    }
}
