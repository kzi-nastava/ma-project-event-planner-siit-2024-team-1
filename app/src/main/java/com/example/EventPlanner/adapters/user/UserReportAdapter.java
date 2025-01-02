package com.example.EventPlanner.adapters.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.fragments.userreport.UserReportViewModel;
import com.example.EventPlanner.model.common.ErrorResponseDto;
import com.example.EventPlanner.model.user.UserReportOverview;
import com.example.EventPlanner.model.user.UserReportResponse;
import com.example.EventPlanner.model.user.UserSuspension;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class UserReportAdapter extends RecyclerView.Adapter<UserReportAdapter.ViewHolder> {
    private List<UserReportOverview> reports;
    private UserReportViewModel userReportListViewModel;
    private Context context;
    public UserReportAdapter(Context context, List<UserReportOverview> reports, UserReportViewModel viewModel) {
        this.context=context;
        this.reports = reports;
        userReportListViewModel =viewModel;

    }

    @Override
    public UserReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_report_card, parent, false);
        return new UserReportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserReportAdapter.ViewHolder holder, int position) {
        UserReportOverview report = reports.get(position);

        holder.reportedUser.setText("Reported User: "+report.getReportedUserName());
        holder.reportedUserEmail.setText("Reported email: "+report.getReportedUserEmail());
        holder.reporterEmail.setText("Reporter: "+report.getReporterEmail());
        holder.reason.setText("Reason: "+report.getReason());
        holder.approve.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Confirm Approval")
                    .setMessage("Are you sure you want to approve this report?")
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call your approve function here
                        approveReport(report);
                    })
                    .show();
        });

        holder.delete.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Confirm Denial")
                    .setMessage("Are you sure you want to deny this report?")
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call your deny function here
                        deleteReport(report);
                    })
                    .show();
        });

    }

    private void approveReport(UserReportOverview reportOverview){
        ClientUtils.userReportService.approveReport(reportOverview.getId()).enqueue(new Callback<UserSuspension>() {
            @Override
            public void onResponse(Call<UserSuspension> call, Response<UserSuspension> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context,
                            "Report approved",
                            Toast.LENGTH_SHORT).show();
                    userReportListViewModel.getPendingReports();
                } else {
                    try {
                        Converter<ResponseBody, ErrorResponseDto> converter = ClientUtils.retrofit.responseBodyConverter(
                                ErrorResponseDto.class, new Annotation[0]);
                        ErrorResponseDto errorResponse = converter.convert(response.errorBody());

                        if (errorResponse != null) {
                            Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Unknown error occurred", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(context, "Error parsing server response", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserSuspension> call, Throwable t) {
                Toast.makeText(context,
                        "Network error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteReport(UserReportOverview reportOverview){
        ClientUtils.userReportService.denyReport(reportOverview.getId()).enqueue(new Callback<UserReportResponse>() {
            @Override
            public void onResponse(Call<UserReportResponse> call, Response<UserReportResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context,
                            "Report deleted",
                            Toast.LENGTH_SHORT).show();
                    userReportListViewModel.getPendingReports();
                } else {
                    try {
                        Converter<ResponseBody, ErrorResponseDto> converter = ClientUtils.retrofit.responseBodyConverter(
                                ErrorResponseDto.class, new Annotation[0]);
                        ErrorResponseDto errorResponse = converter.convert(response.errorBody());

                        if (errorResponse != null) {
                            Toast.makeText(context, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, "Unknown error occurred", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        Toast.makeText(context, "Error parsing server response", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserReportResponse> call, Throwable t) {
                Toast.makeText(context,
                        "Network error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reportedUser;
        TextView reportedUserEmail;
        TextView reporterEmail;
        TextView reason;
        Button approve;
        Button delete;

        ViewHolder(View view) {
            super(view);
            reportedUser = view.findViewById(R.id.report_reported_user);
            reportedUserEmail = view.findViewById(R.id.report_reported_user_email);
            reporterEmail = view.findViewById(R.id.report_reporter);
            reason = view.findViewById(R.id.report_reason);
            approve=view.findViewById(R.id.report_approve);
            delete=view.findViewById(R.id.report_delete);
        }
    }
}
