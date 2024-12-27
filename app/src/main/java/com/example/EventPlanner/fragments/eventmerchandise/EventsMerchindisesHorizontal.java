package com.example.EventPlanner.fragments.eventmerchandise;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentEventsMerchindisesHorizontalBinding;
import com.example.EventPlanner.fragments.event.EventsList;
import com.example.EventPlanner.fragments.merchandise.MerchandiseList;
import android.widget.Button;
import android.widget.PopupMenu;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventsMerchindisesHorizontal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventsMerchindisesHorizontal extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentEventsMerchindisesHorizontalBinding eventsMerchandiseHorizontalBinding;
    private Button sortByEventsButton;
    private Button sortByMerchandiseButton;
    private Button filtersButton;
    private FragmentContainerView filterFragmentContainer;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EventsMerchindisesHorizontal() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventsMerchindisesHorizontal.
     */
    // TODO: Rename and change types and number of parameters
    public static EventsMerchindisesHorizontal newInstance(String param1, String param2) {
        EventsMerchindisesHorizontal fragment = new EventsMerchindisesHorizontal();
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
        getChildFragmentManager()
                .setFragmentResultListener("applyFilters", this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle bundle) {
                        String result = bundle.getString("filter_result");
                        hideFilterFragment();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        eventsMerchandiseHorizontalBinding = FragmentEventsMerchindisesHorizontalBinding.inflate(getLayoutInflater());
        View view = eventsMerchandiseHorizontalBinding.getRoot();

        String extraValue = getArguments() != null ? getArguments().getString("type", "top") : "top";
        switch (extraValue){
            case "top":
            case "Top":
                eventsMerchandiseHorizontalBinding.filtersOptionsHorizontalScroll.setVisibility(View.GONE);
                break;
            case "all":
            case "All":
                eventsMerchandiseHorizontalBinding.filtersOptionsHorizontalScroll.setVisibility(View.VISIBLE);
                break;
        }
        Bundle bundle = new Bundle();
        bundle.putString("type", extraValue);
        EventsList eventsList=new EventsList();
        eventsList.setArguments(bundle);
        MerchandiseList merchandiseList=new MerchandiseList();
        merchandiseList.setArguments(bundle);


        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewEvent, eventsList)
                .replace(R.id.fragmentContainerViewMerchandise, merchandiseList)
                .commit();

        Button sortByEventsButton = eventsMerchandiseHorizontalBinding.sortEventsByButton;
        Button sortByMerchandiseButton = eventsMerchandiseHorizontalBinding.sortMerchandiseByButton;
        Button filtersButton = eventsMerchandiseHorizontalBinding.filtersButton;
        filterFragmentContainer = eventsMerchandiseHorizontalBinding.fragmentContainerViewFilters;

        sortByEventsButton.setOnClickListener(this::showSortByEventsMenu);
        sortByMerchandiseButton.setOnClickListener(this::showSortByMerchandiseMenu);
        filtersButton.setOnClickListener(this::handleFiltersButtonClick);




        return view;
    }

    private void handleFiltersButtonClick(View v) {
        if (filterFragmentContainer.getVisibility() == View.GONE) {
            getChildFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerViewFilters, new Filters())
                    .commit();
            filterFragmentContainer.setVisibility(View.VISIBLE);
            eventsMerchandiseHorizontalBinding.fragmentContainerViewEvent.setVisibility(View.GONE);
            eventsMerchandiseHorizontalBinding.fragmentContainerViewMerchandise.setVisibility(View.GONE);
        } else {
            hideFilterFragment();
        }
    }
    private void hideFilterFragment() {
        Filters filterFragment = (Filters) getChildFragmentManager().findFragmentById(R.id.fragment_filters);
        if (filterFragment != null) {
            getChildFragmentManager().beginTransaction()
                    .remove(filterFragment)
                    .commit();
        }
        filterFragmentContainer.setVisibility(View.GONE);
        eventsMerchandiseHorizontalBinding.fragmentContainerViewEvent.setVisibility(View.VISIBLE);
        eventsMerchandiseHorizontalBinding.fragmentContainerViewMerchandise.setVisibility(View.VISIBLE);
    }

    private void showSortByEventsMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.sort_by_events_menu, popup.getMenu());
        popup.show();
    }

    private void showSortByMerchandiseMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.sort_by_merchandise_menu, popup.getMenu());
        popup.show();
    }
    private void showFiltersMenu(View view) {
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.getMenuInflater().inflate(R.menu.filters_menu, popup.getMenu());
        popup.show();
    }
}