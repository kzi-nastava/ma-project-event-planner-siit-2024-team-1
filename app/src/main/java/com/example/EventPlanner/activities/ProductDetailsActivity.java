package com.example.EventPlanner.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.merchandise.PhotoSliderAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.databinding.ActivityProductDetailsBinding;
import com.example.EventPlanner.fragments.merchandise.MerchandiseViewModel;
import com.example.EventPlanner.model.merchandise.MerchandiseDetailsDTO;
import com.example.EventPlanner.model.user.GetSpById;
import com.example.EventPlanner.services.WebSocketService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private int merchandiseId;
    private ActivityProductDetailsBinding activityProductDetailsBinding;
    private MerchandiseViewModel merchandiseViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityProductDetailsBinding = ActivityProductDetailsBinding.inflate(getLayoutInflater());
        merchandiseViewModel = new ViewModelProvider(this).get(MerchandiseViewModel.class);
        setContentView(activityProductDetailsBinding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent=getIntent();
        merchandiseId=intent.getIntExtra("MERCHANDISE_ID",-1);
        if(merchandiseId != -1) {
            merchandiseViewModel.getMerchandiseDetails().observe(this, merchandise -> {
                if(merchandise != null) {
                    setFields(merchandise);
                    Button openMessengerButton = activityProductDetailsBinding.messengerButton;
                    openMessengerButton.setOnClickListener(v -> openMessengerButton(merchandise.getServiceProviderId()));
                }
            });
            merchandiseViewModel.merchandiseDetails(merchandiseId);
        }
        Button buyProduct = activityProductDetailsBinding.buyProductBtn;
        buyProduct.setOnClickListener(v -> openBuyProduct(merchandiseId));
        activityProductDetailsBinding.favoriteButton.setOnClickListener(v -> setMerchandiseAsFavorite(merchandiseId));
        int notificationId = getIntent().getIntExtra("NOTIFICATION_ID", -1);
        if (notificationId != -1) {
            // Mark notification as read
            Intent markReadIntent = new Intent(this, WebSocketService.class);
            markReadIntent.setAction(WebSocketService.ACTION_MARK_READ);
            markReadIntent.putExtra("NOTIFICATION_ID", notificationId);
            startService(markReadIntent);
        }
    }

    private void setFields(MerchandiseDetailsDTO merchandise) {
        if(!merchandise.getVisible()) {
            String messageStr = "Service is not visible";
            activityProductDetailsBinding.productTitle.setText(messageStr);
            activityProductDetailsBinding.favoriteButton.setVisibility(View.GONE);
            activityProductDetailsBinding.locationIcon.setVisibility(View.GONE);
            activityProductDetailsBinding.buyProductBtn.setVisibility(View.GONE);
            activityProductDetailsBinding.profileIcon.setVisibility(View.GONE);
            activityProductDetailsBinding.messengerButton.setVisibility(View.GONE);
            return;
        }

        if(isFavorite(merchandise.getId())) {
            activityProductDetailsBinding.favoriteButton.setImageResource(R.drawable.ic_star_filled);
        }else {
            activityProductDetailsBinding.favoriteButton.setImageResource(R.drawable.ic_star_border);
        }

        if(!merchandise.getAvailable()) {
            activityProductDetailsBinding.buyProductBtn.setClickable(false);
        }
        if(merchandise.getMerchandisePhotos() != null && !merchandise.getMerchandisePhotos().isEmpty()) {
            PhotoSliderAdapter photosAdapter = new PhotoSliderAdapter(this, merchandise.getMerchandisePhotos());
            activityProductDetailsBinding.merchandiseImages.setAdapter(photosAdapter);
        }
        activityProductDetailsBinding.productTitle.setText(merchandise.getTitle());
        activityProductDetailsBinding.productCategory.setText(merchandise.getCategory().getTitle());
        String address = merchandise.getAddress().getStreet() + " " + merchandise.getAddress().getNumber() + ", " + merchandise.getAddress().getCity();
        activityProductDetailsBinding.productAddress.setText(address);

        if(merchandise.getDiscount() > 0) {
            String price = String.valueOf(merchandise.getPrice()) + "€";
            SpannableString spannebleString = new SpannableString(price);

            spannebleString.setSpan(new StrikethroughSpan(), 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannebleString.setSpan(new ForegroundColorSpan(Color.RED), 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            activityProductDetailsBinding.productPrice.setText(spannebleString);
            double discountedPrice = merchandise.getPrice() - (merchandise.getPrice()*merchandise.getDiscount())/100;
            String discountedPriceStr = discountedPrice + "€";
            activityProductDetailsBinding.productDiscountedPrice.setText(discountedPriceStr);
        }else {
            String price = String.valueOf(merchandise.getPrice()) + "€";
            activityProductDetailsBinding.productPrice.setText(price);
        }

        String duration = merchandise.getMinDuration() + "-" + merchandise.getMaxDuration();
        activityProductDetailsBinding.productDuration.setText(duration);
        String reservationDeadline = "Reservation Deadline: " + merchandise.getReservationDeadline();
        activityProductDetailsBinding.productReservation.setText(reservationDeadline);
        String cancellationDeadline = "Cancellation Deadline: " + merchandise.getCancellationDeadline();
        activityProductDetailsBinding.productCancellation.setText(cancellationDeadline);
        activityProductDetailsBinding.productDescription.setText(merchandise.getDescription());
        activityProductDetailsBinding.productSpecificity.setText(merchandise.getSpecificity());

        Call<GetSpById> spCall = ClientUtils.userService.getSpById(merchandise.getServiceProviderId());
        spCall.enqueue(new Callback<GetSpById>() {
            @Override
            public void onResponse(Call<GetSpById> call, Response<GetSpById> response) {
                if(response.isSuccessful() && response.body() != null) {
                    String serviceProvider = response.body().getName() + " " + response.body().getSurname();
                    activityProductDetailsBinding.serviceProviderName.setText(serviceProvider);
                }else {
                    String serviceProvider = "Service Provider";
                    activityProductDetailsBinding.serviceProviderName.setText(serviceProvider);
                }
            }
            @Override
            public void onFailure(Call<GetSpById> call, Throwable throwable) {
                String serviceProvider = "Service Provider";
                activityProductDetailsBinding.serviceProviderName.setText(serviceProvider);
            }
        });
    }

    private void openMessengerButton(int merchandiseServiceProviderId) {
        if(merchandiseServiceProviderId != -1) {
            Intent intent = new Intent(ProductDetailsActivity.this, MessengerActivity.class);
            intent.putExtra("USER_ID", merchandiseServiceProviderId);
            startActivity(intent);
        }
    }

    private void openBuyProduct(int merchandiseId) {
        Intent intent = new Intent(ProductDetailsActivity.this, BuyProductActivity.class);
        intent.putExtra("MERCHANDISE_ID", merchandiseId);
        startActivity(intent);
    }

    private boolean isFavorite = false;
    private boolean isFavorite(int merchandiseId) {
        //TODO: implement logic if merchandise is in List of favorite merchandises in user
        return isFavorite;
    }
    private void setMerchandiseAsFavorite(int merchandiseId) {
        if(isFavorite(merchandiseId)) {
            //TODO: implement logic when merchandise is added to favorite
            this.isFavorite = false;
            activityProductDetailsBinding.favoriteButton.setImageResource(R.drawable.ic_star_border);
        }else {
            this.isFavorite = true;
            activityProductDetailsBinding.favoriteButton.setImageResource(R.drawable.ic_star_filled);
        }
    }
}