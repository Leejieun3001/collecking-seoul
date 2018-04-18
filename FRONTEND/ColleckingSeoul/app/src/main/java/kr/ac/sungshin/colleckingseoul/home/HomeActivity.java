package kr.ac.sungshin.colleckingseoul.home;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import kr.ac.sungshin.colleckingseoul.R;

public class HomeActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        LatLng gwanghwamun = new LatLng(37.576183, 126.976926);
        googleMap.addMarker(new MarkerOptions().position(gwanghwamun).title("광화문"));
        // 처음 초기화 위치 잡기, zoom 크기
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.getTag();
        return false;
    }
}
