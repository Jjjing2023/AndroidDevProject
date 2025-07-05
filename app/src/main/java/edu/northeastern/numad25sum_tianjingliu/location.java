package edu.northeastern.numad25sum_tianjingliu;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.LocationRequest;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Locale;

public class location extends AppCompatActivity {

    private TextView coordinates;
    private TextView distance;
    private Button reset;

    private double totalDistance = 0.0;
    private Location previousLocation;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.location);

        coordinates = findViewById(R.id.coordinate);
        distance = findViewById(R.id.distance);
        reset = findViewById(R.id.reset);
        ActivityResultLauncher<String[]> locationPermissionRequest;

        reset.setOnClickListener(v -> {
            totalDistance = 0.0;
            updateDistanceDisplay();
        });

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        updateDistanceDisplay();

        if (savedInstanceState != null) {
            totalDistance = savedInstanceState.getDouble("TOTAL_DISTANCE", 0.0);
            updateDistanceDisplay();
        }

        locationPermissionRequest = registerForActivityResult(
                new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
                    Boolean fineGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                    Boolean coarseGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false);

                    if (Boolean.TRUE.equals(fineGranted) || Boolean.TRUE.equals(coarseGranted)) {
                        startLocationUpdates();
                    } else {
                        Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
        );


        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });


        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(location.this)
                        .setTitle("Exit Location Activity")
                        .setMessage("Your total distance will be lost. Are you sure you want to exit?")
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setMinUpdateIntervalMillis(500)
                .build();


        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;

                for (Location location : locationResult.getLocations()) {
                    updateLocation(location);
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest,
                    locationCallback,
                    Looper.getMainLooper());
        }
    }

    private void updateLocation(Location location) {
        double lat = location.getLatitude();
        double lon = location.getLongitude();
        coordinates.setText("Lat: " + lat + "\nLon: " + lon);

        if (previousLocation != null) {
            float[] results = new float[1];
            Location.distanceBetween(
                    previousLocation.getLatitude(), previousLocation.getLongitude(),
                    lat, lon, results
            );

            float distanceDelta = results[0];

            if (distanceDelta > 1 && distanceDelta < 10) {
                totalDistance += distanceDelta;
                updateDistanceDisplay();
            }
        }

        previousLocation = location;
    }


    private void updateDistanceDisplay() {
        distance.setText(String.format(Locale.US, "Distance: %.2f m", totalDistance));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (locationCallback != null &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            startLocationUpdates();
        }
    }




    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putDouble("TOTAL_DISTANCE", totalDistance);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        totalDistance = savedInstanceState.getDouble("TOTAL_DISTANCE", 0.0);
        updateDistanceDisplay();
    }

}
