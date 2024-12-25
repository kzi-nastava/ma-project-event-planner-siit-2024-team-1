package com.example.EventPlanner.product;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentProductListBinding;

import java.util.ArrayList;

public class ProductList extends Fragment {

    private FragmentProductListBinding productListBinding;
    private ProductViewModel productViewModel;

    public ProductList() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        productListBinding = FragmentProductListBinding.inflate(getLayoutInflater());
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);

        ArrayList<Product> allProducts = productViewModel.getAll();

        ProductAdapter adapter = new ProductAdapter(requireContext(), allProducts);
        RecyclerView recyclerView = productListBinding.productListRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return productListBinding.getRoot();
    }
}

