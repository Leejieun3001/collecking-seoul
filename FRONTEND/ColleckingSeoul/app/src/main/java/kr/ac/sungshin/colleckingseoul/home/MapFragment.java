package kr.ac.sungshin.colleckingseoul.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
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
    private MarkerItem clickedClusterItem;

    MapView mMapView;

    public MapFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ApplicationController.getInstance().getNetworkService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        initMap(view, savedInstanceState);

        return view;
    }

    private void initMap(final View view, Bundle savedInstanceState) {
        mMapView = (MapView) view.findViewById(R.id.maps_mapView_mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(view.getContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(37.581049, 126.982533)).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                if (isFirst) {
                    loadLandmark();
                    isFirst = !isFirst;
                } else {
                    setUpClusterer();
                    addItemsOnCluster();
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
                            setUpClusterer();
                            addItemsOnCluster();
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

    private void setUpClusterer() {
        clusterManager = new ClusterManager<>(getActivity(), googleMap);
        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);
        googleMap.setInfoWindowAdapter(clusterManager.getMarkerManager());
        googleMap.setOnInfoWindowClickListener(clusterManager); //added

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<MarkerItem>() {
            @Override
            public boolean onClusterClick(Cluster<MarkerItem> cluster) {
                CameraPosition cameraPosition = new CameraPosition.Builder().target(cluster.getPosition()).zoom(14).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                return true;
            }
        });

        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<MarkerItem>() {

            @Override
            public boolean onClusterItemClick(MarkerItem markerItem) {
                clickedClusterItem = markerItem;
                return false;
            }
        });

        clusterManager.setOnClusterItemInfoWindowClickListener(new ClusterManager.OnClusterItemInfoWindowClickListener<MarkerItem>() {
            @Override
            public void onClusterItemInfoWindowClick(MarkerItem markerItem) {
                Intent intent = new Intent(getContext(), ReviewListActivity.class);
                intent.putExtra("lat", markerItem.getPosition().latitude);
                intent.putExtra("lng", markerItem.getPosition().longitude);
                intent.putExtra("idx", markerItem.getIdx());
                intent.putExtra("title", markerItem.getTitle());
                startActivity(intent);
            }
        });

        clusterManager.getMarkerCollection().setOnInfoWindowAdapter(new MyInfoWindowAdapter());
    }

    private void addItemsOnCluster() {
        try {
            int length = list.size();

            for (int i = 0; i < length; i++) {
                final Landmark landmark = list.get(i);
                MarkerItem offsetItem = new MarkerItem(landmark.getLat(), landmark.getLng(), landmark.getName(), landmark.getIdx());
                clusterManager.addItem(offsetItem);
            }
        } catch (Exception e) {
            e.getStackTrace();
            Toast.makeText(getContext(), "지도 정보를 불러오는 중 문제가 발생했습니다. 다시 한 번 시도해주세요.", Toast.LENGTH_LONG).show();
        }
    }

    public class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;
        TextView titleTextView;

        MyInfoWindowAdapter() {
            myContentsView = getLayoutInflater().inflate(
                    R.layout.item_map_infowindow, null);
        }
        @Override
        public View getInfoWindow(Marker marker) {
            titleTextView = (TextView) myContentsView.findViewById(R.id.infowindow_textview_title);
            titleTextView.setText(clickedClusterItem.getTitle());
            return myContentsView;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }
}
