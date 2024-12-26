package com.example.EventPlanner.eventType;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentEventTypeCrudBinding;
import com.example.EventPlanner.databinding.FragmentProductCrudBinding;
import com.example.EventPlanner.product.ProductForm;
import com.example.EventPlanner.product.ProductList;
import com.example.EventPlanner.user.EventOrganizer;

public class EventTypeCRUD extends Fragment {

    private FragmentEventTypeCrudBinding eventTypeCrudBinding;

    public EventTypeCRUD() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        eventTypeCrudBinding = FragmentEventTypeCrudBinding.inflate(getLayoutInflater());
        View view = eventTypeCrudBinding.getRoot();

        // Initialize Product List Fragment
        EventTypeList eventTypeList = new EventTypeList();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewEventType, eventTypeList).commit();

        // Add new product button listener
        Button addEventTypeButton = eventTypeCrudBinding.addEventTypeButton;
        addEventTypeButton.setOnClickListener((v) -> {
            Intent intent = new Intent(requireActivity(), EventTypeForm.class);
            intent.putExtra("FORM_TYPE", "NEW_FORM");
            startActivity(intent);
        });

        return view;
    }
}

