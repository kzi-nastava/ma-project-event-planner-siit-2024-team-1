package com.example.EventPlanner.adapters.priceList;

import android.content.Context;
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
import com.example.EventPlanner.fragments.priceList.PriceListItemList;
import com.example.EventPlanner.fragments.priceList.PriceListViewModel;
import com.example.EventPlanner.model.priceList.PriceListItem;
import com.example.EventPlanner.model.priceList.UpdatePriceListItemRequest;

import org.w3c.dom.Text;

import java.util.List;

public class PriceListAdapter extends RecyclerView.Adapter<PriceListAdapter.PriceListHolder> {
    private List<PriceListItem> priceList;
    private Context context;

    public PriceListAdapter(Context context, List<PriceListItem> priceList) {
        this.context = context;
        this.priceList = priceList;
    }

    @NonNull
    @Override
    public PriceListAdapter.PriceListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_price_list_card, parent, false);
        return new PriceListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceListAdapter.PriceListHolder holder, int position) {
        PriceListItem priceListItem = priceList.get(position);
        holder.bind(priceListItem);

        holder.itemView.findViewById(R.id.edit_price_list_button).setOnClickListener(v -> updatePriceList(priceListItem));
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    private void updatePriceList(PriceListItem priceListItem) {
        View dialogView = LayoutInflater.from(context).inflate(R.layout.update_price_list_item_dialog, null);

        EditText price = dialogView.findViewById(R.id.edited_price);
        EditText discount = dialogView.findViewById(R.id.edited_discount);
        Button button = dialogView.findViewById(R.id.update_price_list_item_btn);

        price.setText(String.valueOf(priceListItem.getPrice()));
        discount.setText(String.valueOf(priceListItem.getDiscount()));

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .setTitle("Edit Price List Item")
                .setCancelable(true)
                .create();

        button.setOnClickListener(v -> {
            double priceValue = Double.parseDouble(price.getText().toString().trim());
            int discountValue = Integer.parseInt(discount.getText().toString().trim());

            UpdatePriceListItemRequest dto = new UpdatePriceListItemRequest(priceValue, discountValue);
            PriceListViewModel priceListViewModel = new ViewModelProvider((ViewModelStoreOwner) context)
                    .get(PriceListViewModel.class);
            priceListViewModel.updatePriceListItem(priceListItem.getMerchandiseId(), dto);
            dialog.dismiss();
        });

        dialog.show();
    }

    static class PriceListHolder extends RecyclerView.ViewHolder {
        LinearLayout priceListCard;
        TextView title;
        TextView price;
        TextView discount;
        TextView discountedPrice;
        public PriceListHolder(@NonNull View itemView) {
            super(itemView);
            priceListCard = itemView.findViewById(R.id.price_list_card);
            title = itemView.findViewById(R.id.price_list_title);
            price = itemView.findViewById(R.id.price_list_price);
            discount = itemView.findViewById(R.id.price_list_discount);
            discountedPrice = itemView.findViewById(R.id.price_list_discounted_price);
        }

        public void bind(PriceListItem priceListItem) {
            title.setText(priceListItem.getTitle());
            String priceStr = "Price: " + priceListItem.getPrice();
            price.setText(priceStr);
            String discountStr = "Discount: " + priceListItem.getDiscount();
            discount.setText(discountStr);
            String discountedPriceStr = "Total: " + priceListItem.getDiscountedPrice();
            discountedPrice.setText(discountedPriceStr);
        }
    }
}
