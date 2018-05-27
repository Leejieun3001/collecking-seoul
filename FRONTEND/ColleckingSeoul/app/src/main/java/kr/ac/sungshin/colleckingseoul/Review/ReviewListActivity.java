package kr.ac.sungshin.colleckingseoul.Review;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.model.response.BoardListResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private int hasDone = 0;

    private LinearLayoutManager layoutManager;
    private ReviewListAdapter adapter;
    private ArrayList<BoardItem> itemList;

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
                if (hasDone == 1) {
                    // 이미 후기를 작성한 사용자일 경우,
                    Toast.makeText(getBaseContext(), "후기는 한번만 등록하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 후기를 작성한적이 없는 사용자일 경우,
                    Intent intent = new Intent(getBaseContext(), RegisterReviewActivity.class);
                    intent.putExtra("idx", idx + "");
                    intent.putExtra("purpose", "register");
                    startActivityForResult(intent, REQUEST_FOR_BOARD);
                }
            }
        });
        initRecyclerView();
        getReview(idx);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_FOR_BOARD && resultCode == RESULT_OK) {
            // 리스트 갱신 필요
            getReview(idx);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.addMarker(new MarkerOptions().position(initPosition));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initPosition, 15));
    }

    private void initRecyclerView() {
        itemList = new ArrayList<>();
        adapter = new ReviewListAdapter(getBaseContext(), itemList, clickEvent);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
    }

    // 해당 랜드마크의 리뷰 리스트를 불러온다.
    private void getReview(int idx) {

        Call<BoardListResult> getReviewList = service.getBoardListResult(idx);
        getReviewList.enqueue(new Callback<BoardListResult>() {
            @Override
            public void onResponse(Call<BoardListResult> call, Response<BoardListResult> response) {

                if (response.isSuccessful()) {
                    if (response.body().getMessage().equals("SUCCESS")) {
                        hasDone = response.body().getHasDone();
                        itemList = response.body().getBoards();
                        if(itemList.isEmpty()){
                            BoardItem d = new BoardItem(0,0,0,"","","","",0,"");
                            d.setContent("아직 등록된 리뷰가 없습니다.");
                            itemList.add(d);
                        }

                    }
                    if (response.body().getMessage().equals("NULL_VALUE")) {
                        Toast.makeText(getApplicationContext(), "값을 입력해 주세요.", Toast.LENGTH_SHORT).show();

                    }
                    if (response.body().getMessage().equals("NOT_LOGIN")) {
                        Toast.makeText(getApplicationContext(), "로그인 하지 않은 사용자입니다.", Toast.LENGTH_SHORT).show();

                    }
                    setAdapter(itemList);

                }
            }

            @Override
            public void onFailure(Call<BoardListResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "서버 오류입니다. 빠른 시일내에 개선하겠습니다. 죄송합니다", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public View.OnClickListener clickEvent = new View.OnClickListener() {
        public void onClick(View v) {
            int itemPosition = recyclerView.getChildPosition(v);
            int tempId = itemList.get(itemPosition).getIdx();
            Intent intent = new Intent(getBaseContext(), ReviewActivity.class);

            intent.putExtra("idx", tempId);
            startActivity(intent);
        }
    };

    private void setAdapter(ArrayList<BoardItem> itemList) {
        adapter.setAdapter(itemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
