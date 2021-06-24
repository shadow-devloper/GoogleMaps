package com.fliprhackathon.googlemap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public void showLocation(View v){
        EditText latitude=(EditText) findViewById(R.id.insertLat);
        EditText longitude=(EditText) findViewById(R.id.insertLng);
        LatLng location= new LatLng(Double.parseDouble(latitude.getText().toString()),Double.parseDouble(longitude.getText().toString()));
        //String uriString = String.format("geo:%f,%f?z=%d",Double.parseDouble(latitude.toString()),Double.parseDouble(longitude.toString()),12);
        String uriString = String.format("geo:%f,%f?z=%d",location.latitude,location.longitude,12);
        Uri intentUri = Uri.parse(uriString);
        Intent intent = new Intent(Intent.ACTION_VIEW,intentUri);
        intent.setPackage("com.google.android.apps.maps");

        if(intent.resolveActivity(getPackageManager())!=null) {
            startActivity(intent);
        }

    }

    public void showDefault(View v) {
        Intent intent=new Intent(this,MapsActivity.class);
        Toast.makeText(getApplicationContext(),"Default set to Jaipur\nLatitude: 26.912434  Longitude: 75.787270",Toast.LENGTH_LONG).show();
        startActivity(intent);
    }
}
