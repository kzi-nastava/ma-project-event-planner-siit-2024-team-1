package com.example.EventPlanner.fragments.eventtype;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.adapters.event.EventTypeAdapter;
import com.example.EventPlanner.databinding.FragmentEventTypeListBinding;
import com.example.EventPlanner.model.event.EventType;

import java.util.ArrayList;

public class EventTypeList extends Fragment {

    private FragmentEventTypeListBinding eventTypeListBinding;
    private EventTypeViewModel eventTypeViewModel;

    public EventTypeList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        eventTypeListBinding = FragmentEventTypeListBinding.inflate(getLayoutInflater());
        eventTypeViewModel = new ViewModelProvider(requireActivity()).get(EventTypeViewModel.class);

        ArrayList<EventType> allEventTypes = eventTypeViewModel.getAll();

        EventTypeAdapter adapter = new EventTypeAdapter(requireContext(), allEventTypes);
        RecyclerView recyclerView = eventTypeListBinding.eventTypeListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return eventTypeListBinding.getRoot();
    }
}
