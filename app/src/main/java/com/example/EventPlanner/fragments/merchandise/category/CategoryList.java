package com.example.EventPlanner.fragments.merchandise.category;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.merchandise.category.CategoryAdapter;
import com.example.EventPlanner.databinding.FragmentCategoryListBinding;
import com.example.EventPlanner.fragments.merchandise.service.ServiceViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CategoryList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryList extends Fragment {

    private static final String ARG_CATEGORY_TYPE = "category_type";

    public enum CategoryType {
        APPROVED,
        PENDING
    }
    private String mParam1;
    private String mParam2;

    private FragmentCategoryListBinding categoryListBinding;
    private CategoryViewModel categoryViewModel;


    public CategoryList() {
        // Required empty public constructor
    }

    public static CategoryList newInstance(CategoryType categoryType) {
        CategoryList fragment = new CategoryList();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_TYPE, categoryType.name());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String categoryType = getArguments().getString(ARG_CATEGORY_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categoryListBinding = FragmentCategoryListBinding.inflate(getLayoutInflater());
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        RecyclerView recyclerView = categoryListBinding.categoryListVertical;

        if(getArguments() != null) {
            String categoryTypeStr = getArguments().getString(ARG_CATEGORY_TYPE);
            CategoryType categoryType = CategoryType.valueOf(categoryTypeStr);

            categoryViewModel.getAllCategoriesByType(categoryType).observe(getViewLifecycleOwner(), categories -> {
                CategoryAdapter adapter = new CategoryAdapter(requireContext(), categories, categoryType);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });

            categoryViewModel.getApprovedCategories();
            categoryViewModel.getPendingCategories();
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }

        return categoryListBinding.getRoot();
    }
}