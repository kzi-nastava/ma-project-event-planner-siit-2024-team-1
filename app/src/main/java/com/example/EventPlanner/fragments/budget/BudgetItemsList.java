package com.example.EventPlanner.fragments.budget;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.budget.BudgetAdapter;
import com.example.EventPlanner.databinding.FragmentBudgetItemsListBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetItemsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetItemsList extends Fragment {
    private static final String EVENT_ID = "EVENT_ID";

    private int eventId;
    private FragmentBudgetItemsListBinding budgetItemsListBinding;
    private BudgetViewModel budgetViewModel;

    public BudgetItemsList() {
        // Required empty public constructor
    }

    public static BudgetItemsList newInstance(int event_id) {
        BudgetItemsList fragment = new BudgetItemsList();
        Bundle args = new Bundle();
        args.putInt(EVENT_ID, event_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            eventId = getArguments().getInt(EVENT_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        budgetItemsListBinding = FragmentBudgetItemsListBinding.inflate(getLayoutInflater());
        budgetViewModel = new ViewModelProvider(requireActivity()).get(BudgetViewModel.class);
        RecyclerView recyclerView = budgetItemsListBinding.budgetItemListVertical;

        budgetViewModel.getBudget().observe(getViewLifecycleOwner(), budget -> {
            BudgetAdapter budgetAdapter = new BudgetAdapter(requireContext(), budget.getBudgetId(), budget.getBudgetItems(), eventId);
            recyclerView.setAdapter(budgetAdapter);
            budgetAdapter.notifyDataSetChanged();
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        // Inflate the layout for this fragment
        return budgetItemsListBinding.getRoot();
    }
}