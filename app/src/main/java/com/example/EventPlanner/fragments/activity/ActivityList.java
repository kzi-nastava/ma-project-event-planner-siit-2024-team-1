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
import com.example.EventPlanner.adapters.event.EventTypeAdapter;
import com.example.EventPlanner.databinding.FragmentActivityListBinding;
import com.example.EventPlanner.model.event.Activity;

import java.util.ArrayList;

public class ActivityList extends Fragment {

    private FragmentActivityListBinding activityListBinding;
    private ActivityViewModel activityViewModel;

    private int eventId;

    public ActivityList(int eventId) {
        // Required empty public constructor
        this.eventId = eventId;
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
