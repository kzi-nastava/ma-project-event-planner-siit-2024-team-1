package com.example.EventPlanner.fragments.eventmerchandise;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.adapters.event.EventsMerchandiseAdapter;
import com.example.EventPlanner.databinding.FragmentEventsMerchandiseListBinding;
import com.example.EventPlanner.model.event.EventMerchandise;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsMerchandiseList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsMerchandiseList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private EventMerchandiseViewModel eventMerchandiseViewModel;
    private FragmentEventsMerchandiseListBinding eventsMerchandiseListBinding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventsMerchandiseList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsMerchandiseList.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsMerchandiseList newInstance(String param1, String param2) {
        EventsMerchandiseList fragment = new EventsMerchandiseList();
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
        // Inflate the layout for this fragment
        eventsMerchandiseListBinding=FragmentEventsMerchandiseListBinding.inflate(getLayoutInflater());
        String extraValue = "top"; // Default value
        if (getArguments() != null) {
            extraValue = getArguments().getString("type", "top");
        }
        Log.i("nesto1", extraValue);
        eventMerchandiseViewModel=new ViewModelProvider(this).get(EventMerchandiseViewModel.class);
        List<EventMerchandise> eventMerchandiseList=new ArrayList<>();
        switch (extraValue) {
            case "top":
            case "Top":
                eventMerchandiseList = eventMerchandiseViewModel.getTop();
                break;
            case "all":
            case "All":
                requireActivity().setTitle("All");
                eventMerchandiseList = eventMerchandiseViewModel.getAll();
                break;
        }
        EventsMerchandiseAdapter adapter= new EventsMerchandiseAdapter(eventMerchandiseList);
        RecyclerView recyclerView = eventsMerchandiseListBinding.eventsMerchandiseRecyclerView;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return eventsMerchandiseListBinding.getRoot();
    }
}