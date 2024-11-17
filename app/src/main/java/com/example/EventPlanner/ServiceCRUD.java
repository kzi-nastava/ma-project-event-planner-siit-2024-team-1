package com.example.EventPlanner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.EventPlanner.databinding.FragmentServiceCrudBinding;
import com.example.EventPlanner.databinding.FragmentServiceListBinding;
import com.example.EventPlanner.service.ServiceList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceCRUD#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceCRUD extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentServiceCrudBinding serviceCrudBinding;

    public ServiceCRUD() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceCRUD.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceCRUD newInstance(String param1, String param2) {
        ServiceCRUD fragment = new ServiceCRUD();
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
        serviceCrudBinding = FragmentServiceCrudBinding.inflate(getLayoutInflater());
        View view = serviceCrudBinding.getRoot();
        ServiceList serviceList = new ServiceList();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewService, serviceList).commit();

        Button addServiceButton = (Button) serviceCrudBinding.addServiceButton;
        addServiceButton.setOnClickListener((v) -> {
            Intent intent = new Intent(requireActivity(), ServiceForm.class);
            intent.putExtra("FORM_TYPE", "NEW_FORM");
            startActivity(intent);
        });
        // Inflate the layout for this fragment
        return view;
    }
}