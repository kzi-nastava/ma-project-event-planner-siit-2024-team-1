package com.example.EventPlanner.fragments.merchandise.product;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.event.EventTypesAdapter;
import com.example.EventPlanner.activities.ProductForm;

import java.util.ArrayList;
import java.util.List;

public class ProductCard extends Fragment {

    // Parameter arguments
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Parameters
    private String mParam1;
    private String mParam2;

    private ViewPager2 photoSlider;
    private RecyclerView eventTypesList;

    public ProductCard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductCard.
     */
    public static ProductCard newInstance(String param1, String param2) {
        ProductCard fragment = new ProductCard();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_product_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize UI elements
        photoSlider = view.findViewById(R.id.photo_slider);

        // Initialize buttons
        Button editProductButton = view.findViewById(R.id.edit_product);
        Button deleteProductButton = view.findViewById(R.id.delete_product);


        // Set listeners for buttons
        editProductButton.setOnClickListener(v -> {
            // Handle the "Edit" button click
            // You can navigate to an edit screen or show an edit dialog
            Intent intent = new Intent(getActivity(), ProductForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("PRODUCT_ID", 1);
            startActivity(intent);
        });

        deleteProductButton.setOnClickListener(v -> {
            // Handle the "Delete" button click
            // You can show a confirmation dialog before deleting
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete this product?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Perform delete operation here, e.g., remove product from database
                        Toast.makeText(getContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Initialize buttons
        Button showProductButton = view.findViewById(R.id.show_product);
        Button availProductButton = view.findViewById(R.id.avail_product);


        // Set listeners for buttons
        showProductButton.setOnClickListener(v -> {
            // Handle the "Delete" button click
            // You can show a confirmation dialog before deleting
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete this product?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Perform delete operation here, e.g., remove product from database
                        Toast.makeText(getContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        availProductButton.setOnClickListener(v -> {
            // Handle the "Delete" button click
            // You can show a confirmation dialog before deleting
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Product")
                    .setMessage("Are you sure you want to delete this product?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Perform delete operation here, e.g., remove product from database
                        Toast.makeText(getContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Setup event types list
        setupEventTypesList();

    }

    private void setupEventTypesList() {
        // Dummy data for event types
        List<String> eventTypes = new ArrayList<>();
        eventTypes.add("Wedding");
        eventTypes.add("Corporate Event");
        eventTypes.add("Birthday Party");

        // Setup RecyclerView
        eventTypesList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        EventTypesAdapter adapter = new EventTypesAdapter(eventTypes);
        eventTypesList.setAdapter(adapter);
    }
}
