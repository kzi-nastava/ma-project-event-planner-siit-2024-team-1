package com.example.zadatak2;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.zadatak2.databinding.ActivityServiceFormBinding;
import com.example.zadatak2.databinding.FragmentServiceCrudBinding;

import java.lang.reflect.Array;
import java.util.Arrays;

public class ServiceForm extends AppCompatActivity {
    private ActivityServiceFormBinding serviceFormBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        serviceFormBinding = ActivityServiceFormBinding.inflate(getLayoutInflater());

        EdgeToEdge.enable(this);
        setContentView(serviceFormBinding.getRoot());

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = serviceFormBinding.formTitle;
        if(formType.equals("NEW_FORM")) {
            formTitle.setText("New Service");
            serviceFormBinding.serviceVisibleCheckbox.setVisibility(View.GONE);
            serviceFormBinding.serviceAvailableCheckbox.setVisibility(View.GONE);
        }else if(formType.equals("EDIT_FORM")) {
            formTitle.setText("Edit Service");
            serviceFormBinding.serviceVisibleCheckbox.setVisibility(View.VISIBLE);
            serviceFormBinding.serviceAvailableCheckbox.setVisibility(View.VISIBLE);
            serviceFormBinding.categorySpinner.setVisibility(View.GONE);
            serviceFormBinding.categoryChangeText.setVisibility(View.VISIBLE);
        }

        //adding items to category spinner
        EditText categoryEditText = serviceFormBinding.categoryInput;
        Spinner categorySpinner = serviceFormBinding.categorySpinner;
        String[] categoryOptions = {"options", "space", "food", "drinks", "music", "decorations", "other"};
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categoryOptions);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);
        //adding items to event type spinner
        Spinner eventTypeSpinner = serviceFormBinding.eventTypeSpinner;
        String[] eventTypeOptions = {"options", "wedding", "funeral", "birthday", "conference"};
        ArrayAdapter<String> eventTypeAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, eventTypeOptions);
        eventTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(eventTypeAdapter);


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedOption = categoryOptions[position];
                if (selectedOption.equals("other")) {
                    categorySpinner.setVisibility(View.GONE);
                    categoryEditText.setVisibility(View.VISIBLE);
                } else {
                    categoryEditText.setVisibility(View.GONE);
                    categorySpinner.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}