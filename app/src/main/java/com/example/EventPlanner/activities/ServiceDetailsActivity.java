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
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.merchandise.PhotoSliderAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.ActivityServiceDetailsBinding;
import com.example.EventPlanner.fragments.common.review.MerchandiseReviewList;
import com.example.EventPlanner.fragments.merchandise.MerchandiseViewModel;
import com.example.EventPlanner.fragments.user.UserOverviewViewModel;
import com.example.EventPlanner.model.merchandise.MerchandiseDetailsDTO;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;
import com.example.EventPlanner.model.user.GetEoById;
import com.example.EventPlanner.model.user.GetSpById;
import com.example.EventPlanner.model.user.ServiceProvider;
import com.example.EventPlanner.services.WebSocketService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceDetailsActivity extends AppCompatActivity {
    private int merchandiseId;
    private ActivityServiceDetailsBinding activityServiceDetailsBinding;
    private MerchandiseViewModel merchandiseViewModel;

    private boolean isFavorited = false; // Default state
    private ImageButton starButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        activityServiceDetailsBinding=ActivityServiceDetailsBinding.inflate(getLayoutInflater());
        merchandiseViewModel = new ViewModelProvider(this).get(MerchandiseViewModel.class);
        setContentView(activityServiceDetailsBinding.getRoot());
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
                    Button openMessengerButton = activityServiceDetailsBinding.messengerButton;
                    openMessengerButton.setOnClickListener(v -> openMessenger(merchandise.getServiceProviderId()));
                }
            });
            merchandiseViewModel.merchandiseDetails(merchandiseId);
        }

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

        Button bookReservationBtn=activityServiceDetailsBinding.bookReservationBtn;
        bookReservationBtn.setOnClickListener(v->openBookReservation());
        Log.d("merchandise_id", String.valueOf(merchandiseId));
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
            activityServiceDetailsBinding.serviceTitle.setText(messageStr);
            activityServiceDetailsBinding.favoriteButton.setVisibility(View.GONE);
            activityServiceDetailsBinding.locationIcon.setVisibility(View.GONE);
            activityServiceDetailsBinding.serviceReservation.setVisibility(View.GONE);
            activityServiceDetailsBinding.profileIcon.setVisibility(View.GONE);
            activityServiceDetailsBinding.messengerButton.setVisibility(View.GONE);
            return;
        }

        if(isFavorite(merchandise.getId())) {
            activityServiceDetailsBinding.favoriteButton.setImageResource(R.drawable.ic_star_filled);
        }else {
            activityServiceDetailsBinding.favoriteButton.setImageResource(R.drawable.ic_star_border);
        }

        activityServiceDetailsBinding.serviceReservation.setEnabled(merchandise.getAvailable());

        if(merchandise.getMerchandisePhotos() != null && !merchandise.getMerchandisePhotos().isEmpty()) {
            PhotoSliderAdapter photosAdapter = new PhotoSliderAdapter(this, merchandise.getMerchandisePhotos());
            activityServiceDetailsBinding.merchandiseImages.setAdapter(photosAdapter);
        }
        activityServiceDetailsBinding.serviceTitle.setText(merchandise.getTitle());
        activityServiceDetailsBinding.serviceCategory.setText(merchandise.getCategory().getTitle());
        String address = merchandise.getAddress().getStreet() + " " + merchandise.getAddress().getNumber() + ", " + merchandise.getAddress().getCity();
        activityServiceDetailsBinding.serviceAddress.setText(address);

        if(merchandise.getDiscount() > 0) {
            String price = merchandise.getPrice() + "€";
            SpannableString spannebleString = new SpannableString(price);

            spannebleString.setSpan(new StrikethroughSpan(), 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannebleString.setSpan(new ForegroundColorSpan(Color.RED), 0, price.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            activityServiceDetailsBinding.servicePrice.setText(spannebleString);
            double discountedPrice = merchandise.getPrice() - (merchandise.getPrice()*merchandise.getDiscount())/100;
            String discountedPriceStr = discountedPrice + "€";
            activityServiceDetailsBinding.serviceDiscountedPrice.setText(discountedPriceStr);
        }else {
            String price = String.valueOf(merchandise.getPrice()) + "€";
            activityServiceDetailsBinding.servicePrice.setText(price);
        }

        String duration = merchandise.getMinDuration() + "-" + merchandise.getMaxDuration();
        activityServiceDetailsBinding.serviceDuration.setText(duration);
        String reservationDeadline = "Reservation Deadline: " + merchandise.getReservationDeadline();
        activityServiceDetailsBinding.serviceReservation.setText(reservationDeadline);
        String cancellationDeadline = "Cancellation Deadline: " + merchandise.getCancellationDeadline();
        activityServiceDetailsBinding.serviceCancellation.setText(cancellationDeadline);
        activityServiceDetailsBinding.serviceDescription.setText(merchandise.getDescription());
        activityServiceDetailsBinding.serviceSpecificity.setText(merchandise.getSpecificity());

        Call<GetSpById> spCall = ClientUtils.userService.getSpById(merchandise.getServiceProviderId());
        spCall.enqueue(new Callback<GetSpById>() {
            @Override
            public void onResponse(Call<GetSpById> call, Response<GetSpById> response) {
                if(response.isSuccessful() && response.body() != null) {
                    String serviceProvider = response.body().getName() + " " + response.body().getSurname();
                    activityServiceDetailsBinding.serviceProviderName.setText(serviceProvider);
                }else {
                    String serviceProvider = "Service Provider";
                    activityServiceDetailsBinding.serviceProviderName.setText(serviceProvider);
                }
            }
            @Override
            public void onFailure(Call<GetSpById> call, Throwable throwable) {
                String serviceProvider = "Service Provider";
                activityServiceDetailsBinding.serviceProviderName.setText(serviceProvider);
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

    private boolean isFavorite = false;
    private boolean isFavorite(int merchandiseId) {
        //TODO: implement logic if merchandise is in List of favorite merchandises in user
        return isFavorite;
    }

    private void setMerchandiseAsFavorite(int merchandiseId) {
        if(isFavorite(merchandiseId)) {
            this.isFavorite = false;
            activityServiceDetailsBinding.favoriteButton.setImageResource(R.drawable.ic_star_border);
        }else {
            this.isFavorite = true;
            activityServiceDetailsBinding.favoriteButton.setImageResource(R.drawable.ic_star_filled);
        }
    }

    private void openBookReservation(){
        Intent intent = new Intent(ServiceDetailsActivity.this, BookReservationActivity.class);
        intent.putExtra("MERCHANDISE_ID", merchandiseId);
        startActivity(intent);
    }

    private void openMessenger(int merchandiseServiceProviderId) {
        if(merchandiseServiceProviderId != -1) {
            Intent intent = new Intent(ServiceDetailsActivity.this, MessengerActivity.class);
            intent.putExtra("USER_ID", merchandiseServiceProviderId);
            startActivity(intent);
        }
    }
}