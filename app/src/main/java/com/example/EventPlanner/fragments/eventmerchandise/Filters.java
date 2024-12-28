package com.example.EventPlanner.fragments.eventmerchandise;

import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.EventPlanner.databinding.FragmentFiltersBinding;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Filters#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Filters extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentFiltersBinding filtersBinding;
    private SearchViewModel searchViewModel;
    private String mParam1;
    private String mParam2;

    public Filters() {
        // Required empty public constructor
    }

    public static Filters newInstance(String param1, String param2) {
        Filters fragment = new Filters();
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
        filtersBinding = FragmentFiltersBinding.inflate(getLayoutInflater());
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);

        setupCheckBoxListeners();
        setupCalendarListeners();
        setupTextInputListeners();
        setupButtons();
        populateFieldsFromViewModel();

        return filtersBinding.getRoot();
    }

    private void populateFieldsFromViewModel() {
        // Populate checkboxes
        filtersBinding.cbShowEvents.setChecked(searchViewModel.getShowEvents().getValue() != null ?
                searchViewModel.getShowEvents().getValue() : true);
        filtersBinding.cbShowServices.setChecked(searchViewModel.getShowServices().getValue() != null ?
                searchViewModel.getShowServices().getValue() : true);
        filtersBinding.cbShowProducts.setChecked(searchViewModel.getShowProducts().getValue() != null ?
                searchViewModel.getShowProducts().getValue() : true);

        // Populate text fields
        populateEditText(filtersBinding.eventTypeInput, searchViewModel.getType().getValue());
        populateEditText(filtersBinding.eventCityInput, searchViewModel.getCity().getValue());

        // Populate service fields
        populateEditText(filtersBinding.servicePriceMin, toString(searchViewModel.getServicePriceMin().getValue()));
        populateEditText(filtersBinding.servicePriceMax, toString(searchViewModel.getServicePriceMax().getValue()));
        populateEditText(filtersBinding.serviceCategory, searchViewModel.getServiceCategory().getValue());
        populateEditText(filtersBinding.serviceDurationMin, toString(searchViewModel.getServiceDurationMin().getValue()));
        populateEditText(filtersBinding.serviceDurationMax, toString(searchViewModel.getServiceDurationMax().getValue()));
        populateEditText(filtersBinding.serviceCity, searchViewModel.getServiceCity().getValue());

        // Populate product fields
        populateEditText(filtersBinding.productPriceMin, toString(searchViewModel.getProductPriceMin().getValue()));
        populateEditText(filtersBinding.productPriceMax, toString(searchViewModel.getProductPriceMax().getValue()));
        populateEditText(filtersBinding.productCategory, searchViewModel.getProductCategory().getValue());
        populateEditText(filtersBinding.productDurationMin, toString(searchViewModel.getProductDurationMin().getValue()));
        populateEditText(filtersBinding.productDurationMax, toString(searchViewModel.getProductDurationMax().getValue()));
        populateEditText(filtersBinding.productCity, searchViewModel.getProductCity().getValue());

        // Populate calendar dates if they exist
        LocalDate startDate = searchViewModel.getStartDate().getValue();
        LocalDate endDate = searchViewModel.getEndDate().getValue();

        if (startDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(startDate.getYear(), startDate.getMonthValue() - 1, startDate.getDayOfMonth());
            filtersBinding.eventDatePickerStart.setDate(calendar.getTimeInMillis());
        }

        if (endDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(endDate.getYear(), endDate.getMonthValue() - 1, endDate.getDayOfMonth());
            filtersBinding.eventDatePickerEnd.setDate(calendar.getTimeInMillis());
        }
    }

    private void setupButtons() {
        filtersBinding.btnApplyFilters.setOnClickListener(v -> sendFilterResultToParent());

        // Add reset button functionality
        filtersBinding.btnResetFilters.setOnClickListener(v -> resetFilters());
    }

    private void resetFilters() {
        searchViewModel.resetSearch();

        // Repopulate UI with reset values
        populateFieldsFromViewModel();
        sendFilterResultToParent();
    }

    private void populateEditText(android.widget.EditText editText, String value) {
        if (value != null) {
            editText.setText(value);
        } else {
            editText.setText("");
        }
    }

    private String toString(Object value) {
        return value != null ? String.valueOf(value) : null;
    }

    private void setupCheckBoxListeners() {
        filtersBinding.cbShowEvents.setOnCheckedChangeListener((buttonView, isChecked) -> {
            searchViewModel.setShowEvents(isChecked);
        });

        filtersBinding.cbShowServices.setOnCheckedChangeListener((buttonView, isChecked) -> {
            searchViewModel.setShowServices(isChecked);
        });

        filtersBinding.cbShowProducts.setOnCheckedChangeListener((buttonView, isChecked) -> {
            searchViewModel.setShowProducts(isChecked);
        });
    }

    private void setupCalendarListeners() {
        filtersBinding.eventDatePickerStart.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            searchViewModel.setStartDate(calendar.getTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        });

        filtersBinding.eventDatePickerEnd.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, dayOfMonth);
            searchViewModel.setEndDate(calendar.getTime().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate());
        });
    }

    private void setupTextInputListeners() {
        // Event filters
        filtersBinding.eventTypeInput.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setType(text)));
        filtersBinding.eventCityInput.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setCity(text)));

        filtersBinding.servicePriceMin.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setServicePriceMin(parseDouble(text))));
        filtersBinding.servicePriceMax.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setServicePriceMax(parseDouble(text))));
        filtersBinding.serviceCategory.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setServiceCategory(text)));
        filtersBinding.serviceDurationMin.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setServiceDurationMin(parseInt(text))));
        filtersBinding.serviceDurationMax.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setServiceDurationMax(parseInt(text))));
        filtersBinding.serviceCity.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setServiceCity(text)));

        // Product filters
        filtersBinding.productPriceMin.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setProductPriceMin(parseDouble(text))));
        filtersBinding.productPriceMax.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setProductPriceMax(parseDouble(text))));
        filtersBinding.productCategory.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setProductCategory(text)));
        filtersBinding.productDurationMin.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setProductDurationMin(parseInt(text))));
        filtersBinding.productDurationMax.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setProductDurationMax(parseInt(text))));
        filtersBinding.productCity.addTextChangedListener(new SimpleTextWatcher(text ->
                searchViewModel.setProductCity(text)));
    }

    private void setupApplyButton() {
        filtersBinding.btnApplyFilters.setOnClickListener(v -> sendFilterResultToParent());
    }

    // Helper class to simplify TextWatcher implementation
    private static class SimpleTextWatcher implements TextWatcher {
        private final Consumer<String> onTextChanged;

        SimpleTextWatcher(Consumer<String> onTextChanged) {
            this.onTextChanged = onTextChanged;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            onTextChanged.accept(s.toString());
        }
    }

    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private void sendFilterResultToParent() {
        Bundle result = new Bundle();
        result.putString("filter_result", "data");
        getParentFragmentManager().setFragmentResult("applyFilters", result);
    }
}