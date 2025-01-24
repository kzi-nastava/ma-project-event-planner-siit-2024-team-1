package com.example.EventPlanner.fragments.priceList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.priceList.PriceListAdapter;
import com.example.EventPlanner.databinding.FragmentPriceListBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PriceListItemList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceListItemList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentPriceListBinding priceListBinding;
    private PriceListViewModel priceListViewModel;

    public PriceListItemList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PriceList.
     */
    // TODO: Rename and change types and number of parameters
    public static PriceListItemList newInstance(String param1, String param2) {
        PriceListItemList fragment = new PriceListItemList();
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
        priceListBinding = FragmentPriceListBinding.inflate(getLayoutInflater());
        priceListViewModel = new ViewModelProvider(requireActivity()).get(PriceListViewModel.class);
        RecyclerView recyclerView = priceListBinding.priceListItemListVertical;

        priceListViewModel.getPriceListItems().observe(getViewLifecycleOwner(), priceList -> {
            PriceListAdapter adapter = new PriceListAdapter(requireContext(), priceList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        });

        priceListViewModel.getPriceList();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        // Inflate the layout for this fragment
        return priceListBinding.getRoot();
    }
}