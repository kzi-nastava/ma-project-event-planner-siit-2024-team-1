package com.example.EventPlanner.fragments.eventtype;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.EventTypeForm;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.event.EventTypeOverview;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventTypeCard extends Fragment {

    // Parameter arguments
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // Parameters
    private String mParam1;
    private String mParam2;


    public EventTypeCard() {
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
    public static EventTypeCard newInstance(String param1, String param2) {
        EventTypeCard fragment = new EventTypeCard();
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
        return inflater.inflate(R.layout.fragment_event_type_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize buttons
        Button editEventTypeButton = view.findViewById(R.id.edit_event_type);
        Button deleteEventTypeButton = view.findViewById(R.id.delete_event_type);
        // Set listeners for buttons
        editEventTypeButton.setOnClickListener(v -> {
            // Handle the "Edit" button click
            // You can navigate to an edit screen or show an edit dialog
            Intent intent = new Intent(getActivity(), EventTypeForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("EVENT_TYPE_ID", 1);
            startActivity(intent);
        });

        deleteEventTypeButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), EventTypeForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("EVENT_TYPE_ID", 1);
            int eventTypeId = intent.getIntExtra("EVENT_TYPE_ID", -1);
            Call<EventTypeOverview> call1 = ClientUtils.eventTypeService.deactivate(eventTypeId);
            call1.enqueue(new Callback<EventTypeOverview>() {
                @Override
                public void onResponse(Call<EventTypeOverview> call, Response<EventTypeOverview> response) {
                    if (response.isSuccessful() && response.body() != null) {

                    } else {
                        // Handle error cases
                        Log.e("Login Error", "Response not successful: " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<EventTypeOverview> call, Throwable throwable) {
                    // Handle network errors
                    Log.e("Deactivating Failure", "Error: " + throwable.getMessage());
                }
            });
        });

    }
}
