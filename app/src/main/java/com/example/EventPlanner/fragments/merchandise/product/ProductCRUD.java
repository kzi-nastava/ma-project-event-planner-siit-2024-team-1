package com.example.EventPlanner.fragments.merchandise.product;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentProductCrudBinding;
import com.example.EventPlanner.activities.ProductForm;

public class ProductCRUD extends Fragment {

    private FragmentProductCrudBinding productCrudBinding;

    public ProductCRUD() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        productCrudBinding = FragmentProductCrudBinding.inflate(getLayoutInflater());
        View view = productCrudBinding.getRoot();

        // Initialize Product List Fragment
        ProductList productList = new ProductList();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewProduct, productList).commit();

        // Add new product button listener
        Button addProductButton = productCrudBinding.addProductButton;
        addProductButton.setOnClickListener((v) -> {
            Intent intent = new Intent(requireActivity(), ProductForm.class);
            intent.putExtra("FORM_TYPE", "NEW_FORM");
            startActivity(intent);
        });

        return view;
    }
}

