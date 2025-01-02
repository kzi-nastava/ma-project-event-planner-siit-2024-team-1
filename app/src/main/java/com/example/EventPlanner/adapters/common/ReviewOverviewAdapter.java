package com.example.EventPlanner.adapters.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.BookReservationActivity;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.fragments.common.ReviewsListViewModel;
import com.example.EventPlanner.model.common.ErrorResponseDto;
import com.example.EventPlanner.model.common.ReviewOverview;
import com.example.EventPlanner.model.event.EventOverview;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;


public class ReviewOverviewAdapter extends RecyclerView.Adapter<ReviewOverviewAdapter.ViewHolder> {
    private List<ReviewOverview> reviews;
    private ReviewsListViewModel reviewsListViewModel;
    private Context context;
    public ReviewOverviewAdapter(Context context, List<ReviewOverview> reviews,ReviewsListViewModel viewModel) {
        this.context=context;
        this.reviews = reviews;
        reviewsListViewModel=viewModel;

    }

    @Override
    public ReviewOverviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_review_card, parent, false);
        return new ReviewOverviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewOverviewAdapter.ViewHolder holder, int position) {
        ReviewOverview review = reviews.get(position);

        holder.reviewer.setText("Reviewer: "+review.getReviewerUsername());
        holder.type.setText("Reviewed type: "+review.getReviewedType());
        holder.reviewedTitle.setText("Reviewd title: "+review.getReviewedTitle());
        holder.rating.setText("Rating: "+String.valueOf(review.getRating()));
        holder.comment.setText("Comment: "+review.getComment());
        if (review.getCreatedAt() != null) {
            // Format the date as per your requirement (e.g., "MM/dd/yyyy")
            String inputDate = review.getCreatedAt();

            // Parse the input string to LocalDateTime
            LocalDateTime dateTime = LocalDateTime.parse(inputDate);

            // Define the desired output format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

            // Format the date to the desired string
            String formattedDate = dateTime.format(formatter);
            holder.createdAt.setText(formattedDate);
        }
        holder.approve.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Confirm Approval")
                    .setMessage("Are you sure you want to approve this review?")
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call your approve function here
                        approveReview(review);
                    })
                    .show();
        });

        holder.deny.setOnClickListener(v -> {
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Confirm Denial")
                    .setMessage("Are you sure you want to deny this review?")
                    .setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Call your deny function here
                        denyReview(review);
                    })
                    .show();
        });

    }

    private void approveReview(ReviewOverview reviewOverview){
        ClientUtils.reviewService.approveReview(reviewOverview.getId()).enqueue(new Callback<ReviewOverview>() {
            @Override
            public void onResponse(Call<ReviewOverview> call, Response<ReviewOverview> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context,
                            "Review approved",
                            Toast.LENGTH_SHORT).show();
                    reviewsListViewModel.getPendingReviews();
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
            public void onFailure(Call<ReviewOverview> call, Throwable t) {
                Toast.makeText(context,
                        "Network error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void denyReview(ReviewOverview reviewOverview){
        ClientUtils.reviewService.denyReview(reviewOverview.getId()).enqueue(new Callback<ReviewOverview>() {
            @Override
            public void onResponse(Call<ReviewOverview> call, Response<ReviewOverview> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context,
                            "Review denied",
                            Toast.LENGTH_SHORT).show();
                    reviewsListViewModel.getPendingReviews();
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
            public void onFailure(Call<ReviewOverview> call, Throwable t) {
                Toast.makeText(context,
                        "Network error",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewer;
        TextView type;
        TextView reviewedTitle;
        TextView rating;
        TextView comment;
        TextView createdAt;
        Button approve;
        Button deny;

        ViewHolder(View view) {
            super(view);
            reviewer = view.findViewById(R.id.review_reviewer);
            type = view.findViewById(R.id.review_review_type);
            reviewedTitle = view.findViewById(R.id.review_reviewed_title);
            rating = view.findViewById(R.id.review_review_rating);
            comment = view.findViewById(R.id.review_comment);
            createdAt = view.findViewById(R.id.review_created_at);
            approve=view.findViewById(R.id.review_approve);
            deny=view.findViewById(R.id.review_deny);
        }
    }
}
