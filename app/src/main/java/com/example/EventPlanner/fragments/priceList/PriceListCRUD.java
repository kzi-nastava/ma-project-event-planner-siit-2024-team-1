package com.example.EventPlanner.fragments.priceList;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentPriceListCrudBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PriceListCRUD#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PriceListCRUD extends Fragment {
    private FragmentPriceListCrudBinding priceListBinding;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PriceListCRUD() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PriceListCRUD.
     */
    // TODO: Rename and change types and number of parameters
    public static PriceListCRUD newInstance(String param1, String param2) {
        PriceListCRUD fragment = new PriceListCRUD();
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
        priceListBinding = FragmentPriceListCrudBinding.inflate(getLayoutInflater());
        PriceListItemList priceList = new PriceListItemList();

        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewPriceList, priceList)
                .commit();
        // Inflate the layout for this fragment
        return priceListBinding.getRoot();
    }
}