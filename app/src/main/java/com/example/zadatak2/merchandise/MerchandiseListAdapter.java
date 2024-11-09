package com.example.zadatak2.merchandise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zadatak2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class MerchandiseListAdapter extends ArrayAdapter<Merchandise> {
    private ArrayList<Merchandise> aMerchandises;
    public MerchandiseListAdapter(Context context,ArrayList<Merchandise> merchandises){
        super(context, R.layout.fragment_merchandise_card,merchandises);
        aMerchandises=merchandises;
    }

    @Override
    public int getCount(){
        return aMerchandises.size();
    }
    @Nullable
    @Override
    public Merchandise getItem(int position){
        return  aMerchandises.get(position);
    }
    @Override
    public long getItemId(int position){
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Merchandise merchandise = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_merchandise_card, parent, false);
        }

        LinearLayout merchandiseCard = convertView.findViewById(R.id.merchandise_card_item);
        ImageView merchandiseImage = convertView.findViewById(R.id.merchandise_image);
        TextView merchandiseTitle = convertView.findViewById(R.id.merchandise_title);
        TextView merchandiseDescription = convertView.findViewById(R.id.merchandise_description);
        TextView merchandisePrice = convertView.findViewById(R.id.merchandise_price);
        TextView merchandiseLocation = convertView.findViewById(R.id.merchandise_location);
        RatingBar merchandiseRating = convertView.findViewById(R.id.merchandise_rating);
        TextView merchandiseDiscount = convertView.findViewById(R.id.merchandise_discount);
        TextView merchandiseRatingText=convertView.findViewById(R.id.merchandise_rating_text);
        TextView merchandiseCategory=convertView.findViewById(R.id.merchandise_category);

        if (merchandise != null) {
            if (merchandise.getPhotos() != null && !merchandise.getPhotos().isEmpty()) {
                merchandiseImage.setImageResource(R.drawable.dinja);
            }

            merchandiseTitle.setText(merchandise.getTitle());
            merchandiseDescription.setText(merchandise.getDescription());

            double finalPrice = merchandise.getPrice() * (1 - merchandise.getDiscount());
            merchandisePrice.setText(String.format("$%.2f", finalPrice));

            if (merchandise.getDiscount() > 0) {
                merchandiseDiscount.setVisibility(View.VISIBLE);
                merchandiseDiscount.setText(String.format("-%d%%", (int) (merchandise.getDiscount() * 100)));
            } else {
                merchandiseDiscount.setVisibility(View.GONE);
            }

            merchandiseLocation.setText(String.format("%s, %s",
                    merchandise.getAddress().getCity(),
                    merchandise.getAddress().getStreet()));

            merchandiseRating.setRating(merchandise.getRating().floatValue());
            merchandiseRatingText.setText(String.format(Locale.getDefault(), "%.1f", merchandise.getRating()));
            merchandiseCard.setOnClickListener(v -> {
                Toast.makeText(getContext(), merchandise.getTitle(), Toast.LENGTH_SHORT).show();
            });
            merchandiseCategory.setText(String.format(Locale.getDefault(), "%s/%s",merchandise.getCategory().getTitle(),merchandise.getClass().getSimpleName()));
        }

        return convertView;
    }
}
