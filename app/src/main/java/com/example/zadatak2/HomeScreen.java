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
import androidx.navigation.ui.NavigationUI;

import com.example.zadatak2.databinding.ActivityHomeScreenBinding;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

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

        activityHomeScreenBinding=ActivityHomeScreenBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_home_screen);

        searchInputLayout = findViewById(R.id.search_input_layout);

        navController = findNavController(this, R.id.nav_host_fragment);

        // Link NavigationView with NavController
        NavigationUI.setupWithNavController(navigationView, navController);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);

        rootLayout = findViewById(R.id.drawerLayout);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_drawer);

        // Set up the navigation icon click listener (hamburger icon)
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open or close the drawer when the hamburger icon is clicked
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer if it's open
                    navigationView.setElevation(0);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START); // Open the drawer if it's closed
                    navigationView.setElevation(999);
                }
            }
        });

        // Optional: Handle navigation item clicks
        NavigationView navigationView = findViewById(R.id.navigation_drawer);
        navigationView.setNavigationItemSelectedListener(item -> {
            // Handle item clicks here
            int id = item.getItemId();
            //if (id == R.id.nav_item1) {
            // Handle item1 click
            //}
            drawerLayout.closeDrawer(GravityCompat.START); // Close the drawer after selecting an item
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