package com.example.EventPlanner.fragments.merchandise.product;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.event.EventTypeAdapter;
import com.example.EventPlanner.adapters.merchandise.MerchandiseOverviewAdapter;
import com.example.EventPlanner.adapters.merchandise.product.ProductAdapter;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.FragmentProductListBinding;
import com.example.EventPlanner.model.merchandise.product.Product;

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
        String topString=getString(R.string.top);
        String extraValue = topString; // Default value
        if (getArguments() != null) {
            extraValue = getArguments().getString("type", topString);
        }
        TextView eventsHeader=productListBinding.productsHeader;
        RecyclerView recyclerView = productListBinding.productListRecyclerView;


        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        switch (extraValue){
            case "my":
            case "My":
                productViewModel.getProducts().observe(getViewLifecycleOwner(),products->{
                    ProductAdapter productAdapter = new ProductAdapter(requireActivity(), products);
                    recyclerView.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();

                });
                eventsHeader.setText(R.string.my_products);
                productViewModel.getAllBySp(JwtService.getIdFromToken());
                break;
            case "fav":
            case "Fav":
                productViewModel.getMerc().observe(getViewLifecycleOwner(),merc->{
                    MerchandiseOverviewAdapter merchandiseOverviewAdapter = new MerchandiseOverviewAdapter(requireActivity(), merc, -1);
                    recyclerView.setAdapter(merchandiseOverviewAdapter);
                    merchandiseOverviewAdapter.notifyDataSetChanged();

                });
                eventsHeader.setText(R.string.favorite_merchandise);
                productViewModel.getFavorites();
                break;

        }


        return productListBinding.getRoot();
    }
}

