package com.example.EventPlanner.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.databinding.ActivityBudgetBinding;
import com.example.EventPlanner.fragments.budget.BudgetItemsList;
import com.example.EventPlanner.fragments.budget.BudgetViewModel;
import com.example.EventPlanner.model.budget.Budget;
import com.example.EventPlanner.model.budget.CreateBudgetDTO;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.merchandise.CategoryOverview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetActivity extends AppCompatActivity {
    private int eventId;
    private int budgetId;
    private ActivityBudgetBinding budgetBinding;
    private BudgetViewModel budgetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        budgetBinding = ActivityBudgetBinding.inflate(getLayoutInflater());
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        setContentView(budgetBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        eventId = intent.getIntExtra("EVENT_ID", -1);
        if(eventId != -1) {
            budgetViewModel.getBudget().observe(this, budget -> {
                if(budget != null) {
                    budgetId = budget.getBudgetId();
                    setFields(budget);
                }
            });
            budgetViewModel.getBudgetByEvent(eventId);
        }

        budgetBinding.addBudgetItemBtn.setOnClickListener(v -> showAddBudgetItemDialog());
    }

    private void setFields(Budget budget) {
        Call<CreatedEventResponse> eventCall = ClientUtils.eventService.getById(eventId);
        eventCall.enqueue(new Callback<CreatedEventResponse>() {
            @Override
            public void onResponse(Call<CreatedEventResponse> call, Response<CreatedEventResponse> response) {
                if(response.isSuccessful() && response.body() != null) {
                    budgetBinding.eventTitle.setText(response.body().getTitle());
                }else {
                    Log.e("BudgetActivity", "Failed to fetch event: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CreatedEventResponse> call, Throwable throwable) {
                Log.e("BudgetActivity", "Error fetching event: " + throwable.getMessage());
            }
        });

        String maxAmountStr = budget.getMaxAmount() + "€";
        budgetBinding.maxAmount.setText(maxAmountStr);
        String amountSpentStr = budget.getSpentAmount() + "€";
        budgetBinding.spentAmount.setText(amountSpentStr);

        BudgetItemsList budgetItemsList = BudgetItemsList.newInstance(eventId);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewBudget, budgetItemsList)
                .commit();
    }

    public void showAddBudgetItemDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.add_budget_item_dialog, null);

        EditText maxAmount = dialogView.findViewById(R.id.max_amount);
        Spinner categorySpinner = dialogView.findViewById(R.id.category_spinner);
        Button submitButton = dialogView.findViewById(R.id.add_budget_item_btn);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Add Budget Item")
                .setCancelable(true)
                .create();

        List<CategoryOverview> categoryOptions = new ArrayList<>();
        Call<List<CategoryOverview>> categoryCall = ClientUtils.categoryService.getApproved();
        categoryCall.enqueue(new Callback<List<CategoryOverview>>() {
            @Override
            public void onResponse(Call<List<CategoryOverview>> call, Response<List<CategoryOverview>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    categoryOptions.addAll(response.body());
                    CategoryOverview newCategory = new CategoryOverview(-1, "other", "create new category", false);
                    categoryOptions.add(newCategory);
                    ArrayAdapter<CategoryOverview> adapter = new ArrayAdapter<>(
                            BudgetActivity.this, android.R.layout.simple_spinner_item, categoryOptions
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                }else {
                    Log.e("Error fetching categories", "Response not successful: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryOverview>> call, Throwable throwable) {
                Log.e("Error fetching categories", "Error: " + throwable.getMessage());
            }
        });

        submitButton.setOnClickListener(v -> {
            double maxAmountValue = Double.parseDouble(maxAmount.getText().toString().trim());
            CategoryOverview selectedCategory = (CategoryOverview) categorySpinner.getSelectedItem();

            CreateBudgetDTO dto = new CreateBudgetDTO(selectedCategory.getId(), maxAmountValue);
            budgetViewModel.createBudgetItem(budgetId, dto);
            dialog.dismiss();
        });

        dialog.show();
    }
}