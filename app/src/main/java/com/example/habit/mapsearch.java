package com.example.habit;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class mapsearch extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap mMap;
    FloatingActionButton searchb;
    EditText searchmaptext;
    SupportMapFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapsearch);
        searchb=findViewById(R.id.floatingActionButton);
        searchmaptext=findViewById(R.id.searchmapt);
        mapFragment= (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
     //   mapFragment.getMapAsync(this::onMapReady);
      //  mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1,1),15));

        searchb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mapsearch.this,"it work?",Toast.LENGTH_SHORT).show();
               // String location =searchmaptext.getText().toString();
               // Geocoder geocoder=new Geocoder(mapsearch.this, Locale.getDefault());
             //   LatLng latLng=new LatLng(geocoder.getFromLocationName(location,1)[0],geocoder.getFromLocationName(location,1 )[1]);
             //   com.google.android.gms.maps.model.LatLng test2 = new LatLng(2, -2);
            //    mMap.addMarker(new MarkerOptions().position(test2).title("Marker in "));
           //     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1,1),15));
            //    mapFragment.toString();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap=googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-33.87365, 151.20689), 10));
        Toast.makeText(mapsearch.this,"it map work?",Toast.LENGTH_SHORT).show();
        mMap.addMarker(new MarkerOptions().position(new LatLng(1,1)).title("Marker in "));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(1,1),15));

    }
}