package com.example.EventPlanner;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.EventPlanner.databinding.ActivityHomeScreenBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

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
            }else if(id==R.id.sidebar_services) {
                navController.navigate(R.id.nav_services_list_vertical);
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