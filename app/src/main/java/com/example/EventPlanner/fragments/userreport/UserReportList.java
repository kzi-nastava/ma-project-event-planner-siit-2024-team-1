package com.example.EventPlanner.fragments.userreport;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.common.ReviewOverviewAdapter;
import com.example.EventPlanner.adapters.user.UserReportAdapter;
import com.example.EventPlanner.databinding.FragmentReviewsListBinding;
import com.example.EventPlanner.databinding.FragmentUserReportListBinding;
import com.example.EventPlanner.fragments.common.ReviewsListViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserReportList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserReportList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentUserReportListBinding userReportListBinding;
    private UserReportViewModel userReportViewModel;

    public UserReportList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserReportList.
     */
    // TODO: Rename and change types and number of parameters
    public static UserReportList newInstance(String param1, String param2) {
        UserReportList fragment = new UserReportList();
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
        userReportListBinding= FragmentUserReportListBinding.inflate(getLayoutInflater());
        RecyclerView userReportRecyclerView=userReportListBinding.userReportRecyclerView;
        userReportRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userReportViewModel=new ViewModelProvider(requireActivity()).get(UserReportViewModel.class);
        userReportViewModel.getReports().observe(getViewLifecycleOwner(), reports->{
            UserReportAdapter reportAdapter = new UserReportAdapter(requireActivity(), reports,userReportViewModel);
            userReportRecyclerView.setAdapter(reportAdapter);
            reportAdapter.notifyDataSetChanged();

        });
        userReportViewModel.getPendingReports();

        return userReportListBinding.getRoot();
    }
}