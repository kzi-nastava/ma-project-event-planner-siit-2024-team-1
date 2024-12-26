package com.example.EventPlanner.event;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentEventCrudBinding;
import com.example.EventPlanner.databinding.FragmentEventTypeCrudBinding;
import com.example.EventPlanner.databinding.FragmentProductCrudBinding;
import com.example.EventPlanner.eventType.EventTypeForm;
import com.example.EventPlanner.eventType.EventTypeList;
import com.example.EventPlanner.product.ProductForm;
import com.example.EventPlanner.product.ProductList;
import com.example.EventPlanner.user.EventOrganizer;

public class EventCRUD extends Fragment {

    private FragmentEventCrudBinding eventCrudBinding;

    public EventCRUD() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventCrudBinding = FragmentEventCrudBinding.inflate(getLayoutInflater());
        View view = eventCrudBinding.getRoot();

        // Initialize Product List Fragment
        EventList eventList = new EventList();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewEvent, eventList).commit();

        // Add new product button listener
        Button addEventButton = eventCrudBinding.addEventButton;
        addEventButton.setOnClickListener((v) -> {
            Intent intent = new Intent(requireActivity(), EventForm.class);
            intent.putExtra("FORM_TYPE", "NEW_FORM");
            startActivity(intent);
        });

        return view;
    }
}

