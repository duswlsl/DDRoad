package com.seoul.ddroad.map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.seoul.ddroad.R;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMapClickListener {
    private GoogleMap googleMap;
    private LatLng SEOUL = new LatLng(37.56, 126.97);
    private MapView mapView;
    private LatLng curLatlng;
    public ArrayList<LatLng> coordList;
    private PolylineOptions polyOptions;
    private LocationRequest locRequest;
    private FusedLocationProviderClient fusedLocClient;
    private LocationCallback locCallback;
    RelativeLayout relativeLayout;
    Bitmap captureView;

    public MapFragment() {

    }

    protected void createLocationRequest() {
        locRequest = new LocationRequest();
        locRequest.setInterval(1000);
        locRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createLocationRequest();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        Button btn = (Button)view.findViewById(R.id.captureBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                    @Override
                    public void onSnapshotReady(Bitmap bitmap) {
                        screenshot(bitmap);
                    }
                };
                googleMap.snapshot(callback);

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mapView = (MapView)getView().findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
        locCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null)
                    return;
                for (Location location : locationResult.getLocations()) {
                    Log.d("map2", String.valueOf(location.getLatitude()));
                }
                super.onLocationResult(locationResult);
            }
        };
        fusedLocClient = LocationServices.getFusedLocationProviderClient(this.getContext());

        mapView.setDrawingCacheEnabled(true);
        mapView.buildDrawingCache();



        captureView = mapView.getDrawingCache();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        startLocationUpdates();
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocClient.requestLocationUpdates(locRequest, locCallback, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        startLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        fusedLocClient.removeLocationUpdates(locCallback);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (!googleApiCheck())
            return;


        setMarker();
        //this.googleMap.setOnInfoWindowClickListener(this);
        coordList = new ArrayList<LatLng>();
        PolylineOptions polyOptions = new PolylineOptions();
    }

    private boolean googleApiCheck() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this.getContext());
        if (code == ConnectionResult.SUCCESS)
            return true;
        else {
            api.getErrorDialog(this.getActivity(), code, 0).show();
            return false;
        }
    }

    public void setMarker() {
        setLastKnownLoc(this.getContext());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(curLatlng);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 15));
        googleMap.addMarker(markerOptions);

    }

    public void setLastKnownLoc(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Location loc = null;

        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            loc = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        }
        if (loc != null)
            curLatlng = new LatLng(loc.getLatitude(), loc.getLongitude());
        else
            curLatlng = SEOUL;
        Log.d("map", String.valueOf(curLatlng.latitude));
    }

    public void drawPolyline() {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(5);
        //polylineOptions.addall(polyOptions);
        googleMap.addPolyline(polylineOptions);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        // 창 클릭
    }

    @Override
    public void onMapClick(LatLng latLng) {
        // 맵 클릭
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    public void screenshot(Bitmap captureBitmap) {
        FileOutputStream fos;
        File file = new File(this.getContext().getFilesDir(), "CaptureDir"); // 폴더 경로

        if (!file.exists()) {  // 해당 폴더 없으면 만들어라
            file.mkdirs();
        }

        String strFilePath = file + "/" + "test" + ".png";
        File fileCacheItem = new File(strFilePath);
        try {
            fos = new FileOutputStream(fileCacheItem);
            captureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}