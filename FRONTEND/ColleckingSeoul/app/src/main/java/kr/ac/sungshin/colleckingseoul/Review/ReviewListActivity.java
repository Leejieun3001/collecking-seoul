package kr.ac.sungshin.colleckingseoul.Review;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    @BindView(R.id.reviewlist_floatingbutton_fab)
    FloatingActionButton floatingButton;

    private final String TAG = "ReviewActivity";
    private final int REQUEST_FOR_BOARD = 1000;
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
        Log.d(TAG, gettingIntent.getStringExtra("title"));
        getSupportActionBar().setTitle(gettingIntent.getStringExtra("title"));
        idx = gettingIntent.getIntExtra("idx", 1);
        initPosition = new LatLng(gettingIntent.getDoubleExtra("lat", 37.581049), gettingIntent.getDoubleExtra("lng", 126.982533));

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.reviewlist_fragment_map);
        mapFragment.getMapAsync(this);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RegisterReviewActivity.class);
                intent.putExtra("idx", idx + "");
                startActivityForResult(intent, REQUEST_FOR_BOARD);
            }
        });
        initRecyclerView();
        getReview();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            Toast.makeText(getBaseContext(), "결과가 올바르게 수행되지 못했습니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == REQUEST_FOR_BOARD) {
            // 리스트 갱신 필요
        }
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
