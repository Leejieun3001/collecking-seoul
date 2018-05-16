package kr.ac.sungshin.colleckingseoul.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.Review.ReviewListActivity;
import kr.ac.sungshin.colleckingseoul.model.response.LandmarkListResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kwonhyeon-a on 2018. 5. 15..
 */

public class MapFragment extends Fragment {
    String TAG = "MapFragment";
    private boolean isFirst = true;
    private GoogleMap googleMap;
    private NetworkService service;
    private ClusterManager<MarkerItem> clusterManager;
    private ArrayList<Landmark> list = new ArrayList<>();

    MapView mMapView;

    public MapFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getView();
        service = ApplicationController.getInstance().getNetworkService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        initMap(view, savedInstanceState);

        return view;
    }

    private void initMap(View view, Bundle savedInstanceState) {
        mMapView = (MapView) view.findViewById(R.id.maps_mapView_mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(37.581049, 126.982533)).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        marker.getTag();
                        Intent intent = new Intent(getContext(), ReviewListActivity.class);
                        intent.putExtra("idx", marker.getTag().toString());
                        intent.putExtra("lng", 126.976926);
                        startActivity(intent);
                        return false;
                    }
                });

                if (isFirst) {
                    loadLandmark();
                    isFirst = !isFirst;
                } else {
                    createMarkers();
                }

            }
        });
    }

    private void loadLandmark() {
        Call<LandmarkListResult> getLandmarkList = service.getLandmarkList();
        getLandmarkList.enqueue(new Callback<LandmarkListResult>() {
            @Override
            public void onResponse(Call<LandmarkListResult> call, Response<LandmarkListResult> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    switch (message) {
                        case "SUCCESS":
                            list.addAll(response.body().getLandmarkList());
//                            setUpClusterer();
                            createMarkers();
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
            Marker marker = googleMap.addMarker(new MarkerOptions().position(latLng).title(landmark.getName()));
            marker.setTag(landmark);
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Landmark ld = (Landmark) marker.getTag();
                    Intent intent = new Intent(getContext(), ReviewListActivity.class);
                    intent.putExtra("lat", ld.getLat());
                    intent.putExtra("lng", ld.getLng());
                    intent.putExtra("idx", ld.getIdx());
                    intent.putExtra("title", ld.getName());
                    startActivity(intent);
                    return false;
                }
            });
        }
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


    private void setUpClusterer() {
        clusterManager = new ClusterManager<MarkerItem>(getContext(), googleMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        googleMap.setOnCameraIdleListener(clusterManager);

        // Add cluster items (markers) to the cluster manager.
        try {
            addItems();
        } catch (Exception e) {
            e.getStackTrace();
            Toast.makeText(getContext(), "Problem reading list of markers.", Toast.LENGTH_LONG).show();
        }
    }

    private void addItems() {
        int length = list.size();
        for (int i = 0; i < length; i++) {
            final Landmark landmark = list.get(i);
            MarkerItem offsetItem = new MarkerItem(landmark.getLat(), landmark.getLng());
            clusterManager.addItem(offsetItem);
        }
    }
}
