package kr.ac.sungshin.colleckingseoul.home;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.R;
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
    String TAG = "RankFragment";
    private NetworkService service;
    private ArrayList<UserRank> userRankList = new ArrayList<>();
    private ArrayList<LandmarkRank> landmarkRankList = new ArrayList<>();

    public RankFragment() {
        super();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = ApplicationController.getInstance().getNetworkService();
        Call<UserRankResult> getUserRankList = service.getUserRankList();
        getUserRankList.enqueue(new Callback<UserRankResult>() {
            @Override
            public void onResponse(Call<UserRankResult> call, Response<UserRankResult> response) {
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    switch (message) {
                        case "SUCCESS":
                            userRankList.addAll(response.body().getUserRankList());
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
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    switch (message) {
                        case "SUCCESS":
                            landmarkRankList.addAll(response.body().getLandmarkRankList());
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<LandmarkRankResult> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        return view;
    }
}
