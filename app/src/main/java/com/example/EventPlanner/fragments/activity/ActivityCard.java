package com.example.EventPlanner.fragments.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.ActivityForm;
import com.example.EventPlanner.activities.EventForm;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link com.example.EventPlanner.fragments.event.EventCard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityCard extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ActivityCard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventCard.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityCard newInstance(String param1, String param2) {
        ActivityCard fragment = new ActivityCard();
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
        return inflater.inflate(R.layout.fragment_activity_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Initialize buttons
        Button editActivityButton = view.findViewById(R.id.edit_activity);
        Button deleteActivityButton = view.findViewById(R.id.delete_activity);

        // Set listeners for buttons
        editActivityButton.setOnClickListener(v -> {
            // Handle the "Edit" button click
            // You can navigate to an edit screen or show an edit dialog
            Intent intent = new Intent(getActivity(), ActivityForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("ACTIVITY_ID", 1);
            startActivity(intent);
        });

        deleteActivityButton.setOnClickListener(v -> {
            // Handle the "Delete" button click
            // You can show a confirmation dialog before deleting
            new AlertDialog.Builder(getContext())
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this Event Type?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Perform delete operation here, e.g., remove product from database
                        Toast.makeText(getContext(), "Activity deleted", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}
