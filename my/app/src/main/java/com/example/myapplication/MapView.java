package com.example.myapplication;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;


public class MapView extends AppCompatActivity
        implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {


    private GoogleApiClient mGoogleApiClient = null;
    private GoogleMap mGoogleMap = null;
    private Marker currentMarker1 = null;
    private Marker currentMarker2 = null;
    private Marker currentMarker3 = null;

    private static final String TAG = "googlemap_example";
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 2002;
    private static final int UPDATE_INTERVAL_MS = 5000;  // 1초
    private static final int FASTEST_UPDATE_INTERVAL_MS = 5000; // 0.5초

    private AppCompatActivity mActivity;
    boolean askPermissionOnceAgain = false;
    boolean mRequestingLocationUpdates = false;
    Location location;
    boolean mMoveMapByUser = true;
    boolean mMoveMapByAPI = true;
    LatLng currentPosition;
    int a=0;
    String userName2;
    String userName3;
    String lat;
    String lot;
    String at;
    String ot;
    double lat2;
    double lot2;
    double at2;
    double ot2;
    int json;
    public static final String NOTIFICATION_CHANNEL_ID = "10001";
    double distance;
    String meter;
    String meter1;
    double i = 2;

    com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest()
            .setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setInterval(UPDATE_INTERVAL_MS)
            .setFastestInterval(FASTEST_UPDATE_INTERVAL_MS);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.map);


        Log.d(TAG, "onCreate");
        mActivity = this;


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();


        MapFragment mapFragment1 = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment1.getMapAsync(this);

        new BackgroundTask().execute();

    }

    private class BackgroundTask extends AsyncTask<Void, Void, String>
    {
        String target;

        @Override
        protected void onPreExecute(){
            try {
                target = "https://rlatmd0829.cafe24.com/GetLocation.php";
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL(target);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                StringBuilder stringBuilder = new StringBuilder();
                while((temp = bufferedReader.readLine()) != null)
                {
                    stringBuilder.append(temp + "\n");
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            } catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onProgressUpdate(Void... values){
            super.onProgressUpdate();
        }

        @Override
        public void onPostExecute(String result){
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONArray jsonArray = jsonObject.getJSONArray("response");

                    json = jsonArray.length()-1;
                    JSONObject object = jsonArray.getJSONObject(json);

                    lat = object.getString("lat");
                    lot = object.getString("lot");
                    userName2 = object.getString("userName");

                JSONObject object1 = jsonArray.getJSONObject(json-1);

                at = object1.getString("lat");
                ot = object1.getString("lot");
                userName3 = object1.getString("userName");

                /*lat2=Double.parseDouble(lat);
                lot2 =Double.parseDouble(lot);

                LatLng currentLatLng2 = new LatLng(lat2, lot2);

                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(currentLatLng2);
                markerOptions.title(getCurrentAddress(currentLatLng2));
                markerOptions.snippet("위도:" + String.valueOf(lat2)
                        + " 경도:" + String.valueOf(lot2));
                markerOptions.draggable(true);


                currentMarker = mGoogleMap.addMarker(markerOptions);

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng2);
                mGoogleMap.moveCamera(cameraUpdate);*/







            } catch (Exception e){
                e.printStackTrace();
            }
        }


    }






    @Override
    public void onResume() {

        super.onResume();

        if (mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onResume : call startLocationUpdates");
            if (!mRequestingLocationUpdates) startLocationUpdates();
        }


        //앱 정보에서 퍼미션을 허가했는지를 다시 검사해봐야 한다.
        if (askPermissionOnceAgain) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                askPermissionOnceAgain = false;

                checkPermissions();
            }
        }
    }


    private void startLocationUpdates() {

        if (!checkLocationServicesStatus()) {

            Log.d(TAG, "startLocationUpdates : call showDialogForLocationServiceSetting");
            showDialogForLocationServiceSetting();
        }else {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                Log.d(TAG, "startLocationUpdates : 퍼미션 안가지고 있음");
                return;
            }


            Log.d(TAG, "startLocationUpdates : call FusedLocationApi.requestLocationUpdates");
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
            mRequestingLocationUpdates = true;

            mGoogleMap.setMyLocationEnabled(true);

        }

    }



    private void stopLocationUpdates() {

        Log.d(TAG,"stopLocationUpdates : LocationServices.FusedLocationApi.removeLocationUpdates");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mRequestingLocationUpdates = false;
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {

        Log.d(TAG, "onMapReady :");

        mGoogleMap = googleMap;


        //런타임 퍼미션 요청 대화상자나 GPS 활성 요청 대화상자 보이기전에
        //지도의 초기위치를 서울로 이동
        setDefaultLocation();

        //mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
        mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener(){

            @Override
            public boolean onMyLocationButtonClick() {

                Log.d( TAG, "onMyLocationButtonClick : 위치에 따른 카메라 이동 활성화");
                mMoveMapByAPI = true;
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                Log.d( TAG, "onMapClick :");
            }
        });

        mGoogleMap.setOnCameraMoveStartedListener(new GoogleMap.OnCameraMoveStartedListener() {

            @Override
            public void onCameraMoveStarted(int i) {

                if (mMoveMapByUser == true && mRequestingLocationUpdates){

                    Log.d(TAG, "onCameraMove : 위치에 따른 카메라 이동 비활성화");
                    mMoveMapByAPI = false;
                }

                mMoveMapByUser = true;

            }
        });


        mGoogleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {

            @Override
            public void onCameraMove() {


            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {
        json++;

        currentPosition
                = new LatLng(location.getLatitude(), location.getLongitude());


        Log.d(TAG, "onLocationChanged : ");

        String markerTitle = getCurrentAddress(currentPosition);
        String markerSnippet = "위도:" + String.valueOf(location.getLatitude())
                + " 경도:" + String.valueOf(location.getLongitude());

        //현재 위치에 마커 생성하고 이동
        setCurrentLocation(location, markerTitle, markerSnippet);




    }

    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");

        builder.setSmallIcon(R.drawable.covid192);
        builder.setContentTitle("!!!!!!!!안전 안내 문자!!!!!!!");
        builder.setContentText("사회적 거리두기 2m 위반 하셨습니다.");

        builder.setColor(Color.RED);
        // 사용자가 탭을 클릭하면 자동 제거
        builder.setAutoCancel(true);

        // 알림 표시
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel("default", "기본 채널", NotificationManager.IMPORTANCE_DEFAULT));
        }

        // id값은
        // 정의해야하는 각 알림의 고유한 int값
        notificationManager.notify(1, builder.build());
    }



    @Override
    protected void onStart() {

        if(mGoogleApiClient != null && mGoogleApiClient.isConnected() == false){

            Log.d(TAG, "onStart: mGoogleApiClient connect");
            mGoogleApiClient.connect();
        }

        super.onStart();
    }

    @Override
    protected void onStop() {

        if (mRequestingLocationUpdates) {

            Log.d(TAG, "onStop : call stopLocationUpdates");
            stopLocationUpdates();
        }

        if ( mGoogleApiClient.isConnected()) {

            Log.d(TAG, "onStop : mGoogleApiClient disconnect");
            mGoogleApiClient.disconnect();
        }

        super.onStop();
    }


    @Override
    public void onConnected(Bundle connectionHint) {


        if ( mRequestingLocationUpdates == false ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED) {

                    ActivityCompat.requestPermissions(mActivity,
                            new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                } else {

                    Log.d(TAG, "onConnected : 퍼미션 가지고 있음");
                    Log.d(TAG, "onConnected : call startLocationUpdates");
                    startLocationUpdates();
                    mGoogleMap.setMyLocationEnabled(true);
                }

            }else{

                Log.d(TAG, "onConnected : call startLocationUpdates");
                startLocationUpdates();
                mGoogleMap.setMyLocationEnabled(true);
            }
        }
    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.d(TAG, "onConnectionFailed");
        setDefaultLocation();
    }


    @Override
    public void onConnectionSuspended(int cause) {

        Log.d(TAG, "onConnectionSuspended");
        if (cause == CAUSE_NETWORK_LOST)
            Log.e(TAG, "onConnectionSuspended(): Google Play services " +
                    "connection lost.  Cause: network lost.");
        else if (cause == CAUSE_SERVICE_DISCONNECTED)
            Log.e(TAG, "onConnectionSuspended():  Google Play services " +
                    "connection lost.  Cause: service disconnected");
    }


    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }


    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    public void setCurrentLocation(final Location location, final String markerTitle, final String markerSnippet) {

        mMoveMapByUser = false;


        new BackgroundTask().execute();
            //if (currentMarker != null) currentMarker.remove();
            Intent intent = getIntent();
            String userName = intent.getStringExtra("userName");
            final double abc1 = location.getLatitude();
            final double abc2 = location.getLongitude();
            final String lat1 = Double.toString(abc1);
            final String lot1 = Double.toString(abc2);





            try {
                at2 = Double.parseDouble(at);
                ot2 = Double.parseDouble(ot);
                lat2 = Double.parseDouble(lat);
                lot2 = Double.parseDouble(lot);
                if (currentMarker1 != null) currentMarker1.remove();
                if( a== 0){
                    new BackgroundTask().execute();

                    LatLng currentLatLng2 = new LatLng(abc1, abc2);

                    MarkerOptions markerOptions1 = new MarkerOptions();
                    markerOptions1.position(currentLatLng2);
                    markerOptions1.title("'" + userName + "' " + getCurrentAddress(currentLatLng2));
                    markerOptions1.snippet("위도:" + String.valueOf(abc1)
                            + " 경도:" + String.valueOf(abc2));
                    markerOptions1.draggable(true);


                    currentMarker1 = mGoogleMap.addMarker(markerOptions1);

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng2);
                    mGoogleMap.moveCamera(cameraUpdate);

                    Response.Listener<String> responseListener1 = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {

                            try {

                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                        /*if (success) {
                            LatLng currentLatLng = new LatLng(abc1, abc2);

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(currentLatLng);
                            markerOptions.title(markerTitle);
                            markerOptions.snippet(markerSnippet);
                            markerOptions.draggable(true);


                            currentMarker = mGoogleMap.addMarker(markerOptions);

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                            mGoogleMap.moveCamera(cameraUpdate);


                            if (mMoveMapByAPI) {

                                Log.d(TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                                        + location.getLatitude() + " " + location.getLongitude());
                                // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                                //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                                mGoogleMap.moveCamera(cameraUpdate);
                            }
                        }*/

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }


                    };

                    LocationRequest locationRequset = new LocationRequest(lat1, lot1, userName, responseListener1);
                    RequestQueue queue1 = Volley.newRequestQueue(MapView.this);
                    queue1.add(locationRequset);


                    if (mMoveMapByAPI) {

                        Log.d(TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                                + location.getLatitude() + " " + location.getLongitude());
                        // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                        mGoogleMap.moveCamera(cameraUpdate);
                    }
                    a++;
                } else {
                    new BackgroundTask().execute();
                    if (userName.equals(userName2) ) {

                        if (currentMarker2 != null) currentMarker2.remove();

                        LatLng currentLatLng2 = new LatLng(lat2, lot2);

                        MarkerOptions markerOptions2 = new MarkerOptions();
                        markerOptions2.position(currentLatLng2);
                        markerOptions2.title("'" + userName2 + "' " + getCurrentAddress(currentLatLng2));
                        markerOptions2.snippet("위도:" + String.valueOf(lat2)
                                + " 경도:" + String.valueOf(lot2));
                        markerOptions2.draggable(true);


                        currentMarker2 = mGoogleMap.addMarker(markerOptions2);

                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng2);
                        mGoogleMap.moveCamera(cameraUpdate);

                        if (mMoveMapByAPI) {

                            Log.d(TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                                    + location.getLatitude() + " " + location.getLongitude());
                            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                            mGoogleMap.moveCamera(cameraUpdate);
                        }
                    } else {
                       new BackgroundTask().execute();
                       if (currentMarker3 != null) currentMarker3.remove();
                        LatLng currentLatLng2 = new LatLng(lat2, lot2);

                        MarkerOptions markerOptions3 = new MarkerOptions();
                        markerOptions3.position(currentLatLng2);
                        markerOptions3.title("'" + userName2 + "' " + getCurrentAddress(currentLatLng2));
                        markerOptions3.snippet("위도:" + String.valueOf(lat2)
                                + " 경도:" + String.valueOf(lot2));
                        markerOptions3.icon(BitmapDescriptorFactory.defaultMarker(200f));
                        markerOptions3.draggable(true);


                        currentMarker3 = mGoogleMap.addMarker(markerOptions3);

                        /*CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng2);
                        mGoogleMap.moveCamera(cameraUpdate);

                        if (mMoveMapByAPI) {

                            Log.d(TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                                    + location.getLatitude() + " " + location.getLongitude());
                            // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                            //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                            mGoogleMap.moveCamera(cameraUpdate);
                        }*/


                    }
                }

                Response.Listener<String> responseListener1 = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                        /*if (success) {
                            LatLng currentLatLng = new LatLng(abc1, abc2);

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(currentLatLng);
                            markerOptions.title(markerTitle);
                            markerOptions.snippet(markerSnippet);
                            markerOptions.draggable(true);


                            currentMarker = mGoogleMap.addMarker(markerOptions);

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                            mGoogleMap.moveCamera(cameraUpdate);


                            if (mMoveMapByAPI) {

                                Log.d(TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                                        + location.getLatitude() + " " + location.getLongitude());
                                // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                                //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                                mGoogleMap.moveCamera(cameraUpdate);
                            }
                        }*/

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                };

                LocationRequest locationRequset = new LocationRequest(lat1, lot1, userName, responseListener1);

                RequestQueue queue1 = Volley.newRequestQueue(MapView.this);
                queue1.add(locationRequset);

                location.setLatitude(at2);  // 마지막 레코드에서 위에값
                location.setLongitude(ot2);

                Location locationA = new Location("p");
                locationA.setLatitude(lat2);  // 마지막 레코드값
                locationA.setLongitude(lot2);




                distance = location.distanceTo(locationA);   // 두 데이터베이스에 위도경도 값에 거리비교

                meter = String.format("%.2f",distance);
                Toast.makeText(getApplicationContext(), "거리비교 : " + meter, Toast.LENGTH_SHORT).show();

                if (distance < i) {

                    createNotification();


                }




            } catch (Exception e) {
                e.printStackTrace();
            }
        json++;



            /*Response.Listener<String> responseListener2 = new Response.Listener<String>(){

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        String abcd1 =jsonResponse.getString("lat"); /// lat, lot에 값이 재대로 안받아와짐
                        String abcd2 =jsonResponse.getString("lot");
                        double lat2=Double.parseDouble(abcd1);
                        double lot2 =Double.parseDouble(abcd2);

                            LatLng currentLatLng2 = new LatLng(lat2, lot2);

                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(currentLatLng2);
                            markerOptions.title(getCurrentAddress(currentLatLng2));
                            markerOptions.snippet("위도:" + String.valueOf(lat2)
                                    + " 경도:" + String.valueOf(lot2));
                            markerOptions.draggable(true);


                            currentMarker = mGoogleMap.addMarker(markerOptions);

                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng2);
                            mGoogleMap.moveCamera(cameraUpdate);


                            if ( mMoveMapByAPI ) {

                                Log.d( TAG, "setCurrentLocation :  mGoogleMap moveCamera "
                                        + location.getLatitude() + " " + location.getLongitude() ) ;
                                // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 15);
                                //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(currentLatLng);
                                mGoogleMap.moveCamera(cameraUpdate);
                            }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            GetLocationRequest getlocationRequest = new GetLocationRequest(responseListener2);
            RequestQueue queue2 = Volley.newRequestQueue(MapView.this);
            queue2.add(getlocationRequest);*/





    }




    public void setDefaultLocation() {


        mMoveMapByUser = false;


        //디폴트 위치, Seoul
        LatLng DEFAULT_LOCATION = new LatLng(37.56, 126.97);
        String markerTitle = "위치정보 가져올 수 없음";
        String markerSnippet = "위치 퍼미션과 GPS 활성 여부 확인하세요";


        //if (currentMarker != null) currentMarker.remove();

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(DEFAULT_LOCATION);
        markerOptions.title(markerTitle);
        markerOptions.snippet(markerSnippet);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currentMarker2 = mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(DEFAULT_LOCATION, 15);
        mGoogleMap.moveCamera(cameraUpdate);

    }


    //여기부터는 런타임 퍼미션 처리을 위한 메소드들
    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        boolean fineLocationRationale = ActivityCompat
                .shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (hasFineLocationPermission == PackageManager
                .PERMISSION_DENIED && fineLocationRationale)
            showDialogForPermission("앱을 실행하려면 퍼미션을 허가하셔야합니다.");

        else if (hasFineLocationPermission
                == PackageManager.PERMISSION_DENIED && !fineLocationRationale) {
            showDialogForPermissionSetting("퍼미션 거부 + Don't ask again(다시 묻지 않음) " +
                    "체크 박스를 설정한 경우로 설정에서 퍼미션 허가해야합니다.");
        } else if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {


            Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");

            if ( mGoogleApiClient.isConnected() == false) {

                Log.d(TAG, "checkPermissions : 퍼미션 가지고 있음");
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (permsRequestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION && grantResults.length > 0) {

            boolean permissionAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (permissionAccepted) {


                if ( mGoogleApiClient.isConnected() == false) {

                    Log.d(TAG, "onRequestPermissionsResult : mGoogleApiClient connect");
                    mGoogleApiClient.connect();
                }



            } else {

                checkPermissions();
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void showDialogForPermission(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapView.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            }
        });

        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }

    private void showDialogForPermissionSetting(String msg) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapView.this);
        builder.setTitle("알림");
        builder.setMessage(msg);
        builder.setCancelable(true);
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                askPermissionOnceAgain = true;

                Intent myAppSettings = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + mActivity.getPackageName()));
                myAppSettings.addCategory(Intent.CATEGORY_DEFAULT);
                myAppSettings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mActivity.startActivity(myAppSettings);
            }
        });
        builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        builder.create().show();
    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MapView.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d(TAG, "onActivityResult : 퍼미션 가지고 있음");


                        if ( mGoogleApiClient.isConnected() == false ) {

                            Log.d( TAG, "onActivityResult : mGoogleApiClient connect ");
                            mGoogleApiClient.connect();
                        }
                        return;
                    }
                }

                break;
        }
    }



}
