package com.example.EventPlanner.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.databinding.ActivityCategoryFormBinding;
import com.example.EventPlanner.fragments.merchandise.category.CategoryViewModel;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.merchandise.CreateCategoryRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryForm extends AppCompatActivity {
    private ActivityCategoryFormBinding categoryFormBinding;
    private CategoryViewModel categoryViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryFormBinding = ActivityCategoryFormBinding.inflate(getLayoutInflater());
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(categoryFormBinding.getRoot());

        String formType = getIntent().getStringExtra("FORM_TYPE");
        TextView formTitle = categoryFormBinding.formTitle;

        Spinner categorySpinner = categoryFormBinding.categorySpinner;
        List<CategoryOverview> categoryOptions = new ArrayList<>();
        Call<List<CategoryOverview>> categoryCall = ClientUtils.categoryService.getApproved();
        categoryCall.enqueue(new Callback<List<CategoryOverview>>() {
            @Override
            public void onResponse(Call<List<CategoryOverview>> call, Response<List<CategoryOverview>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    categoryOptions.addAll(response.body());
                    ArrayAdapter<CategoryOverview> adapter = new ArrayAdapter<>(
                            CategoryForm.this, android.R.layout.simple_spinner_item, categoryOptions
                    );
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(adapter);
                }else {
                    Log.e("CategoryForm", "Failed to fetch all approved categories: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<List<CategoryOverview>> call, Throwable throwable) {
                Log.e("CategoryForm", "Error loading approved categories: " + throwable.getMessage());
            }
        });

        if(formType.equals("NEW_FORM")) {
            formTitle.setText("New Category");
        }else if(formType.equals("EDIT_FORM")) {
            int categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
            if(categoryId != -1) {
                categoryViewModel.getSelectedCategory().observe(this, category -> {
                    if(category != null) {
                        setFields(category);
                        if(category.isPending()) {
                            formTitle.setText("Replace Category");
                            categoryFormBinding.replacedCategory.setVisibility(View.VISIBLE);
                            categoryFormBinding.replacedCategoryTitle.setVisibility(View.VISIBLE);
                            categoryFormBinding.replacedCategory.setText(category.getTitle());
                            categoryFormBinding.categorySpinner.setVisibility(View.VISIBLE);
                            categoryFormBinding.title.setVisibility(View.GONE);
                            categoryFormBinding.description.setVisibility(View.GONE);
                        }else {
                            formTitle.setText("Edit Category");
                            categoryFormBinding.categorySpinner.setVisibility(View.GONE);
                        }
                    }
                });
                categoryViewModel.getById(categoryId);
            }
        }

        categoryFormBinding.submitServiceButton.setOnClickListener(v -> {
            if(isValidInput()) {
                if("NEW_FORM".equals(formType)) {
                    CreateCategoryRequest dto = createCategoryRequest();
                    if(dto != null) {
                        categoryViewModel.createCategory(dto);
                    }
                }else if("EDIT_FORM".equals(formType)) {
                    int categoryId = getIntent().getIntExtra("CATEGORY_ID", -1);
                    if(categoryId != -1) {
                        if(categoryViewModel.getSelectedCategory().getValue().isPending()) {
                            CategoryOverview selectedCategory = (CategoryOverview) categoryFormBinding.categorySpinner.getSelectedItem();
                            categoryViewModel.replaceCategory(selectedCategory.getId(), categoryId);
                        }else {
                            CreateCategoryRequest dto = createCategoryRequest();
                            categoryViewModel.updateCategory(categoryId, dto);
                        }
                    }

                }

                Intent intent = new Intent(CategoryForm.this, HomeScreen.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setFields(CategoryOverview category) {
        categoryFormBinding.title.setText(category.getTitle());
        categoryFormBinding.description.setText(category.getDescription());
    }

    private boolean isValidInput() {
        return !TextUtils.isEmpty(categoryFormBinding.title.getText()) &&
                !TextUtils.isEmpty(categoryFormBinding.description.getText());
    }

    private CreateCategoryRequest createCategoryRequest() {
        String title = categoryFormBinding.title.getText().toString();
        String description = categoryFormBinding.description.getText().toString();
        return new CreateCategoryRequest(title, description, false);
    }
}