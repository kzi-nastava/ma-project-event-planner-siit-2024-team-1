package com.example.EventPlanner.fragments.user;

import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.user.UserOverviewAdapter;
import com.example.EventPlanner.adapters.user.UserReportAdapter;
import com.example.EventPlanner.databinding.FragmentUserOverviewListBinding;
import com.example.EventPlanner.databinding.FragmentUserReportListBinding;
import com.example.EventPlanner.fragments.userreport.UserReportViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserOverviewList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserOverviewList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentUserOverviewListBinding fragmentUserOverviewListBinding;
    private UserOverviewViewModel userOverviewViewModel;

    public UserOverviewList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserOverviewList.
     */
    // TODO: Rename and change types and number of parameters
    public static UserOverviewList newInstance(String param1, String param2) {
        UserOverviewList fragment = new UserOverviewList();
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
    public void onResume() {
        super.onResume();
        userOverviewViewModel.getChatUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentUserOverviewListBinding= FragmentUserOverviewListBinding.inflate(getLayoutInflater());
        RecyclerView userOverviewRecyclerView=fragmentUserOverviewListBinding.userOverviewRecyclerView;
        userOverviewRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        userOverviewViewModel=new ViewModelProvider(requireActivity()).get(UserOverviewViewModel.class);
        userOverviewViewModel.getUsers().observe(getViewLifecycleOwner(), users->{
            UserOverviewAdapter userAdapter = new UserOverviewAdapter(requireActivity(), users);
            userOverviewRecyclerView.setAdapter(userAdapter);
            userAdapter.notifyDataSetChanged();

        });
        userOverviewViewModel.getChatUsers();

        return fragmentUserOverviewListBinding.getRoot();
    }
}