package com.example.EventPlanner.fragments.event;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.EventPlanner.adapters.event.EventOverviewAdapter;
import com.example.EventPlanner.fragments.eventmerchandise.DotsIndicatorDecoration;
import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentEventListBinding;
import com.example.EventPlanner.fragments.eventmerchandise.SearchViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EventListViewModel eventListViewModel;
    private SearchViewModel searchViewModel;

    private FragmentEventListBinding eventsListBinding;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventsList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventList.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsList newInstance(String param1, String param2) {
        EventsList fragment = new EventsList();
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
        eventsListBinding= FragmentEventListBinding.inflate(getLayoutInflater());
        eventListViewModel =new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
        searchViewModel=new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        String topString=getString(R.string.top);
        String extraValue = topString; // Default value
        if (getArguments() != null) {
            extraValue = getArguments().getString("type", topString);
        }
        TextView eventsHeader=eventsListBinding.eventsHeader;
        RecyclerView recyclerView = eventsListBinding.eventsRecyclerViewHorizontal;
        eventListViewModel.getEvents().observe(getViewLifecycleOwner(), events->{
            EventOverviewAdapter eventsAdapter = new EventOverviewAdapter(requireActivity(), events);
            recyclerView.setAdapter(eventsAdapter);
            eventsAdapter.notifyDataSetChanged();

        });
        recyclerView.addItemDecoration(new DotsIndicatorDecoration(
                ContextCompat.getColor(getContext(), R.color.accent_color),
                ContextCompat.getColor(getContext(), R.color.primary_color)
        ));
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        switch (extraValue){
            case "top":
            case "Top":
                eventsHeader.setText(R.string.top_events);
                eventListViewModel.getTop();
                break;
            case "all":
            case "All":
                eventsHeader.setText(R.string.all_events);
                eventListViewModel.search(Boolean.TRUE.equals(searchViewModel.getShowEvents().getValue()),searchViewModel.getSearchText().getValue(),searchViewModel.getStartDate().getValue(),searchViewModel.getEndDate().getValue(),
                        searchViewModel.getType().getValue(),searchViewModel.getCity().getValue(),searchViewModel.getEventSortBy().getValue());
                break;
            case "my":
            case "My":
                eventsHeader.setText(R.string.my_events);
                eventListViewModel.getByEo();
                break;

        }


        return eventsListBinding.getRoot();
    }
}