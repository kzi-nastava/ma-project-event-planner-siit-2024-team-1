package com.example.EventPlanner.activities;

import static androidx.navigation.Navigation.findNavController;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.EventPlanner.R;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.ActivityHomeScreenBinding;
import com.example.EventPlanner.fragments.eventmerchandise.SearchViewModel;
import com.example.EventPlanner.model.auth.LoginResponse;
import com.example.EventPlanner.model.common.ErrorResponseDto;
import com.example.EventPlanner.model.event.FollowResponse;
import com.example.EventPlanner.model.event.InvitationResponse;
import com.example.EventPlanner.services.WebSocketService;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.annotation.Annotation;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding activityHomeScreenBinding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextInputLayout searchInputLayout;
    private MenuItem searchMenuItem;
    private View rootLayout;

    private SearchViewModel searchViewModel;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Initialize binding and set the root view
        activityHomeScreenBinding = ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(activityHomeScreenBinding.getRoot());  // Use binding root instead of layout resource

        // Initialize views using binding
        searchInputLayout = activityHomeScreenBinding.searchInputLayout;
        rootLayout = activityHomeScreenBinding.drawerLayout;
        drawerLayout = activityHomeScreenBinding.drawerLayout;
        navigationView = activityHomeScreenBinding.navigationDrawer;
        MaterialToolbar toolbar = activityHomeScreenBinding.topAppBar;

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Set up NavController
        //navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        // Set up toolbar and navigation
        setSupportActionBar(toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                    navigationView.setElevation(0);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                    navigationView.setElevation(999);
                }
            }
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            drawerLayout.closeDrawer(GravityCompat.START);
            if(id==R.id.sidebar_top) {
                navController.navigate(R.id.nav_events_merchandise_list_horizontal);
            }
            else if(id==R.id.sidebar_all) {
                Bundle args = new Bundle();
                args.putString("type", "all");
                args.putString("title", getString(R.string.all));
                navController.navigate(R.id.nav_events_merchandise_list_horizontal,args);
            }else if(id==R.id.sidebar_services) {
                navController.navigate(R.id.nav_services_list_vertical);
            }
            else if(id==R.id.sidebar_products) {
                navController.navigate(R.id.nav_products_list_vertical);
            }
            else if(id==R.id.sidebar_event_types) {
                navController.navigate(R.id.nav_event_types_crud);
            }
            else if(id==R.id.sidebar_events) {
                navController.navigate(R.id.nav_events_crud);
            }
            else if(id==R.id.sidebar_reviews) {
                navController.navigate(R.id.nav_reviews);
            }
            else if(id==R.id.sidebar_user_reports) {
                navController.navigate(R.id.nav_user_reports);
            }
            else if(id==R.id.sidebar_messenger) {
                navController.navigate(R.id.nav_messenger);
            }
            else if(id==R.id.sidebar_favorite_events) {
                navController.navigate(R.id.nav_favorite_events);
            }
            else if(id==R.id.sidebar_favorite_merc) {
                navController.navigate(R.id.nav_favorite_merc);
            }
            return true;
        });

        rootLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (searchInputLayout.getVisibility() == View.VISIBLE) {
                    hideSearchInput();
                }
                return false;
            }
        });
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            if(arguments!=null && arguments.getString("title")!=null){
                toolbar.setTitle(arguments.getString("title"));
            }


        });

        searchInputLayout.setEndIconOnClickListener(v -> {
            hideSearchInput();
        });

        TextInputEditText searchEditText = findViewById(R.id.search_edit_text);
        searchViewModel=new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getSearchText().observe(this,searchText->{
            searchEditText.setText(searchText);
            Bundle args = new Bundle();
            args.putString("type", "all");
            args.putString("title", getString(R.string.all));
            navController.navigate(R.id.nav_events_merchandise_list_horizontal,args);
        });

        // Set an OnEditorActionListener
        searchEditText.setOnEditorActionListener((TextView v, int actionId, KeyEvent event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                // Get the text entered by the user
                String searchText = searchEditText.getText().toString().trim();

                // Perform the desired action with the entered text
                searchViewModel.setSearchText(searchText);

                return true; // Return true to consume the event
            }
            return false; // Return false to let the event propagate
        });

        if(JwtService.getEventToken()!=null && !JwtService.getEventToken().isEmpty()){
            JSONObject eventToken=JwtService.decodeToken(JwtService.getEventToken());
            String tokenEmail="";
            String eventTitle="";
            int eventId=-1;
            try {
                tokenEmail= eventToken.getString("userEmail");
                eventId=eventToken.getInt("id");
                eventTitle=eventToken.getString("title");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            if(tokenEmail.equals(JwtService.getEmailFromToken())&&(!tokenEmail.isEmpty())&&(!JwtService.getEmailFromToken().isEmpty())){
                Call<FollowResponse> call = ClientUtils.userService.followEvent(JwtService.getIdFromToken(),eventId);
                String finalEventTitle = eventTitle;
                call.enqueue(new Callback<FollowResponse>() {
                    @Override
                    public void onResponse(Call<FollowResponse> call, Response<FollowResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(HomeScreen.this, "You now follow "+ finalEventTitle, Toast.LENGTH_LONG).show();

                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<FollowResponse> call, Throwable t) {
                        Log.d("onfail",t.getMessage());
                    }
                });
            }

        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout) || super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        searchMenuItem = menu.findItem(R.id.search_icon);

        switch (JwtService.getRoleFromToken()){
            case "AU":
                menu.findItem(R.id.edit_profile).setVisible(false);
                menu.findItem(R.id.deactivate_account).setVisible(false);
                break;
            case "EO":
            case "SP":
                menu.findItem(R.id.promote_eo).setVisible(false);
                menu.findItem(R.id.deactivate_account).setVisible(true);
                menu.findItem(R.id.promote_sp).setVisible(false);
                break;
            case "A":
                menu.findItem(R.id.edit_profile).setVisible(false);
                menu.findItem(R.id.deactivate_account).setVisible(false);
                menu.findItem(R.id.promote_eo).setVisible(false);
                menu.findItem(R.id.promote_sp).setVisible(false);
                break;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.edit_profile) {
            // Handle Notifications click
            String token = JwtService.getAccessToken();
            switch (JwtService.getRoleFromToken()){
                case "SP":
                    Intent intent2 = new Intent(HomeScreen.this, RegisterSpScreen.class);
                    intent2.putExtra("FORM_TYPE", "EDIT_FORM");
                    intent2.putExtra("USER_ID", JwtService.getIdFromToken());
                    startActivity(intent2);
                    break;
                case "EO":
                    Intent intent3 = new Intent(HomeScreen.this, RegisterEoScreen.class);
                    intent3.putExtra("FORM_TYPE", "EDIT_FORM");
                    intent3.putExtra("USER_ID", JwtService.getIdFromToken());
                    startActivity(intent3);
                    break;
            }
            return true;

        }
        else if (itemId == R.id.deactivate_account) {
            // Handle Option 1 click
            Call<Boolean> call1 = ClientUtils.authService.deactivate(JwtService.getIdFromToken());
            call1.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    if (response.isSuccessful() && response.body() != null) {

                    } else {
                        // Parse error response
                        try {
                            Converter<ResponseBody, ErrorResponseDto> converter = ClientUtils.retrofit.responseBodyConverter(
                                    ErrorResponseDto.class, new Annotation[0]);
                            ErrorResponseDto errorResponse = converter.convert(response.errorBody());

                            if (errorResponse != null) {
                                Toast.makeText(HomeScreen.this, errorResponse.getMessage(), Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(HomeScreen.this, "Unknown error occurred", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            Toast.makeText(HomeScreen.this, "Error parsing server response", Toast.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable throwable) {
                    // Handle network errors
                    Toast.makeText(HomeScreen.this, "Network error: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            return true;

        }
        else if (itemId == R.id.promote_eo) {
            // Handle Option 1 click
            Intent intent2 = new Intent(HomeScreen.this, RegisterEoScreen.class);
            intent2.putExtra("FORM_TYPE", "NEW_FORM");
            intent2.putExtra("USER_ID", JwtService.getIdFromToken());
            startActivity(intent2);
            return true;

        }
        else if (itemId == R.id.promote_sp) {
            // Handle Option 1 click
            Intent intent2 = new Intent(HomeScreen.this, RegisterSpScreen.class);
            intent2.putExtra("FORM_TYPE", "NEW_FORM");
            intent2.putExtra("USER_ID", JwtService.getIdFromToken());
            startActivity(intent2);
            return true;

        }
        else if (itemId == R.id.settings) {
            // Handle Option 1 click
            Toast.makeText(this, "Option 1 clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (itemId == R.id.logout) {
            // Handle Option 2 click
            Intent intent = new Intent(this, WebSocketService.class);
            stopService(intent);
            JwtService.logout();
            // Navigate to the home screen
            Intent intent2 = new Intent(HomeScreen.this, LoginScreen.class);
            startActivity(intent2);
            return true;
        }else if (itemId == R.id.search_icon)
        {
            // Show search input and hide search icon
            searchInputLayout.setVisibility(View.VISIBLE);
            searchMenuItem.setVisible(false);

            // Focus on the input field
            searchInputLayout.getEditText().requestFocus();

            // Set focus change listener
            searchInputLayout.getEditText().setOnFocusChangeListener((view, hasFocus) -> {
                if (!hasFocus) {
                    hideSearchInput();
                }
            });

            searchInputLayout.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        hideSearchInput();
                    }
                }
            });

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideSearchInput() {
        searchInputLayout.setVisibility(View.GONE);
        searchMenuItem.setVisible(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}