package kr.ac.sungshin.colleckingseoul.Review;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.home.MarkerItem;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;

public class ReviewListActivity extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.reviewlist_recyclerview_recyclerview)
    RecyclerView recyclerView;

    private final String TAG = "ReviewActivity";
    private NetworkService service;
    private GoogleMap googleMap;
    private LatLng initPosition;
    private int idx;

    private LinearLayoutManager layoutManager;
    private ReviewListAdapter adapter;
    private List<ReviewItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);
        service = ApplicationController.getInstance().getNetworkService();
        ButterKnife.bind(this);

        Intent gettingIntent = getIntent();
        idx = gettingIntent.getIntExtra("idx", 1);
        initPosition = new LatLng(gettingIntent.getDoubleExtra("lat", 37.581049), gettingIntent.getDoubleExtra("lng", 126.982533));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.reviewlist_fragment_map);
        mapFragment.getMapAsync(this);
        initRecyclerView();
        getReview();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.addMarker(new MarkerOptions().position(initPosition));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initPosition, 15));
    }

    private void initRecyclerView () {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    // 해당 랜드마크의 리뷰 리스트를 불러온다.
    private void getReview () {

    }

    private void setAdapter(ArrayList<ReviewItem> itemList) {
        adapter = new ReviewListAdapter(ReviewListActivity.this, itemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
