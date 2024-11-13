package com.example.zadatak2.event;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.zadatak2.databinding.FragmentEventsListBinding;

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
    private EventViewModel eventViewModel;

    private FragmentEventsListBinding eventsListBinding;

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
        eventsListBinding= FragmentEventsListBinding.inflate(getLayoutInflater());
        eventViewModel =new ViewModelProvider(this).get(EventViewModel.class);
        EventsListAdapter mla = new EventsListAdapter(requireContext(), eventViewModel.getEvents());
        ListView eventListView = eventsListBinding.eventsList;
        eventListView.setAdapter(mla);

        return eventsListBinding.getRoot();
    }
}