package com.example.EventPlanner.fragments.merchandise.category;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.CategoryForm;
import com.example.EventPlanner.databinding.FragmentCategoryCrudBinding;

public class CategoryCRUD extends Fragment {
    private FragmentCategoryCrudBinding categoryCrudBinding;

    public CategoryCRUD() {
        // Required empty public constructor
    }

    public static CategoryCRUD newInstance(String param1, String param2) {
        CategoryCRUD fragment = new CategoryCRUD();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        categoryCrudBinding = FragmentCategoryCrudBinding.inflate(getLayoutInflater());
        CategoryList approvedCategoryList = CategoryList.newInstance(CategoryList.CategoryType.APPROVED);
        CategoryList pendingCategoryList = CategoryList.newInstance(CategoryList.CategoryType.PENDING);

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewApprovedCategories, approvedCategoryList)
                .commit();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewPendingCategories, pendingCategoryList)
                .commit();

        ImageButton addCategoryButton = (ImageButton) categoryCrudBinding.addCategoryButton;
        addCategoryButton.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CategoryForm.class);
            intent.putExtra("FORM_TYPE", "NEW_FORM");
            startActivity(intent);
        });

        return categoryCrudBinding.getRoot();
    }
}