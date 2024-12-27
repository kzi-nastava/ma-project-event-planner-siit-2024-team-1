package com.example.EventPlanner.fragments.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.adapters.activity.ActivityAdapter;
import com.example.EventPlanner.adapters.event.EventsAdapter;
import com.example.EventPlanner.databinding.FragmentActivityListBinding;
import com.example.EventPlanner.databinding.FragmentEventListBinding;
import com.example.EventPlanner.fragments.event.EventViewModel;
import com.example.EventPlanner.model.event.Activity;
import com.example.EventPlanner.model.event.Event;

import java.util.ArrayList;

public class ActivityList extends Fragment {

    private FragmentActivityListBinding activityListBinding;
    private ActivityViewModel activityViewModel;

    public ActivityList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activityListBinding = FragmentActivityListBinding.inflate(getLayoutInflater());
        activityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);

        ArrayList<Activity> activities = activityViewModel.getAll();

        ActivityAdapter adapter = new ActivityAdapter(requireContext(), activities);
        RecyclerView recyclerView = activityListBinding.activitiesRecyclerViewHorizontal;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return activityListBinding.getRoot();
    }
}
