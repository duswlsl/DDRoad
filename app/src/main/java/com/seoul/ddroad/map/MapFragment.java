package com.seoul.ddroad.map;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.seoul.ddroad.R;

import android.Manifest;
import android.app.Activity;
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
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
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
import java.util.List;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapFragment extends android.app.Fragment implements LocationListener, OnMapReadyCallback {
    private GoogleMap googleMap;
    private LatLng SEOUL = new LatLng(37.56, 126.97);
    private MapView mapView;
    private LatLng curLatlng;
    private ArrayList<LatLng> latLngList;
    private Polyline polyline;
    private LocationRequest locRequest;
    private FusedLocationProviderClient fusedLocClient;
    Bitmap captureView;
    private LocationCallback locCallback, locCallback_walk;
    private Marker marker;


    @BindView(R.id.btn_walk)
    Button btn_walk;

    public MapFragment() {

    }

    protected void createLocationRequest() {
        locRequest = new LocationRequest();
        locRequest.setInterval(2000);
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
        ButterKnife.bind(this, view);

//        Button btn = (Button)view.findViewById(R.id.captureBtn);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
//                    @Override
//                    public void onSnapshotReady(Bitmap bitmap) {
//                        screenshot(bitmap);
//                    }
//                };
//                googleMap.snapshot(callback);
//
//            }
//        });

        return view;
    }

    @OnClick(R.id.btn_walk)
    void startWalk() {
        String state = btn_walk.getTag().toString();
        if (state.equals("OFF")) { //산책 시작
            latLngList = new ArrayList<>();
            btn_walk.setTag("ON");
            btn_walk.setText("끝");
            changeCallback(locCallback, locCallback_walk, true);
        } else { //산책 끝
            btn_walk.setTag("OFF");
            btn_walk.setText("시작");
            changeCallback(locCallback_walk, locCallback, false);

            //다이얼로그
            PolylineDialog dialog = new PolylineDialog();
            Bundle args = new Bundle();
            args.putParcelableArrayList("pointList", latLngList);
            dialog.setArguments(args);
            dialog.setTargetFragment(this, 2);
            dialog.show(getActivity().getFragmentManager(), "tag");
            polyline.remove();

        }
    }


    // 콜백 메소드 교체 (경로 그리기 <-> 마커 업데이트만)
    public void changeCallback(LocationCallback callback1, LocationCallback callback2, boolean isWalk) {
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!isWalk)
            fusedLocClient.removeLocationUpdates(callback1);
        fusedLocClient.requestLocationUpdates(locRequest, callback2, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) { // 경로 저장
            latLngList = data.getExtras().getParcelableArrayList("pointList");
            drawPolyline(latLngList);
            GoogleMap.SnapshotReadyCallback callback = new GoogleMap.SnapshotReadyCallback() {
                @Override
                public void onSnapshotReady(Bitmap bitmap) {
                    screenshot(bitmap);
                }
            };
            googleMap.snapshot(callback);
            Toast.makeText(this.getContext(), "저장되었습니다", Toast.LENGTH_LONG).show();
        }
    }

    public void drawPolyline(List<LatLng> pointList) {
        if (polyline != null)
            polyline.remove();
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.RED);
        polylineOptions.width(15);
        polylineOptions.addAll(pointList);
        polyline = googleMap.addPolyline(polylineOptions);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapView = (MapView) getView().findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(savedInstanceState);
            mapView.getMapAsync(this);
        }
        fusedLocClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        setLocCallback();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
        startLocationUpdates();
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

    //location 콜백메소드 정의
    public void setLocCallback() {
        locCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null)
                    return;
                for (Location location : locationResult.getLocations()) {
                    setCurLatlng(location);
                }
            }
        };

        locCallback_walk = new LocationCallback() { // 산책
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null)
                    return;
                latLngList.add(curLatlng);
                drawPolyline(latLngList);
            }
        };
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocClient.requestLocationUpdates(locRequest, locCallback, null);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        setDefaultLoc(this.getContext());
        setMarker();
    }


    //초기 위치 설정
    public void setDefaultLoc(Context context) {
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
    }


    //위치 바뀔 때
    @Override
    public void onLocationChanged(Location location) {
        curLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        setMarker();
    }

    // set current latlng
    public void setCurLatlng(Location location) {
        curLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        setMarker();
    }

    // update marker position
    public void setMarker() {
        if (marker != null)
            marker.setPosition(curLatlng);
        else {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(curLatlng);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curLatlng, 16));
            marker = googleMap.addMarker(markerOptions);
            marker.showInfoWindow();
        }

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
        Log.d("file", this.getContext().getFilesDir().toString());
        if (!file.exists()) {  // 해당 폴더 없으면 만들어라
            file.mkdirs();
        }

        String strFilePath = file + "/" + "test" + ".png";
        File fileCacheItem = new File(strFilePath);
        try {
            fos = new FileOutputStream(fileCacheItem);
            captureBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            polyline.remove();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}