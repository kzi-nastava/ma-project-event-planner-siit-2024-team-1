package com.example.EventPlanner.fragments.merchandise;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.EventPlanner.adapters.event.EventOverviewAdapter;
import com.example.EventPlanner.adapters.merchandise.MerchandiseAdapter;
import com.example.EventPlanner.adapters.merchandise.MerchandiseOverviewAdapter;
import com.example.EventPlanner.fragments.common.map.MapViewModel;
import com.example.EventPlanner.fragments.eventmerchandise.DotsIndicatorDecoration;
import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentMerchandisesListBinding;
import com.example.EventPlanner.fragments.eventmerchandise.SearchViewModel;
import com.example.EventPlanner.model.merchandise.Merchandise;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MerchandiseList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MerchandiseList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MerchandiseViewModel merchandiseViewModel;
    private SearchViewModel searchViewModel;
    private MapViewModel mapViewModel;


    private FragmentMerchandisesListBinding merchandisesListBinding;


    public MerchandiseList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Merchandises_list.
     */
    // TODO: Rename and change types and number of parameters
    public static MerchandiseList newInstance(String param1, String param2) {
        MerchandiseList fragment = new MerchandiseList();
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
        // Inflate the layout for this fragment
        merchandisesListBinding=FragmentMerchandisesListBinding.inflate(getLayoutInflater());
        merchandiseViewModel =new ViewModelProvider(requireActivity()).get(MerchandiseViewModel.class);
        searchViewModel=new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mapViewModel=new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        String topString=getString(R.string.top);
        String extraValue = topString; // Default value
        if (getArguments() != null) {
            extraValue = getArguments().getString("type", topString);
        }
        TextView merchandiseHeader=merchandisesListBinding.merchandiseHeader;
        RecyclerView recyclerView = merchandisesListBinding.merchandisesListHorizontal;
        merchandiseViewModel.getMerchandise().observe(getViewLifecycleOwner(),merchandise->{
            MerchandiseOverviewAdapter merchandiseAdapter = new MerchandiseOverviewAdapter(requireContext(), merchandise);
            recyclerView.setAdapter(merchandiseAdapter);
            merchandiseAdapter.notifyDataSetChanged();
            mapViewModel.setMerchandise(merchandise);
        });
        recyclerView.addItemDecoration(new DotsIndicatorDecoration(
                ContextCompat.getColor(getContext(), R.color.accent_color),
                ContextCompat.getColor(getContext(), R.color.primary_color)
        ));
        LinearLayoutManager layoutManager= new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        switch (extraValue){
            case "top":
            case "Top":
                merchandiseHeader.setText(R.string.top_merchandise);
                merchandiseViewModel.getTop();
                break;
            case "all":
            case "All":
                merchandiseHeader.setText(R.string.all_merchandise);
                merchandiseViewModel.search(Boolean.TRUE.equals(searchViewModel.getShowServices().getValue()),
                        Boolean.TRUE.equals(searchViewModel.getShowProducts().getValue()),
                        searchViewModel.getSearchText().getValue(),searchViewModel.getProductPriceMin().getValue(),
                        searchViewModel.getProductPriceMax().getValue(),searchViewModel.getProductDurationMin().getValue(),
                        searchViewModel.getProductDurationMax().getValue(),
                        searchViewModel.getProductCity().getValue(),searchViewModel.getProductCategory().getValue(),
                        searchViewModel.getServicePriceMin().getValue(),searchViewModel.getServicePriceMax().getValue(),searchViewModel.getServiceDurationMin().getValue(),
                        searchViewModel.getServiceDurationMax().getValue(),searchViewModel.getServiceCity().getValue(),searchViewModel.getServiceCategory().getValue(),
                        searchViewModel.getMerchandiseSortBy().getValue(), Boolean.TRUE.equals(searchViewModel.getMerchandiseSortByAscending().getValue()));
                break;

        }


        return merchandisesListBinding.getRoot();
    }
}