package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.Review.BoardItem;
import kr.ac.sungshin.colleckingseoul.model.response.MyLandmarkResult;
import kr.ac.sungshin.colleckingseoul.model.response.rank.UserRank;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import kr.ac.sungshin.colleckingseoul.rank.LandmarkRankAdapter;
import kr.ac.sungshin.colleckingseoul.rank.UserRankAdapter;
import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kwonhyeon-a on 2018. 5. 15..
 */

public class MyPageFragment extends android.support.v4.app.Fragment {
    @BindView(R.id.mypage_textview_modify_info)
    TextView textViewModifyInfo;
    @BindView(R.id.mypage_textview_modify_pw)
    TextView TextViewModifyPw;
    @BindView(R.id.mypage_textview_logout)
    TextView textViewLogout;
    @BindView(R.id.mypage_textview_leave)
    TextView textLeave;
    @BindView(R.id.my_landmark_recyclerView_userrecyclerview)
    RecyclerView recyclerViewMyLandmark;

    String TAG = "MyPageFragment";
    private NetworkService service;

    private MyPageAdapter myPageAdapter;
    private LinearLayoutManager myPageLayoutManager;
    private ArrayList<Landmark> MyVisitList = new ArrayList<>();

    public MyPageFragment() {
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

        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        ButterKnife.bind(this, view);
        initRecyclerView();

        Call<MyLandmarkResult> getMyLandmarkList = service.getMyLandmarkList();
        getMyLandmarkList.enqueue(new Callback<MyLandmarkResult>() {
            @Override
            public void onResponse(Call<MyLandmarkResult> call, Response<MyLandmarkResult> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    Log.d(TAG, String.valueOf(response.body().getMessage()));
                    switch (message) {
                        case "SUCCESS":
                            MyVisitList.addAll(response.body().getLandmarks());
                            if (MyVisitList.isEmpty()) {
                                //BoardItem d = new BoardItem(0,0,0,"","","","","");
                                //d.setContent("아직 등록된 리뷰가 없습니다.");
                                //MyVisitList.add(d);
                            }
                            Log.d(TAG, String.valueOf(response.body().getMessage()));
                            setMypageAdapter();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<MyLandmarkResult> call, Throwable t) {

            }
        });


        ButterKnife.bind(this, view);
        bindClickListener();

        return view;
    }

    private void initRecyclerView() {
        recyclerViewMyLandmark.setHasFixedSize(true);
        myPageLayoutManager = new LinearLayoutManager(getContext());
        myPageLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMyLandmark.setLayoutManager(myPageLayoutManager);
    }

    private void setMypageAdapter() {
        myPageAdapter = new MyPageAdapter(getContext(), MyVisitList);
        myPageAdapter.notifyDataSetChanged();
        recyclerViewMyLandmark.setAdapter(myPageAdapter);
    }

    public void bindClickListener() {
        textViewModifyInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPageModifyActivity.class);
                startActivity(intent);
            }
        });
        TextViewModifyPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MypageModifyPwActivity.class);
                startActivity(intent);
            }
        });

        textViewLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("key", "value");
                LogoutFragmentDialog dialog = new LogoutFragmentDialog();
                dialog.setArguments(args);
                dialog.show(getActivity().getSupportFragmentManager(), "logout");

            }
        });
        textLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("key", "value");
                MemberLeaveFragmentDialog dialog = new MemberLeaveFragmentDialog();
                dialog.setArguments(args);
                dialog.show(getActivity().getSupportFragmentManager(), "leave");
            }
        });

    }
}
