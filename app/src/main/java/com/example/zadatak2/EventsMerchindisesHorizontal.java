package com.example.zadatak2;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zadatak2.databinding.FragmentEventsMerchandiseListBinding;
import com.example.zadatak2.databinding.FragmentEventsMerchindisesHorizontalBinding;
import com.example.zadatak2.event.EventsList;
import com.example.zadatak2.eventmerchandise.EventMerchandise;
import com.example.zadatak2.eventmerchandise.EventMerchandiseViewModel;
import com.example.zadatak2.eventmerchandise.EventsMerchandiseAdapter;
import com.example.zadatak2.merchandise.MerchandiseList;

import java.util.ArrayList;
import java.util.List;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        eventsMerchandiseHorizontalBinding= FragmentEventsMerchindisesHorizontalBinding.inflate(getLayoutInflater());
        String extraValue = "top"; // Default value
        if (getArguments() != null) {
            extraValue = getArguments().getString("type", "top");
        }
        Bundle bundle = new Bundle();
        bundle.putString("type", extraValue);
        EventsList eventsList=new EventsList();
        eventsList.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewEvent, eventsList)
                .commit();
        MerchandiseList merchandiseList=new MerchandiseList();
        merchandiseList.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewMerchandise, merchandiseList)
                .commit();

        return  eventsMerchandiseHorizontalBinding.getRoot();
    }
}