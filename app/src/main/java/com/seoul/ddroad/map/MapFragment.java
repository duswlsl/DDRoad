package com.seoul.ddroad.map;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {
    private static String TAG = MapFragment.class.getSimpleName();
    private GoogleMap googleMap;
    private LatLng SEOUL = new LatLng(37.56, 126.97);
    private MapView mapView;
    private LatLng curLatlng;
    private ArrayList<LatLng> latLngList;
    private Polyline polyline;
    private LocationRequest locRequest;
    private FusedLocationProviderClient fusedLocClient;
    private LocationCallback locCallback, locCallback_walk;
    private Marker marker;
    private HashMap<Marker, Data> markerMap = new HashMap<>();


    @BindView(R.id.btn_walk)
    Button btn_walk;
    @BindView(R.id.btn_cafe)
    Button btn_cafe;
    @BindView(R.id.btn_hospital)
    Button btn_hospital;
    @BindView(R.id.btn_hotel)
    Button btn_hotel;
    @BindView(R.id.btn_salon)
    Button btn_salon;
    @BindView(R.id.btn_trail)
    Button btn_trail;
    @BindView(R.id.btn_all)
    Button btn_all;

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

        return view;
    }

    @OnClick(R.id.btn_walk)
    void startWalk() {
        String state = btn_walk.getTag().toString();
        if (state.equals("OFF")) { //산책 시작
            latLngList = new ArrayList<>();
            btn_walk.setTag("ON");
            //btn_walk.setText("끝");
            btn_walk.setBackgroundResource(R.drawable.btn_end);
            changeCallback(locCallback, locCallback_walk, true);
        } else { //산책 끝
            btn_walk.setTag("OFF");
            //btn_walk.setText("시작");
            btn_walk.setBackgroundResource(R.drawable.btn_walk);
            changeCallback(locCallback_walk, locCallback, false);

            //다이얼로그
            PolylineDialog dialog = new PolylineDialog();
            Bundle args = new Bundle();
            args.putParcelableArrayList("pointList", latLngList);
            dialog.setArguments(args);
            dialog.setTargetFragment(this, 2);
            dialog.show(getActivity().getSupportFragmentManager(), "tag");
            if (polyline != null)
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
        googleMap.setOnInfoWindowClickListener(this);
        setDefaultLoc(this.getContext());
        setCurMarker();
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
        setCurMarker();
    }

    // set current latlng
    public void setCurLatlng(Location location) {
        curLatlng = new LatLng(location.getLatitude(), location.getLongitude());
        setCurMarker();
    }

    // update current marker position
    public void setCurMarker() {
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
        Log.d(TAG, this.getContext().getFilesDir().toString());
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


    @OnClick({R.id.btn_cafe, R.id.btn_hotel, R.id.btn_hospital, R.id.btn_salon, R.id.btn_trail})
    void clickSearch(View view) {
        String state = view.getTag().toString();
        if (state.equals("X")) { // 버튼 off -> on
            btn_all.setTag("X");
            view.setTag("O");
        } else // 버튼 on -> off
            view.setTag("X");
        showMarker(btn_cafe.getTag().toString(), btn_hotel.getTag().toString(), btn_hospital.getTag().toString(), btn_salon.getTag().toString(), btn_trail.getTag().toString());
    }

    @OnClick(R.id.btn_all)
    void clickSearchAll(View view) {
        String state = view.getTag().toString();
        if (state.equals("X")) { // 버튼 off -> on
            btn_all.setTag("O");
            btn_cafe.setTag("X");
            btn_hotel.setTag("X");
            btn_hospital.setTag("X");
            btn_salon.setTag("X");
            btn_trail.setTag("X");
            showMarker("O", "O", "O", "O", "O");
        } else { // 버튼 on -> of
            btn_all.setTag("X");
            showMarker("X", "X", "X", "X", "X");
        }
    }


    private void showMarker(String cafe, String hotel, String hospital, String salon, String trail) { //버튼 클릭했을 때

        googleMap.clear();
        marker = null;
        setCurMarker();
        if (latLngList != null)
            drawPolyline(latLngList);

        if (cafe.equals("O")) {
            long _start = System.currentTimeMillis();
            for (Data data : DataSet.cafeList)
                addMarker(data, "marker_cafe");
            long _end = System.currentTimeMillis();
            Log.d(TAG, "cafe"+(_end-_start)/1000);
        }
        if (hotel.equals("O")) {
            long _start = System.currentTimeMillis();
            for (Data data : DataSet.hotelList)
                addMarker(data, "marker_hotel");
            long _end = System.currentTimeMillis();
            Log.d(TAG, "hotel"+(_end-_start)/1000);
        }
        if (hospital.equals("O")) {
            long _start = System.currentTimeMillis();
            for (Data data : DataSet.hospitalList)
                addMarker(data, "marker_hospital");
            long _end = System.currentTimeMillis();
            Log.d(TAG, "hospital"+(_end-_start)/1000);
        }
        if (salon.equals("O")) {
            long _start = System.currentTimeMillis();
            for (Data data : DataSet.salonList)
                addMarker(data, "marker_salon");
            long _end = System.currentTimeMillis();
            Log.d(TAG, "salon"+(_end-_start)/1000);
        }
        if (trail.equals("O")) {
            long _start = System.currentTimeMillis();
            for (Data data : DataSet.trailList)
                addMarker(data, "marker_tree");
            long _end = System.currentTimeMillis();
            Log.d(TAG, "salon"+(_end-_start)/1000);
        }
    }

    private void addMarker(Data data, String imgname) { //마커 추가

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(imgname, "drawable", getContext().getPackageName()));
        Bitmap bitmap_resize = Bitmap.createScaledBitmap(bitmap, 120, 120, false);

        LatLng position = new LatLng(data.getLatitude(), data.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions()
                .title(data.getTitle())
                .position(position)
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap_resize));
        Marker marker = googleMap.addMarker(markerOptions);
        marker.setTag(data);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        DataDialog dialog = new DataDialog();
        Bundle args = new Bundle();
        args.putSerializable("data", (Data) (marker.getTag()));
        args.putParcelable("curLatlng", curLatlng);
        dialog.setArguments(args);
        dialog.show(getActivity().getFragmentManager(), "tag");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        googleMap.animateCamera(center);
        return false;
    }

}