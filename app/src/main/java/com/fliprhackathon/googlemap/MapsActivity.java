package com.fliprhackathon.googlemap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.fliprhackathon.googlemap.databinding.ActivityMapsBinding;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    final int REQUEST_LOCATION_PERMISSION = 500;
    public Marker marker;
    double latitude;
    double longitude;

    //private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        assert mapFragment !=null;

        mapFragment.getMapAsync(this);

        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAllertMessageNoGps();
        }

        /*
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

         */
    }

    private void buildAllertMessageNoGps() {
        new AlertDialog.Builder(this)
                .setMessage("Your GPS is disabled, please enable it...")
                .setCancelable(false)
                .setPositiveButton("OK",(dialog, id) ->{
                    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                })
                .setNegativeButton("Cancel",(dialog, id)->{ finish(); })
                .show();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(true);
        LatLng jaipur = new LatLng(26.912434, 75.787270);
        latitude = jaipur.latitude;
        longitude= jaipur.longitude;
        mMap.addMarker(new MarkerOptions()
                .position(jaipur)
                .draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(jaipur));
        enableMylocation();
        //marker = mMap.addMarker(new MarkerOptions().draggable(true));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull @org.jetbrains.annotations.NotNull Marker marker) {
                Toast.makeText(getApplicationContext(),"Drag started", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onMarkerDrag(@NonNull @org.jetbrains.annotations.NotNull Marker marker) {
                Toast.makeText(getApplicationContext(),"Dragging", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onMarkerDragEnd(@NonNull @org.jetbrains.annotations.NotNull Marker marker) {
                Toast.makeText(getApplicationContext(),"Drag ended", Toast.LENGTH_SHORT).show();
                LatLng latLng =marker.getPosition();
                latitude = latLng.latitude;
                longitude= latLng.longitude;
                Toast.makeText(getApplicationContext(),"Latitude: "+latitude+"\nLongitude: "+longitude,Toast.LENGTH_LONG).show();

            }
        });

        /*
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            public View getInfoWindow(@NonNull @NotNull Marker marker) {
                return null;
            }

            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            public View getInfoContents(@NonNull @NotNull Marker marker) {
                View v =getLayoutInflater().inflate(R.layout.info_window,null);
                LatLng latLng=marker.getPosition();
                TextView title = (TextView)v.findViewById(R.id.title);
                TextView snippet = (TextView)v.findViewById(R.id.snippet);
                TextView lat = (TextView)v.findViewById(R.id.lat);
                TextView lng = (TextView)v.findViewById(R.id.lng);

                //title.setText(marker.getTitle());
                //snippet.setText(marker.getSnippet());
                lat.setText("Latitude: "+ latLng.latitude);
                lng.setText("Longitude:"+ latLng.longitude);


                return v;
            }
        });

         */
    }


    private void enableMylocation() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        else {
            ActivityCompat.requestPermissions(this,new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                          @NonNull String[] permissions,
                                          @NonNull int[] grantResult) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if(grantResult.length>0
                        && grantResult[0]== PackageManager.PERMISSION_GRANTED) {
                    enableMylocation();
                    break;
                }

            default:
                super.onRequestPermissionsResult(requestCode,permissions,grantResult);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Display menu to user
        switch(item.getItemId()) {
            case R.id.normal_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                return true;
            case R.id.satellite_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                return true;
            case R.id.terrain_map:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    /*
    public void sendLocation(View v){
        double lat = marker.getPosition().latitude;
        double lng =marker.getPosition().longitude;

        Intent intent = new Intent(MapsActivity.this,Loc.class);
        intent.putExtra("Latitide",lat);
        intent.putExtra("Longitude",lng);
        startActivity(intent);



    }

     */
    public void getCoordinates(View v){
        //Location location = new Location(LocationManager.GPS_PROVIDER);
        //location.setLatitude(latitude);
        //location.setLongitude(longitude);
        new AlertDialog.Builder(this)
                .setTitle("Location")
                .setMessage("Latitude :" + latitude + "\nLongitude :" + longitude)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
        }

    public void getLocation(View v){
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        if(location!=null) {
            getAddress(location);
        }
    }

    public void getAddress(Location location) {
        if(Geocoder.isPresent()) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            String addressText = null;
            latitude = location.getLatitude();
            longitude=location.getLongitude();
            try{
                addresses=geocoder.getFromLocation(latitude,longitude,1);
            } catch (IOException|IllegalArgumentException e) {
                e.printStackTrace();
            }
            if(addresses!=null&&addresses.size()>0) {
                Address address = addresses.get(0);
                addressText = String.format(
                        "%s, %s, %s, %s",address.getMaxAddressLineIndex()>0 ? address.getAddressLine(0) : "",
                        address.getLocality(),
                        address.getCountryName(),
                        address.getPostalCode()
                );
            }
            new AlertDialog.Builder(this)
                    .setTitle("Location")
                    .setMessage(addressText)
            .show();

        } else {
            Toast.makeText(this,"Geocoder could not be implemented",Toast.LENGTH_LONG).show();
        }
    }







}