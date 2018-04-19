package kr.ac.sungshin.colleckingseoul.home;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.Review.ReviewListActivity;
import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;
import kr.ac.sungshin.colleckingseoul.sqLite.SqLiteController;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.home_fragment_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;

        LatLng gwanghwamun = new LatLng(37.576183, 126.976926);
        googleMap.addMarker(new MarkerOptions().position(gwanghwamun).title("광화문"));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override public boolean onMarkerClick(Marker marker) {
                marker.getTag();
                Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
                intent.putExtra("lat", 37.576183);
                intent.putExtra("lng", 126.976926);
                startActivity(intent);
                return false;
            }
        });

//        loadLandmark();
    }

    private void loadLandmark () {
        SqLiteController helper = new SqLiteController(this);
        ArrayList<Landmark> landmarks = helper.getAllLandmark(helper.getWritableDatabase());
        for (Landmark landmark : landmarks) {
            LatLng latLng = new LatLng(37.576183, 126.976926);
            googleMap.addMarker(new MarkerOptions().position(latLng).title(landmark.getName()));
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override public boolean onMarkerClick(Marker marker) {
                    marker.getTag();
                    Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
                    intent.putExtra("lat", 37.576183);
                    intent.putExtra("lng", 126.976926);
                    startActivity(intent);
                    return false;
                }
            });
        }

        // icon 으로 마커 생성하는 방법
//        Marker melbourne = mMap.addMarker(new MarkerOptions().position(MELBOURNE)
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
    }
}
