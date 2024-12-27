package com.example.EventPlanner.fragments.event;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.adapters.event.EventsAdapter;
import com.example.EventPlanner.databinding.FragmentEventListBinding;
import com.example.EventPlanner.model.event.Event;

import java.util.ArrayList;

public class EventList extends Fragment {

    private FragmentEventListBinding eventListBinding;
    private EventViewModel eventViewModel;

    public EventList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        eventListBinding = FragmentEventListBinding.inflate(getLayoutInflater());
        eventViewModel = new ViewModelProvider(requireActivity()).get(EventViewModel.class);

        ArrayList<Event> allEvents = eventViewModel.events;

        EventsAdapter adapter = new EventsAdapter(requireContext(), allEvents);
        RecyclerView recyclerView = eventListBinding.eventsRecyclerViewHorizontal;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return eventListBinding.getRoot();
    }
}

