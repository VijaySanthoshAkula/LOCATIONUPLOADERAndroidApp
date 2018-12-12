package com.example.avsanthoshkumar.locationuploader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class UploadGpsActivity extends AppCompatActivity {
    private final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;
    private final int MY_PERMISSIONS_REQUEST_COARSE_LOCATION = 1;
    Button startSending,getperm;
    loginDatabaseHelper myDb;
    Location location;
    double longitude, latitude;
    private LocationListener locationListener;
    private LocationManager locationManager;
    String outside;
    View myLayout;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Student");
    ArrayList<Point1> polygonVertices = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_gps);
        startSending = findViewById(R.id.upload);
    //    startSending.setEnabled(false);


        polygonVertices.add(new Point1(12.845423, 77.662578 ));
        polygonVertices.add(new Point1(12.845046, 77.662561 ));
        polygonVertices.add(new Point1(12.844994, 77.662346));
        polygonVertices.add(new Point1(12.844126, 77.662282));
        polygonVertices.add(new Point1(12.843979, 77.662679));
        polygonVertices.add(new Point1(12.843571, 77.662550));
        polygonVertices.add(new Point1(12.843132, 77.663451));
        polygonVertices.add(new Point1(12.843697, 77.663644));
        polygonVertices.add(new Point1(12.843760, 77.663333));
        polygonVertices.add(new Point1(12.844147, 77.663419));
        polygonVertices.add(new Point1(12.844100, 77.663725));
        polygonVertices.add(new Point1(12.844116, 77.664165));
        polygonVertices.add(new Point1(12.845372, 77.664192));
        polygonVertices.add(new Point1(12.845409, 77.663178));

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LayoutInflater myInflator = getLayoutInflater();
        myLayout = myInflator.inflate(R.layout.customtoast, (ViewGroup) findViewById(R.id.toastlayout));
       // onClickSetPermissions();
        onClickListenerPublishLocation();
    }

    /*private void onClickSetPermissions() {
        getperm = findViewById(R.id.perm);
        getperm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissions(displayGpsStatus());
                if(displayGpsStatus()) {
                    startSending.setEnabled(true);
                }
            }
        });
    }*/


    public void permissions(boolean flag) {
        if (flag) {

           locationListener = new MyLocationListener();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(UploadGpsActivity.this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UploadGpsActivity.this, new String [] {Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
                }else{
                    /*Criteria locationCritera = new Criteria();
                    String providerName = locationManager.getBestProvider(locationCritera,
                            true);
                    if(providerName!=null) {
                        location = locationManager.getLastKnownLocation(providerName);
                        updateGPSCoordinates();
                    }

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                            0, locationListener);*/
                    locationManager.requestLocationUpdates(locationManager
                            .GPS_PROVIDER, 1000, 0, locationListener);
                    if (locationManager != null) {
                        Log.d("Entering_here11",locationManager.toString());
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.d("Entering_here11",locationManager.toString());
                        updateGPSCoordinates();
                    }
                }
            }
            else{
                if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(UploadGpsActivity.this, new String [] {Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_COARSE_LOCATION);
                }else{
                    /*Criteria locationCritera = new Criteria();
                    String providerName = locationManager.getBestProvider(locationCritera,
                            true);
                    if(providerName!=null) {
                        location = locationManager.getLastKnownLocation(providerName);
                        updateGPSCoordinates();
                    }

                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000,
                            0, locationListener);*/

                  locationManager.requestLocationUpdates(locationManager
                            .GPS_PROVIDER, 1000, 0, locationListener);
                    if (locationManager != null) {
                        Log.d("Entering_here1",locationManager.toString());
                        location = locationManager
                                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.d("Entering_here1",locationManager.toString());
                        updateGPSCoordinates();

                    }
                }
            }

        }else{
            TextView text = (TextView) myLayout.findViewById(R.id.text);
            text.setText("GPS is off please turn it on");
            Toast myToast=new Toast(UploadGpsActivity.this);
            myToast.setDuration(Toast.LENGTH_SHORT);
            myToast.setView(myLayout);
            myToast.show();
        }
    }
    public void onClickListenerPublishLocation() {

        myDb=new loginDatabaseHelper(this);
        boolean flag = displayGpsStatus();

        startSending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissions(displayGpsStatus());
                if(displayGpsStatus()){
                    if(latitude==0.0){
                        TextView text = (TextView) myLayout.findViewById(R.id.text);
                        text.setText("Gps Coordinates problem press the Upload button again");
                        Toast myToast=new Toast(UploadGpsActivity.this);
                        myToast.setDuration(Toast.LENGTH_LONG);
                        myToast.setView(myLayout);
                        myToast.show();
                        Log.d("Gpslongitude", Double.toString(longitude));
                    }else{
                        TextView text = (TextView) myLayout.findViewById(R.id.text);
                        text.setText("Uploading your GPS location to server please dont kill the App keep it in background...");
                        Toast myToast=new Toast(UploadGpsActivity.this);
                        myToast.setDuration(Toast.LENGTH_LONG);
                        myToast.setView(myLayout);
                        myToast.show();
                        Cursor res = myDb.getAllData();
                        res.moveToNext();
                        final String sIdNumber=res.getString(4).toString();
                        Log.d("sIDNumber", sIdNumber);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                RaycastHelper ray=new RaycastHelper();
                                if(ray.isPointInside(polygonVertices,new Point1(latitude,longitude))){
                                    myRef.child(sIdNumber).child("lat").setValue(Double.toString(latitude));
                                    myRef.child(sIdNumber).child("lng").setValue(Double.toString(longitude));
                                    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                    Date date = new Date();
                                    myRef.child(sIdNumber).child("time").setValue(dateFormat.format(date));
                                    myRef.child(sIdNumber).child("outside").setValue("no");
                                }else{
                                    DatabaseReference ref= myRef.child(sIdNumber).child("outside");
                                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            outside = dataSnapshot.getValue(String.class);
                                            Log.d("outsidevalue", outside);
                                            if(outside.equals("no")){
                                                myRef.child(sIdNumber).child("lat").setValue(Double.toString(latitude));
                                                myRef.child(sIdNumber).child("lng").setValue(Double.toString(longitude));
                                                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                                                Date date = new Date();
                                                myRef.child(sIdNumber).child("time").setValue(dateFormat.format(date));
                                                myRef.child(sIdNumber).child("outside").setValue("yes");
                                            }else{
                                                myRef.child(sIdNumber).child("lat").setValue(Double.toString(latitude));
                                                myRef.child(sIdNumber).child("lng").setValue(Double.toString(longitude));
                                            }
                                        }
                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {}
                                    });

                                }


                                Log.d("Gpslongitude", Double.toString(longitude));
                                Log.d("Gpslatitude", Double.toString(latitude));
                            }
                        }, 0,2000);

                    }

            }}
        });

    }

    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            longitude = loc.getLongitude();
            latitude = loc.getLatitude();
           // location=loc;
        }
        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        } }
    public void updateGPSCoordinates() {
        if (location != null) {
            Log.d("Entering_here",location.toString());
            Log.d("lat_here",Double.toString(location.getLatitude()));
            Log.d("lat_here",Double.toString(location.getLongitude()));
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }
}
