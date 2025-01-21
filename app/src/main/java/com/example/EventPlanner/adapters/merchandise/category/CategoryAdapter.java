package com.example.EventPlanner.adapters.merchandise.category;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.CategoryForm;
import com.example.EventPlanner.fragments.merchandise.category.CategoryList;
import com.example.EventPlanner.fragments.merchandise.category.CategoryViewModel;
import com.example.EventPlanner.model.merchandise.CategoryOverview;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private ArrayList<CategoryOverview> allCategories;
    private Context context;

    private CategoryList.CategoryType categoryType;

    public CategoryAdapter(Context context, ArrayList<CategoryOverview> allCategories, CategoryList.CategoryType categoryType) {
        this.context = context;
        this.allCategories = allCategories;
        this.categoryType = categoryType;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = categoryType == CategoryList.CategoryType.APPROVED
                ? R.layout.fragment_approved_category_card
                : R.layout.fragment_pending_category_card;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryOverview category = allCategories.get(position);
        holder.bind(category);

        holder.itemView.findViewById(R.id.edit_category_button).setOnClickListener(v -> {
            Intent intent = new Intent(context, CategoryForm.class);
            intent.putExtra("FORM_TYPE", "EDIT_FORM");
            intent.putExtra("CATEGORY_ID", category.getId());
            context.startActivity(intent);
        });

        holder.itemView.findViewById(R.id.delete_category_button).setOnClickListener(v -> {
            CategoryViewModel categoryViewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                    .get(CategoryViewModel.class);
            categoryViewModel.deleteCategory(category.getId());

            allCategories.remove(position);

            notifyItemRemoved(position);
            notifyItemRangeChanged(position, allCategories.size());
        });

        if(this.categoryType == CategoryList.CategoryType.PENDING) {
            holder.itemView.findViewById(R.id.approve_category_button).setOnClickListener(v -> {
                CategoryViewModel categoryViewModel = new CategoryViewModel();
                categoryViewModel.approveCategory(category.getId());
            });
        }
    }

    @Override
    public int getItemCount() { return allCategories.size(); }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        LinearLayout categoryCard;
        TextView categoryTitle;
        TextView categoryDescription;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryCard = itemView.findViewById(R.id.category_card);
            categoryTitle = itemView.findViewById(R.id.category_title);
            categoryDescription = itemView.findViewById(R.id.category_description);
        }

        public void bind(CategoryOverview category) {
            categoryTitle.setText(category.getTitle());
            categoryDescription.setText(category.getDescription());
        }
    }
}
