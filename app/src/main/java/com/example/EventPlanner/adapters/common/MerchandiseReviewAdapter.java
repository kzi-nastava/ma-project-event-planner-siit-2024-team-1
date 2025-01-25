package com.example.EventPlanner.adapters.common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.model.merchandise.DetailsReviewOverview;

import java.util.List;


public class MerchandiseReviewAdapter extends RecyclerView.Adapter<MerchandiseReviewAdapter.MerchandiseReviewHolder> {
    private List<DetailsReviewOverview> merchandiseReviews;
    private Context context;

    public MerchandiseReviewAdapter(Context context, List<DetailsReviewOverview> merchandiseReview) {
        this.context = context;
        this.merchandiseReviews = merchandiseReview;
    }

    @NonNull
    @Override
    public MerchandiseReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_merchandise_review_card, parent, false);
        return new MerchandiseReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MerchandiseReviewHolder holder, int position) {
        DetailsReviewOverview review = merchandiseReviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return merchandiseReviews.size();
    }

    static class MerchandiseReviewHolder extends RecyclerView.ViewHolder {
        LinearLayout merchandiseReviewCard;
        TextView reviewerName;
        RatingBar reviewRating;
        TextView reviewComment;

        public MerchandiseReviewHolder(@NonNull View itemView) {
            super(itemView);
            merchandiseReviewCard = itemView.findViewById(R.id.merchandise_review_card);
            reviewerName = itemView.findViewById(R.id.reviewer_name);
            reviewRating = itemView.findViewById(R.id.review_rating);
            reviewComment = itemView.findViewById(R.id.review_comment);
        }

        public void bind(DetailsReviewOverview review) {
            reviewerName.setText(review.getReviewersUsername());
            reviewRating.setRating(review.getRating());
            reviewComment.setText(review.getComment());
        }
    }
}
