package com.example.EventPlanner.fragments.event;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.EventPlanner.adapters.event.EventOverviewAdapter;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.fragments.common.map.MapViewModel;
import com.example.EventPlanner.fragments.eventmerchandise.DotsIndicatorDecoration;
import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentEventListBinding;
import com.example.EventPlanner.fragments.eventmerchandise.SearchViewModel;
import com.example.EventPlanner.model.event.EventOverview;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private MapViewModel mapViewModel;

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
    MaterialCalendarView calendarView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventsListBinding= FragmentEventListBinding.inflate(getLayoutInflater());
        eventListViewModel =new ViewModelProvider(requireActivity()).get(EventListViewModel.class);
        searchViewModel=new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mapViewModel=new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        String topString=getString(R.string.top);
        String extraValue; // Default value
        if (getArguments() != null) {
            extraValue = getArguments().getString("type", topString);
        } else {
            extraValue = topString;
        }
        TextView eventsHeader=eventsListBinding.eventsHeader;

        RecyclerView recyclerView = eventsListBinding.eventsRecyclerViewHorizontal;
        calendarView = eventsListBinding.calendarView;
        calendarView.setDateTextAppearance(R.color.white);
        eventListViewModel.getEvents().observe(getViewLifecycleOwner(), events->{
            EventOverviewAdapter eventsAdapter = new EventOverviewAdapter(requireActivity(), events, extraValue);
            recyclerView.setAdapter(eventsAdapter);
            mapViewModel.setEvents(events);

            calendarView.removeDecorators();
            List<CalendarDay> eventDates = new ArrayList<>();
            for (EventOverview event : events) {
                // Convert your event dates to CalendarDay objects
                DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

                // Parse the date string
                LocalDateTime date = LocalDateTime.parse(event.getDate(), formatter);

                // Extract year, month, and day
                int year = date.getYear();
                int month = date.getMonthValue() - 1;
                int day = date.getDayOfMonth();
                CalendarDay calDate = CalendarDay.from(year, month, day);
                eventDates.add(calDate);
            }

            // Decorate the calendar with event markers
            calendarView.addDecorator(new EventDecorator(
                    ContextCompat.getColor(requireContext(), R.color.accent_color),
                    eventDates
            ));

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
                calendarView.setVisibility(View.GONE);
                break;
            case "all":
            case "All":
                eventsHeader.setText(R.string.all_events);
                eventListViewModel.search(Boolean.TRUE.equals(searchViewModel.getShowEvents().getValue()),searchViewModel.getSearchText().getValue(),searchViewModel.getStartDate().getValue(),searchViewModel.getEndDate().getValue(),
                        searchViewModel.getType().getValue(),searchViewModel.getCity().getValue(),searchViewModel.getEventSortBy().getValue());
                calendarView.setVisibility(View.GONE);
                break;
            case "my":
            case "My":
                eventsHeader.setText(R.string.my_events);
                eventListViewModel.getByEo();
                break;
            case "fav":
            case "Fav":
                eventsHeader.setText(R.string.favorite_events);
                eventListViewModel.getFavorites();
                break;
            case "Flw":
            case "flw":
                eventsHeader.setText(R.string.followed_events);
                eventListViewModel.getFollowed();
                break;
        }


        return eventsListBinding.getRoot();
    }

}