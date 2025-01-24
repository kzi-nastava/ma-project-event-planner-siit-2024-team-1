package com.example.EventPlanner.adapters.budget;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.ProductDetailsActivity;
import com.example.EventPlanner.activities.ServiceDetailsActivity;
import com.example.EventPlanner.activities.ShowRecommendedMerchandise;
import com.example.EventPlanner.fragments.budget.BudgetViewModel;
import com.example.EventPlanner.model.budget.BudgetItem;
import com.example.EventPlanner.model.budget.UpdateBudgetDTO;


import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {
    private List<BudgetItem> allBudgetItems;
    private Context context;
    private int budgetId;
    private int eventId;

    public BudgetAdapter(Context context, int budgetId, List<BudgetItem> allBudgetItems, int eventId) {
        this.context = context;
        this.allBudgetItems = allBudgetItems;
        this.budgetId = budgetId;
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_budget_item_card, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetItem budgetItem = allBudgetItems.get(position);
        holder.bind(budgetItem);

        holder.itemView.findViewById(R.id.edit_budget_button).setOnClickListener(v -> updateBudgetItem(budgetItem));

        holder.itemView.findViewById(R.id.delete_budget_button).setOnClickListener(v -> {
            BudgetViewModel budgetViewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                    .get(BudgetViewModel.class);
            budgetViewModel.deleteBudgetItem(budgetId, budgetItem.getId());
            allBudgetItems.remove(position);
        });

        setupMerchandiseBtn(holder, budgetItem);
    }

    private void updateBudgetItem(BudgetItem budgetItem) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.update_budget_item_dialog, null);

        EditText maxAmount = dialogView.findViewById(R.id.max_amount);
        Button submitButton = dialogView.findViewById(R.id.update_budget_item_btn);

        maxAmount.setText(String.valueOf(budgetItem.getMaxAmount()));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Edit Budget Item")
                .setCancelable(true)
                .create();

        submitButton.setOnClickListener(v -> {
            double maxAmountValue = Double.parseDouble(maxAmount.getText().toString().trim());

            UpdateBudgetDTO dto = new UpdateBudgetDTO(budgetId, maxAmountValue);
            BudgetViewModel budgetViewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                    .get(BudgetViewModel.class);
            budgetViewModel.updateBudgetItem(budgetItem.getId(), dto);
            dialog.dismiss();
        });

        dialog.show();
    }

    @Override
    public int getItemCount() { return allBudgetItems.size(); }

    private void setupMerchandiseBtn(BudgetViewHolder holder, BudgetItem budgetItem) {
        Button button = holder.itemView.findViewById(R.id.merchandise_btn);
        if(budgetItem.getMerchandise() != null) {
            button.setText(R.string.merchandise_details);
            if(budgetItem.getMerchandise().getType().equals("Service")) {
                button.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ServiceDetailsActivity.class);
                    intent.putExtra("MERCHANDISE_ID", budgetItem.getMerchandise().getId());
                    context.startActivity(intent);
                });
            }else if(budgetItem.getMerchandise().getType().equals("Product")) {
                button.setOnClickListener(v -> {
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("MERCHANDISE_ID", budgetItem.getMerchandise().getId());
                    context.startActivity(intent);
                });
            }
        }else {
            button.setText(R.string.choose_merchandise);
            button.setOnClickListener(v -> {
                Intent intent = new Intent(context, ShowRecommendedMerchandise.class);
                intent.putExtra("CATEGORY_ID", budgetItem.getCategory().getId());
                intent.putExtra("MAX_PRICE", budgetItem.getMaxAmount());
                intent.putExtra("EVENT_ID", eventId);
                context.startActivity(intent);
            });
        }
    }

    static class BudgetViewHolder extends RecyclerView.ViewHolder {
        LinearLayout budgetItemCard;
        TextView categoryTitle;
        TextView maxAmount;
        TextView spentAmount;

        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            budgetItemCard = itemView.findViewById(R.id.budget_item_card);
            categoryTitle = itemView.findViewById(R.id.category_title);
            maxAmount = itemView.findViewById(R.id.max_amount);
            spentAmount = itemView.findViewById(R.id.spent_amount);
        }

        public void bind(BudgetItem budgetItem) {
            categoryTitle.setText(budgetItem.getCategory().getTitle());
            String maxAmountStr = "Max Amount: " + budgetItem.getMaxAmount() + "€";
            maxAmount.setText(maxAmountStr);
            String spentAmountStr = "Spent Amount: " + budgetItem.getAmountSpent() + "€";
            spentAmount.setText(spentAmountStr);
        }
    }
}
