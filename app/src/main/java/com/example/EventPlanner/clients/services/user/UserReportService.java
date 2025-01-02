package com.example.EventPlanner.clients.services.user;

import com.example.EventPlanner.model.user.UserReportOverview;
import com.example.EventPlanner.model.user.UserReportResponse;
import com.example.EventPlanner.model.user.UserSuspension;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserReportService {
    @GET("user-reports")
    Call<List<UserReportOverview>> getPendingReports();

    @POST("user-reports/{reportId}/approve")
    Call<UserSuspension> approveReport(@Path("reportId")int reportId);

    @POST("user-reports/{reportId}/deny")
    Call<UserReportResponse> denyReport(@Path("reportId") int reportId);
}
