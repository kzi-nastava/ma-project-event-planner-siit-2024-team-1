package com.example.EventPlanner.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.merchandise.PhotoSliderAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.ActivityProductDetailsBinding;
import com.example.EventPlanner.fragments.common.review.MerchandiseReviewList;
import com.example.EventPlanner.fragments.merchandise.MerchandiseViewModel;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.MerchandiseDetailsDTO;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;
import com.example.EventPlanner.model.user.GetSpById;
import com.example.EventPlanner.services.WebSocketService;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailsActivity extends AppCompatActivity {

    private int merchandiseId;
    private ActivityProductDetailsBinding activityProductDetailsBinding;
    private MerchandiseViewModel merchandiseViewModel;
    private ImageButton starButton;
    private boolean isFavorited = false;


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
        int eventId = intent.getIntExtra("EVENT_ID", -1);
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
        buyProduct.setOnClickListener(v -> openBuyProduct(merchandiseId, eventId));

        starButton = findViewById(R.id.favorite_button);

        Call<List<MerchandiseOverview>> call1 = ClientUtils.merchandiseService.getFavorites(JwtService.getIdFromToken());
        call1.enqueue(new Callback<List<MerchandiseOverview>>() {
            @Override
            public void onResponse(Call<List<MerchandiseOverview>> call, Response<List<MerchandiseOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if(response.body().stream().filter(x -> x.getId() == merchandiseId).findAny().isPresent()){
                        isFavorited = true;
                    }
                    else{
                        isFavorited = false;
                    }
                    updateStarIcon();
                } else {
                    // Handle error cases
                    Log.e("Favorizing Event Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<MerchandiseOverview>> call, Throwable throwable) {
                // Handle network errors
                Log.e("Favorizing Event Failure", "Error: " + throwable.getMessage());
            }
        });

        if(JwtService.getIdFromToken() == -1){
            starButton.setVisibility(View.GONE);
        }

        // Handle star button click
        starButton.setOnClickListener(view -> {
            isFavorited = !isFavorited; // Toggle the favorite state
            updateStarIcon();
            saveFavoriteState(); // Save state to backend or local storage
        });

        Bundle bundle = new Bundle();
        bundle.putInt("id", merchandiseId);
        bundle.putString("reviewType", "MERCHANDISE_REVIEW");
        MerchandiseReviewList reviewList = new MerchandiseReviewList();
        reviewList.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainerViewReviews, reviewList)
                .commit();

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

    private void updateStarIcon() {
        if (isFavorited) {
            starButton.setImageResource(R.drawable.ic_star_filled); // Use filled star
        } else {
            starButton.setImageResource(R.drawable.ic_star_border); // Use empty star
        }
    }

    private void saveFavoriteState() {
        Call<Boolean> call1 = ClientUtils.merchandiseService.favorizeMerchandise(merchandiseId, JwtService.getIdFromToken());
        call1.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    // Handle error cases
                    Log.e("Favorizing Event Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                // Handle network errors
                Log.e("Favorizing Event Failure", "Error: " + throwable.getMessage());
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

    private void openBuyProduct(int merchandiseId, int eventId) {
        if(eventId != -1) {
            Call<ResponseBody> call = ClientUtils.productService.buyProduct(merchandiseId, eventId);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful()) {
                        Intent intent = new Intent(ProductDetailsActivity.this, BudgetActivity.class);
                        intent.putExtra("EVENT_ID", eventId);
                        startActivity(intent);
                    }else {
                        Intent intent = new Intent(ProductDetailsActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("MERCHANDISE_ID", merchandiseId);
                        startActivity(intent);
                        Log.e("BuyProduct", "Failed to buy product: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.e("BuyProduct", "Error buying product: " + throwable.getMessage());
                }
            });
        }else {
            Intent intent = new Intent(ProductDetailsActivity.this, BuyProductActivity.class);
            intent.putExtra("MERCHANDISE_ID", merchandiseId);
            startActivity(intent);
        }
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