package com.example.habit;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ZoomControls;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.airbnb.lottie.L;
import com.example.habit.databinding.ActivityMapsBinding;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private ActivityMapsBinding binding;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    SharedPreferences sharedPref;

    Button doneButton;
    Button goButton;
    Button mapTypeButton;

    ZoomControls zoomControls;
    PlacesClient placesClient;

    Marker clickMarker;
    LatLng location;
    FusedLocationProviderClient fusedLocationProviderClient;

    Intent incomingIntent;
    Double initialLat;
    Double initialLon;

    // The geographical location where the device is currently located.
    private Location lastKnownLocation;

    // Default location to use if location not provided
    private final LatLng defaultLocation = new LatLng(53.5267, -113.5271);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    boolean locationPermissionGranted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Setup view
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get intent, use 300 as default value to indicate nothing was passed
        incomingIntent = getIntent();
        initialLat = incomingIntent.getDoubleExtra("lat", 300);
        initialLon = incomingIntent.getDoubleExtra("lon", 300);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        placesClient = Places.createClient(this);

        // Get buttons
        doneButton = findViewById(R.id.done);
        mapTypeButton = (Button) findViewById(R.id.btn_Sat);
        goButton = (Button) findViewById(R.id.btn_Go);

        // Get zoom controls
        zoomControls = (ZoomControls) findViewById(R.id.zoom);

        // Get location client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        // Set done button listener
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create intent and add latitude and longitude
                Intent backToFragment = new Intent();

                // Get selected location
                backToFragment.putExtra("lat", location.latitude);
                backToFragment.putExtra("lon", location.longitude);
                MapsActivity.this.setResult(RESULT_OK, backToFragment);
                finish();
            }
        });

        // Set zoom in/out button listeners
        zoomControls.setOnZoomOutClickListener(view -> map.animateCamera(CameraUpdateFactory.zoomOut()));
        zoomControls.setOnZoomInClickListener(view -> map.animateCamera(CameraUpdateFactory.zoomIn()));

        // Set map type button listener
        mapTypeButton.setOnClickListener(view -> {
            if (map.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mapTypeButton.setText("Normal View");
            } else {
                map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mapTypeButton.setText("Satellite View");
            }
        });

        // Set go button listener
        goButton.setOnClickListener(view -> {
            EditText etLocation = (EditText) findViewById(R.id.et_location);
            String location = etLocation.getText().toString();
            if (location != null && !location.equals("")) {
                List<Address> adressList = null;
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                try {
                    adressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = adressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                map.addMarker(new MarkerOptions().position(latLng).title("Location " + location));
                map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Set map reference
        map = googleMap;

        // Turn on location layer
        updateLocationUI();

        // Get current device location and set position on map
        getDeviceLocation(true);

        // Set click listener
        map.setOnMapClickListener(this::onMapClick);
    }

    public void onMapClick(LatLng point) {
        System.out.println("Lat: " + Double.toString(point.latitude) + "| Lon:" + Double.toString(point.longitude));

        // Remove click marker if there is one
        if (clickMarker != null) {
            clickMarker.remove();
        }

        // Add new marker and save it's location
        clickMarker = map.addMarker(new MarkerOptions().position(point).title("Marked Location"));
        location = clickMarker.getPosition();
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation(Boolean initial) {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                @SuppressLint("MissingPermission") Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {

                                if (initial && initialLat != 300 && initialLon != 300) {

                                    // Move to initial location
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(initialLat, initialLon), DEFAULT_ZOOM));

                                    // Set selected location to initial location
                                    location = new LatLng(initialLat, initialLon);

                                    // Set marker to initial location
                                    clickMarker = map.addMarker(new MarkerOptions().position(location).title("Marked Location"));

                                } else {

                                    // Move to current location
                                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(lastKnownLocation.getLatitude(),
                                                    lastKnownLocation.getLongitude()), DEFAULT_ZOOM));

                                    // Set select location to current location
                                    location = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());

                                    // Set marker to current location
                                    clickMarker = map.addMarker(new MarkerOptions().position(location).title("Marked Location"));
                                }
                            }
                        } else {
                            Log.d("LocationError", "Current location is null. Using defaults.");
                            Log.e("LocationError", "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);

                            // Set selection location to the default
                            location = new LatLng(defaultLocation.latitude, defaultLocation.longitude);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        System.out.println("Requesting location perms");
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        updateLocationUI();
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    @SuppressLint("MissingPermission")
    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        try {
            if (locationPermissionGranted) {
                map.setMyLocationEnabled(true);
                map.getUiSettings().setMyLocationButtonEnabled(true);
                System.out.println("Updated map UI for current location");
            } else {
                map.setMyLocationEnabled(false);
                map.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;

                // Ask user for location permissions
                getLocationPermission();
            }
        } catch (Exception e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }
}