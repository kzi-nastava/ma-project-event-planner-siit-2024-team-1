package com.example.EventPlanner.fragments.common.review;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.common.MerchandiseReviewAdapter;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.FragmentMerchandiseReviewListBinding;
import com.example.EventPlanner.model.common.merchandiseReview.ReviewMerchandiseRequest;
import com.example.EventPlanner.model.common.merchandiseReview.ReviewType;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MerchandiseReviewList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MerchandiseReviewList extends Fragment {

    private FragmentMerchandiseReviewListBinding reviewListBinding;
    private MerchandiseReviewViewModel reviewViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MerchandiseReviewList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MerchandiseReviewList.
     */
    // TODO: Rename and change types and number of parameters
    public static MerchandiseReviewList newInstance(String param1, String param2) {
        MerchandiseReviewList fragment = new MerchandiseReviewList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reviewListBinding = FragmentMerchandiseReviewListBinding.inflate(getLayoutInflater());
        reviewViewModel = new ViewModelProvider(requireActivity()).get(MerchandiseReviewViewModel.class);
        RecyclerView recyclerView = reviewListBinding.reviewList;

        int id;
        ReviewType reviewType;
        if(getArguments() != null) {
            id = getArguments().getInt("id", -1);
            reviewType = ReviewType.valueOf(getArguments().getString("reviewType", "MERCHANDISE_REVIEW"));
        } else {
            reviewType = ReviewType.MERCHANDISE_REVIEW;
            id = -1;
        }

        reviewViewModel.getReviews().observe(getViewLifecycleOwner(), reviews -> {
            MerchandiseReviewAdapter adapter = new MerchandiseReviewAdapter(requireContext(), reviews);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });
        reviewViewModel.setReviews(id, reviewType);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        reviewViewModel.getIsEligibleForReview().observe(getViewLifecycleOwner(), isEligibleForReview -> {
            if(isEligibleForReview) {
                reviewListBinding.leaveReviewForm.setVisibility(View.VISIBLE);
            }else {
                reviewListBinding.leaveReviewForm.setVisibility(View.GONE);
            }
        });
        reviewViewModel.checkIfEligibleForReview(id, reviewType);

        reviewListBinding.leaveReviewBtn.setOnClickListener(v -> leaveReview(id, reviewType));

        reviewListBinding.reviewRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.e("Rating", "Changed");
            }
        });
        // Inflate the layout for this fragment
        return reviewListBinding.getRoot();
    }

    private void leaveReview(int id, ReviewType reviewType) {
        if(isFormValid()) {
            int rating = (int) reviewListBinding.reviewRating.getRating();
            String comment = reviewListBinding.reviewComment.getText().toString().trim();
            ReviewMerchandiseRequest dto = new ReviewMerchandiseRequest(JwtService.getIdFromToken(), comment, rating, reviewType.toString());

            reviewViewModel.leaveReview(id, dto);
        }else {
            Toast.makeText(requireContext(), "Form invalid", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isFormValid() {
        return !TextUtils.isEmpty(reviewListBinding.reviewComment.getText());
    }
}