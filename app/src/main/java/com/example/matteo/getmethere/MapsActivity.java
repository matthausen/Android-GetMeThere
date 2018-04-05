package com.example.matteo.getmethere;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    Spinner spinner;
    ArrayAdapter<CharSequence> adapter;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        spinner = (Spinner)findViewById(R.id.dropdown);
        adapter = ArrayAdapter.createFromResource(this, R.array.transport,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getBaseContext(), parent.getItemAtPosition(position) + " was selected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

        /** Add a marker in London and move the camera */
        LatLng london = new LatLng(51.5074, 0.1278);
        mMap.addMarker(new MarkerOptions().position(london).title("London"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(london));
    }


    /*Create a method onClick*/
    public void onClick(View v)
    {
        /* First clear the map of all previous markers */
        mMap.clear();

        /*Convert user input to String and add to a list of address*/
        EditText origin_tf = (EditText)findViewById(R.id.start);
        String origin = origin_tf.getText().toString();
        List<Address> addressList = null;
        if(origin != null || !origin.equals(""))
        {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(origin , 1);


            } catch (IOException e) {
                e.printStackTrace();
            }

            /* Get lat and long from address and set a marker on the map */
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude() , address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Origin"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            /* Now the same for the destination */
            EditText destination_tf = (EditText)findViewById(R.id.end);
            String destination = destination_tf.getText().toString();
            List<Address> addressList2 = null;
            if(destination != null || !destination.equals("")) {
                Geocoder geocoder2 = new Geocoder(this);
                try {
                    addressList2 = geocoder.getFromLocationName(destination, 1);


                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address2 = addressList2.get(0);
                LatLng latLng2 = new LatLng(address2.getLatitude(), address2.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng2).title("Destination"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng2));

                /* Create variable to store lat and long of origin and destination */
                double startLatitude = address.getLatitude();
                double startLongitude = address.getLongitude();
                double endLatitude = address2.getLatitude();
                double endLongitude = address2.getLongitude();

                /* Use distanceBetween Google method to calculate distance between 2 points on mao in meters*/
                float results[] = new float[10];
                Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);

                /* Display distance converted in miles and rounded on TextView label */
                float distanceMeters = results[0];
                double distanceMiles = Math.floor((distanceMeters * 0.00062)*100) / 100;


                TextView distance = (TextView) findViewById(R.id.distance);
                distance.setText("Distance = " + distanceMiles + " miles.");

                /* Create variables to hold speed of transports*/
                int boeingSpeed = 583;
                int ostrichSpeed = 45;
                int soundSpeed = 767;
                int horseSpeed = 30;
                int walkingSpeed = 3;
                int shuttleSpeed = 17500;

                /* And variables to store the travel duration */
                double boeingDuration = Math.floor((distanceMiles/boeingSpeed)*100)/100;
                double ostrichDuration = Math.floor((distanceMiles/ostrichSpeed)*100)/100;
                double soundDuration = Math.floor((distanceMiles/soundSpeed)*100)/100;
                double horseDuration = Math.floor((distanceMiles/horseSpeed)*100)/100;
                double walkingDuration = Math.floor((distanceMiles/walkingSpeed)*100)/100;
                double shuttleDuration = Math.floor((distanceMiles/shuttleSpeed)*100)/100;

                /* Display duration adapted to sec, min, hrs or days on TextView label */
                TextView duration = (TextView) findViewById(R.id.duration);
                switch (String.valueOf(spinner.getSelectedItem()))
                {
                    case ("Boeing 737"):
                        if (boeingDuration < 0.01){
                            duration.setText("Duration = "  + (boeingDuration*60)*60 + " seconds");
                        }
                        else if (boeingDuration < 1 && boeingDuration >= 0.01){
                            duration.setText("Duration = "  + (boeingDuration*60) + " minutes");
                        }
                        else if(boeingDuration <= 24 && boeingDuration >=1){
                            duration.setText("Duration = "  + boeingDuration + " hours");
                        }
                        else if (boeingDuration > 24){
                            duration.setText("Duration = "  + (boeingDuration*0.0416667) + " days");
                        }
                        break;
                    case ("Ostrich"):
                        if (ostrichDuration < 0.01){
                            duration.setText("Duration = "  + (ostrichDuration*60)*60 + " seconds");
                        }
                        else if (ostrichDuration < 1 && ostrichDuration >= 0.01){
                            duration.setText("Duration = "  + (ostrichDuration*60) + " minutes");
                        }
                        else if(ostrichDuration <= 24 && ostrichDuration >=1){
                            duration.setText("Duration = "  + ostrichDuration + " hours");
                        }
                        else if (ostrichDuration > 24){
                            duration.setText("Duration = "  + (ostrichDuration*0.0416667) + " days");
                        }
                        break;
                    case ("Speed of sound"):
                        if (soundDuration < 0.01){
                            duration.setText("Duration = "  + (soundDuration*60)*60 + " seconds");
                        }
                        else if (soundDuration < 1 && soundDuration >= 0.01){
                            duration.setText("Duration = "  + (soundDuration*60) + " minutes");
                        }
                        else if(soundDuration <= 24 && soundDuration >=1){
                            duration.setText("Duration = "  + soundDuration + " hours");
                        }
                        else if (soundDuration > 24){
                            duration.setText("Duration = "  + (soundDuration*0.0416667) + " days");
                        }
                        break;
                    case ("Horse"):
                        if (horseDuration < 0.01){
                            duration.setText("Duration = "  + (horseDuration*60)*60 + " seconds");
                        }
                        else if (horseDuration < 1 && horseDuration >= 0.01){
                            duration.setText("Duration = "  + (horseDuration*60) + " minutes");
                        }
                        else if(horseDuration <= 24 && horseDuration >=1){
                            duration.setText("Duration = "  + horseDuration + " hours");
                        }
                        else if (horseDuration > 24){
                            duration.setText("Duration = "  + (horseDuration*0.0416667) + " days");
                        }
                        break;
                    case ("Walking"):
                        if (walkingDuration < 0.01){
                            duration.setText("Duration = "  + (walkingDuration*60)*60 + " seconds");
                        }
                        else if (walkingDuration < 1 && walkingDuration >= 0.01){
                            duration.setText("Duration = "  + (walkingDuration*60) + " minutes");
                        }
                        else if(walkingDuration <= 24 && walkingDuration >=1){
                            duration.setText("Duration = "  + walkingDuration + " hours");
                        }
                        else if (walkingDuration > 24){
                            duration.setText("Duration = "  + (walkingDuration*0.0416667) + " days");
                        }
                        break;
                    case ("Space Shuttle"):
                        if (shuttleDuration < 0.01){
                            duration.setText("Duration = "  + (shuttleDuration*60)*60 + " seconds");
                        }
                        else if (shuttleDuration < 1 && shuttleDuration >= 0.01){
                            duration.setText("Duration = "  + (shuttleDuration*60) + " minutes");
                        }
                        else if(shuttleDuration <= 24 && shuttleDuration >=1){
                            duration.setText("Duration = "  + shuttleDuration + " hours");
                        }
                        else if (shuttleDuration > 24){
                            duration.setText("Duration = "  + (shuttleDuration*0.0416667) + " days");
                        }
                        break;

                }

                /*Make the Pop Up Windows visible on click */
                View view = (View) findViewById(R.id.result);
                view.setVisibility(View.VISIBLE);

                /* Display a popup message if user selects Space Shuttle */
                if (String.valueOf(spinner.getSelectedItem()).equals("Space Shuttle")) {
                    Toast.makeText(MapsActivity.this, "Too Fast!!!", Toast.LENGTH_SHORT).show();

                }

            }

        }
    }
}
