package kr.ac.sungshin.colleckingseoul.rank;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.Review.BoardItem;
import kr.ac.sungshin.colleckingseoul.Review.ReviewActivity;
import kr.ac.sungshin.colleckingseoul.Review.ReviewListAdapter;
import kr.ac.sungshin.colleckingseoul.model.response.LandmarkListResult;
import kr.ac.sungshin.colleckingseoul.model.response.rank.LandmarkRank;
import kr.ac.sungshin.colleckingseoul.model.response.rank.LandmarkRankResult;
import kr.ac.sungshin.colleckingseoul.model.response.rank.UserRank;
import kr.ac.sungshin.colleckingseoul.model.response.rank.UserRankResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kwonhyeon-a on 2018. 5. 15..
 */

public class RankFragment extends Fragment {
    @BindView(R.id.rank_recyclerView_userrecyclerview)
    RecyclerView userRecyclerView;
    @BindView(R.id.rank_recyclerView_landmarkrecyclerview)
    RecyclerView landmarkRecyclerView;

    String TAG = "RankFragment";
    private NetworkService service;
    private boolean flag = true;

    private UserRankAdapter userRankAdapter;
    private LandmarkRankAdapter landmarkRankAdapter;
    private LinearLayoutManager userLayoutManager;
    private LinearLayoutManager landmarkLayoutManager;
    private ArrayList<UserRank> userRankList = new ArrayList<>();
    private ArrayList<LandmarkRank> landmarkRankList = new ArrayList<>();

    public RankFragment() {
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
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();
        if (!flag) {
            userRecyclerView.setAdapter(userRankAdapter);
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
                            setUserRankAdapter();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<UserRankResult> call, Throwable t) {
                Log.d(TAG, "onFailure");
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

    private void initRecyclerView() {
        userRecyclerView.setHasFixedSize(true);
        userLayoutManager = new LinearLayoutManager(getContext());
        userLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        userRecyclerView.setLayoutManager(userLayoutManager);


        landmarkRecyclerView.setHasFixedSize(true);
        landmarkLayoutManager = new LinearLayoutManager(getContext());
        landmarkLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        landmarkRecyclerView.setLayoutManager(landmarkLayoutManager);

    }

    private void setUserRankAdapter() {
        userRankAdapter = new UserRankAdapter(getContext(), userRankList);
        userRankAdapter.notifyDataSetChanged();
        userRecyclerView.setAdapter(userRankAdapter);
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
