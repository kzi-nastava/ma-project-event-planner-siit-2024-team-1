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

import java.time.ZoneId;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Filters#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Filters extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentFiltersBinding filtersBinding;
    private SearchViewModel searchViewModel;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Filters() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Filters.
     */
    // TODO: Rename and change types and number of parameters
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
        setupCheckBoxListeners();
        setupCalendarListeners();
        setupTextInputListeners();
        setupApplyButton();
        searchViewModel = new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        return filtersBinding.getRoot();
    }

    private void setupCheckBoxListeners() {
        filtersBinding.cbShowEvents.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //searchViewModel.setShowEvents(isChecked);
        });

        filtersBinding.cbShowServices.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //searchViewModel.setShowServices(isChecked);
        });

        filtersBinding.cbShowProducts.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            searchViewModel.setShowProducts(isChecked);
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

        // Service filters
//        filtersBinding.servicePriceMin.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setServicePriceMin(parseDouble(text))));
//        filtersBinding.servicePriceMax.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setServicePriceMax(parseDouble(text))));
//        filtersBinding.serviceCategory.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setServiceCategory(text)));
//        filtersBinding.serviceDurationMin.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setServiceDurationMin(parseInt(text))));
//        filtersBinding.serviceDurationMax.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setServiceDurationMax(parseInt(text))));
//        filtersBinding.serviceCity.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setServiceCity(text)));
//
//        // Product filters
//        filtersBinding.productPriceMin.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setProductPriceMin(parseDouble(text))));
//        filtersBinding.productPriceMax.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setProductPriceMax(parseDouble(text))));
//        filtersBinding.productCategory.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setProductCategory(text)));
//        filtersBinding.productDurationMin.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setProductDurationMin(parseInt(text))));
//        filtersBinding.productDurationMax.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setProductDurationMax(parseInt(text))));
//        filtersBinding.productCity.addTextChangedListener(new SimpleTextWatcher(text ->
//                searchViewModel.setProductCity(text)));
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