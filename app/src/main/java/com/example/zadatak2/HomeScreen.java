package com.example.zadatak2;

import static androidx.navigation.Navigation.findNavController;

import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.zadatak2.databinding.ActivityHomeScreenBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.zadatak2.databinding.ActivityHomeScreenBinding;
import com.example.zadatak2.event.Event;
import com.example.zadatak2.event.EventViewModel;
import com.example.zadatak2.eventmerchandise.EventMerchandise;
import com.example.zadatak2.eventmerchandise.EventMerchandiseViewModel;
import com.example.zadatak2.eventmerchandise.EventsMerchandiseAdapter;
import com.example.zadatak2.merchandise.Merchandise;
import com.example.zadatak2.merchandise.MerchandiseViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    ActivityHomeScreenBinding activityHomeScreenBinding;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextInputLayout searchInputLayout;
    private MenuItem searchMenuItem;
    private View rootLayout;
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

    }
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout) || super.onSupportNavigateUp();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        searchMenuItem = menu.findItem(R.id.search_icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.edit_profile) {
            // Handle Notifications click
            Toast.makeText(this, "Notifications clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (itemId == R.id.settings) {
            // Handle Option 1 click
            Toast.makeText(this, "Option 1 clicked", Toast.LENGTH_SHORT).show();
            return true;

        } else if (itemId == R.id.logout) {
            // Handle Option 2 click
            Toast.makeText(this, "Option 2 clicked", Toast.LENGTH_SHORT).show();
            return true;
        }else if (itemId == R.id.search_icon)
        {
            showSearchInput();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSearchInput() {
        // Hide the search icon and show the search input field
        searchMenuItem.setVisible(false);
        searchInputLayout.setVisibility(View.VISIBLE);
        setContentView(R.layout.activity_home_screen);
        // Focus on the search input field
        findViewById(R.id.search_edit_text).requestFocus();

        findViewById(R.id.search_edit_text).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // Hide search input and show the search icon again when focus is lost
                    hideSearchInput();
                }
            }
        });
    }

    private void hideSearchInput() {
        // Hide the search input field and show the search icon
        searchInputLayout.setVisibility(View.GONE);
        searchMenuItem.setVisible(true);
        searchInputLayout.clearFocus();
        // Clear the search text
        findViewById(R.id.search_edit_text).clearFocus();
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