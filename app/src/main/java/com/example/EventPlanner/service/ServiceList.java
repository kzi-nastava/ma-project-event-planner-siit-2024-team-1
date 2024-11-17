package com.example.EventPlanner.service;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.databinding.FragmentServiceListBinding;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentServiceListBinding serviceListBinding;
    private ServiceViewModel serviceViewModel;

    public ServiceList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceList.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceList newInstance(String param1, String param2) {
        ServiceList fragment = new ServiceList();
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
        serviceListBinding = FragmentServiceListBinding.inflate(getLayoutInflater());
        serviceViewModel = new ViewModelProvider(requireActivity()).get(ServiceViewModel.class);

        ArrayList<Service> allServices = new ArrayList<Service>();
        allServices = serviceViewModel.getAll();

        ServiceAdapter adapter = new ServiceAdapter(requireContext(), allServices);
        RecyclerView recyclerView = serviceListBinding.serviceListVertical;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        return serviceListBinding.getRoot();
    }
}