package com.example.EventPlanner.fragments.common.map;

// MapFragment.java


import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.EventPlanner.R;
import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;

import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements MapEventsReceiver {
    private CustomMapView mapView;
    private MapViewModel viewModel;
    private static final String ARG_SHOW_EVENTS = "show_events";
    private static final String ARG_SHOW_MERCHANDISE = "show_merchandise";
    private static final String ARG_ENABLE_ADDRESS_DETECTION = "enable_address_detection";
    private boolean showEvents = true;
    private boolean showMerchandise = true;
    private boolean enableAddressDetection = false;
    private OnMapAddressSelectedListener addressListener;
    private static final double DEFAULT_LATITUDE = 45.25710603831593;
    private static final double DEFAULT_LONGITUDE = 19.84540080257916;

    public interface OnMapAddressSelectedListener {
        void onAddressSelected(Address address);
    }

    // Add color resources to res/values/colors.xml
    // <color name="marker_event">#FF4081</color>      // Pink for events
    // <color name="marker_product">#2196F3</color>    // Blue for products
    // <color name="marker_service">#4CAF50</color>    // Green for services

    public static MapFragment newInstance(boolean showEvents, boolean showMerchandise, boolean enableAddressDetection) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_SHOW_EVENTS, showEvents);
        args.putBoolean(ARG_SHOW_MERCHANDISE, showMerchandise);
        args.putBoolean(ARG_ENABLE_ADDRESS_DETECTION, enableAddressDetection);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            showEvents = getArguments().getBoolean(ARG_SHOW_EVENTS, true);
            showMerchandise = getArguments().getBoolean(ARG_SHOW_MERCHANDISE, true);
            enableAddressDetection = getArguments().getBoolean(ARG_ENABLE_ADDRESS_DETECTION, false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.getController().setZoom(15.0);

        // Add map click overlay if address detection is enabled
        if (enableAddressDetection) {
            MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this);
            mapView.getOverlays().add(0, mapEventsOverlay); // Add at index 0 to handle clicks first
        }

        viewModel = new ViewModelProvider(requireActivity()).get(MapViewModel.class);

        if (showEvents) {
            viewModel.getEvents().observe(getViewLifecycleOwner(), this::displayEventMarkers);
        }

        if (showMerchandise) {
            viewModel.getMerchandise().observe(getViewLifecycleOwner(), this::displayMerchandiseMarkers);
        }

        if (!showEvents && !showMerchandise) {
            // Set default position when both showEvents and showMerchandise are false
            GeoPoint defaultPoint = new GeoPoint(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
            mapView.getController().setCenter(defaultPoint);
            mapView.invalidate();
        }
    }

    public void setOnMapAddressSelectedListener(OnMapAddressSelectedListener listener) {
        this.addressListener = listener;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint point) {
        if (enableAddressDetection && addressListener != null) {
            getAddressFromPoint(point);
        }
        return true;
    }
    @Override
    public boolean longPressHelper(GeoPoint point) {
        return false;
    }
    private void getAddressFromPoint(GeoPoint point) {
        if (!Geocoder.isPresent()) {
            return;
        }

        Thread thread = new Thread(() -> {
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            try {
                List<android.location.Address> addresses = geocoder.getFromLocation(
                        point.getLatitude(),
                        point.getLongitude(),
                        1
                );

                if (addresses != null && !addresses.isEmpty()) {
                    android.location.Address androidAddress = addresses.get(0);

                    // Create your custom Address object
                    Address address = new Address();
                    address.setLatitude(point.getLatitude());
                    address.setLongitude(point.getLongitude());
                    address.setStreet(androidAddress.getThoroughfare());
                    address.setCity(androidAddress.getLocality());
                    address.setNumber(androidAddress.getSubThoroughfare());
                    mapView.getOverlays().removeIf(overlay -> overlay instanceof Marker);
                    Marker marker = createMarker(
                            point,
                            "",
                            "",
                            R.color.marker_default
                    );

                    mapView.getOverlays().add(marker);
                    // Notify on main thread
                    requireActivity().runOnUiThread(() -> {
                        addressListener.onAddressSelected(address);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
    private Marker createMarker(GeoPoint point, String title, String snippet, int colorResId) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setTitle(title);
        marker.setSnippet(snippet);

        // Create a colored icon for the marker
        Drawable icon = ContextCompat.getDrawable(requireContext(), org.osmdroid.library.R.drawable.marker_default);
        if (icon != null) {
            icon.setTint(ContextCompat.getColor(requireContext(), colorResId));
            marker.setIcon(icon);
        }

        marker.setOnMarkerClickListener((m, mapView) -> {
            m.showInfoWindow();
            return true;
        });

        return marker;
    }

    private void displayEventMarkers(List<EventOverview> events) {
        if (events == null) return;

        // Remove existing markers
        mapView.getOverlays().removeIf(overlay -> overlay instanceof Marker);

        for (EventOverview event : events) {
            if (event.getAddress() != null &&
                    event.getAddress().getLatitude() != null &&
                    event.getAddress().getLongitude() != null) {

                GeoPoint point = new GeoPoint(
                        event.getAddress().getLatitude(),
                        event.getAddress().getLongitude()
                );

                Marker marker = createMarker(
                        point,
                        event.getTitle(),
                        event.getType(),
                        R.color.marker_event  // Pink color for events
                );

                mapView.getOverlays().add(marker);
            }
        }

        if (!events.isEmpty() && events.get(0).getAddress() != null) {
            mapView.getController().setCenter(new GeoPoint(
                    events.get(0).getAddress().getLatitude(),
                    events.get(0).getAddress().getLongitude()
            ));
        }

        mapView.invalidate();
    }

    private void displayMerchandiseMarkers(List<MerchandiseOverview> merchandise) {
        if (merchandise == null) return;

        for (MerchandiseOverview item : merchandise) {
            if (item.getAddress() != null &&
                    item.getAddress().getLatitude() != null &&
                    item.getAddress().getLongitude() != null) {

                GeoPoint point = new GeoPoint(
                        item.getAddress().getLatitude(),
                        item.getAddress().getLongitude()
                );

                // Choose color based on merchandise type
                int colorResId = "Product".equalsIgnoreCase(item.getType())
                        ? R.color.marker_product    // Blue for products
                        : R.color.marker_service;   // Green for services

                Marker marker = createMarker(
                        point,
                        item.getTitle(),
                        String.format("%s - $%.2f", item.getType(), item.getPrice()),
                        colorResId
                );

                mapView.getOverlays().add(marker);
            }
        }

        mapView.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
}