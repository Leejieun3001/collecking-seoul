package kr.ac.sungshin.colleckingseoul.home;

import android.content.SharedPreferences;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.OnMapReadyCallback;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;

import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.Review.ReviewListActivity;

import kr.ac.sungshin.colleckingseoul.model.response.LandmarkListResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;
import retrofit2.Call;
import retrofit2.Callback;

import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;

import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;


    String TAG = "HomeActivity";
    private NetworkService service;
    private ArrayList<Landmark> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        service = ApplicationController.getInstance().getNetworkService();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.home_fragment_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.getTag();
                Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
                intent.putExtra("lat", 37.576183);
                intent.putExtra("lng", 126.976926);
                startActivity(intent);
                return false;
            }
        });

        LatLng latLng = new LatLng(37.576183, 126.976926);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("현아"));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.getTag();
                Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
                intent.putExtra("lat", 37.576183);
                intent.putExtra("lng", 126.976926);
                startActivity(intent);
                return false;
            }
        });

        loadLandmark();
        createMarkers();
    }

//    private void loadLandmark() {
//        SqLiteController helper = new SqLiteController(this);
//        ArrayList<Landmark> landmarks = helper.getAllLandmark(helper.getWritableDatabase());
//        for (final Landmark landmark : landmarks) {
//            LatLng latLng = new LatLng(landmark.getLat(), landmark.getLat());
//            googleMap.addMarker(new MarkerOptions().position(latLng).title(landmark.getName()));
//            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    marker.getTag();
//                    Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
//                    intent.putExtra("lat", landmark.getLat());
//                    intent.putExtra("lng", landmark.getLat());
//                    startActivity(intent);
//                    return false;
//                }
//            });
//        }
//
//        // icon 으로 마커 생성하는 방법
////        Marker melbourne = mMap.addMarker(new MarkerOptions().position(MELBOURNE)
////                .icon(BitmapDescriptorFactory
////                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//    }

    private void loadLandmark() {
        Call<LandmarkListResult> getLandmarkList = service.getLandmarkList();
        getLandmarkList.enqueue(new Callback<LandmarkListResult>() {
            @Override
            public void onResponse(Call<LandmarkListResult> call, Response<LandmarkListResult> response) {
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();

                    switch (message) {
                        case "SUCCESS":
                            list.addAll(response.body().getLandmarkList());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<LandmarkListResult> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    private void createMarkers() {
        int length = list.size();
        for (int i = 0; i < length; i++) {
            final Landmark landmark = list.get(i);
            LatLng latLng = new LatLng(landmark.getLat(), landmark.getLng());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(landmark.getName()));
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.getTag();
                    Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
                    intent.putExtra("lat", landmark.getLat());
                    intent.putExtra("lng", landmark.getLat());
                    startActivity(intent);
                    return false;
                }
            });
        }
    }

}
