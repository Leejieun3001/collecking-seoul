package kr.ac.sungshin.colleckingseoul.rank;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.Review.BoardItem;
import kr.ac.sungshin.colleckingseoul.Review.ReviewActivity;
import kr.ac.sungshin.colleckingseoul.Review.ReviewListAdapter;
import kr.ac.sungshin.colleckingseoul.ViewPagerAdapter;
import kr.ac.sungshin.colleckingseoul.home.HomeActivity;
import kr.ac.sungshin.colleckingseoul.home.MapFragment;
import kr.ac.sungshin.colleckingseoul.model.response.LandmarkListResult;
import kr.ac.sungshin.colleckingseoul.model.response.rank.LandmarkRank;
import kr.ac.sungshin.colleckingseoul.model.response.rank.LandmarkRankResult;
import kr.ac.sungshin.colleckingseoul.model.response.rank.UserRank;
import kr.ac.sungshin.colleckingseoul.model.response.rank.UserRankResult;
import kr.ac.sungshin.colleckingseoul.mypage.MyPageFragment;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by kwonhyeon-a on 2018. 5. 15..
 */

public class RankFragment extends Fragment {
    @BindView(R.id.rank_circleimageview_photono1)
    CircleImageView no1PhotoCircleImageView;
    @BindView(R.id.rank_textview_nameno1)
    TextView no1NameTextView;
    @BindView(R.id.rank_textview_idno1)
    TextView no1IdTextView;
    @BindView(R.id.rank_circleimageview_photono2)
    CircleImageView no2PhotoCircleImageView;
    @BindView(R.id.rank_textview_nameno2)
    TextView no2NameTextView;
    @BindView(R.id.rank_textview_idno2)
    TextView no2IdTextView;
    @BindView(R.id.rank_circleimageview_photono3)
    CircleImageView no3PhotoCircleImageView;
    @BindView(R.id.rank_textview_nameno3)
    TextView no3NameTextView;
    @BindView(R.id.rank_textview_idno3)
    TextView no3IdTextView;

    @BindView(R.id.rank_recyclerView_landmarkrecyclerview)
    RecyclerView landmarkRecyclerView;

    String TAG = "RankFragment";
    private NetworkService service;
    private boolean flag = true;
    private final int MEMBER = 0;
    private final int LANDMARK = 1;
    private FragmentManager fragmentManager;

    private LandmarkRankAdapter landmarkRankAdapter;
    private LinearLayoutManager landmarkLayoutManager;
    private ArrayList<UserRank> userRankList = new ArrayList<>();
    private ArrayList<LandmarkRank> landmarkRankList = new ArrayList<>();

    public RankFragment() {
        super();
    }

    public void setFragmentManager (FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ApplicationController.getInstance().getNetworkService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        if (!flag) {
            setUserRankInfo();
            landmarkRecyclerView.setAdapter(landmarkRankAdapter);
            return view;
        }
        flag = false;

        Call<UserRankResult> getUserRankList = service.getUserRankList();
        getUserRankList.enqueue(new Callback<UserRankResult>() {
            @Override
            public void onResponse(Call<UserRankResult> call, Response<UserRankResult> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    switch (message) {
                        case "SUCCESS":
                            userRankList.addAll(response.body().getUserRankList());
                            setUserRankInfo();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRankResult> call, Throwable t) {

            }
        });

        Call<LandmarkRankResult> getLandmarkRankList = service.getLandmarkRankList();
        getLandmarkRankList.enqueue(new Callback<LandmarkRankResult>() {
            @Override
            public void onResponse(Call<LandmarkRankResult> call, Response<LandmarkRankResult> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    switch (message) {
                        case "SUCCESS":
                            landmarkRankList.addAll(response.body().getLandmarkRankList());
                            setLandmarkRankAdapter();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<LandmarkRankResult> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });

        return view;
    }

    private void setUserRankInfo() {
        UserRank userRank = userRankList.get(0);
        no1IdTextView.setText(userRank.getId().split("@")[0]);
        no1NameTextView.setText(userRank.getNickname());
        Glide.with(getContext())
                .load(userRank.getUrl())
                .into(no1PhotoCircleImageView);

        userRank = userRankList.get(1);
        no2IdTextView.setText(userRank.getId().split("@")[0]);
        no2NameTextView.setText(userRank.getNickname());
        Glide.with(getContext())
                .load(userRank.getUrl())
                .into(no2PhotoCircleImageView);

        userRank = userRankList.get(2);
        no3IdTextView.setText(userRank.getId().split("@")[0]);
        no3NameTextView.setText(userRank.getNickname());
        Glide.with(getContext())
                .load(userRank.getUrl())
                .into(no3PhotoCircleImageView);
    }

    private void initRecyclerView() {
        landmarkRecyclerView.setHasFixedSize(true);
        landmarkLayoutManager = new LinearLayoutManager(getContext());
        landmarkLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        landmarkRecyclerView.setLayoutManager(landmarkLayoutManager);

    }

    private void setLandmarkRankAdapter() {
        landmarkRankAdapter = new LandmarkRankAdapter(getContext(), landmarkRankList, clickEvent);
        landmarkRankAdapter.notifyDataSetChanged();
        landmarkRecyclerView.setAdapter(landmarkRankAdapter);
    }

    public View.OnClickListener clickEvent = new View.OnClickListener() {
        public void onClick(View v) {
        int itemPosition = landmarkRecyclerView.getChildPosition(v);
        int tempId = landmarkRankList.get(itemPosition).getIdx();
        Intent intent = new Intent(getContext(), ReviewActivity.class);

        intent.putExtra("idx", tempId);
        startActivity(intent);
        }
    };
}
