package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.model.response.MyLandmarkResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import kr.ac.sungshin.colleckingseoul.rank.UserRankAdapter;
import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

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
    @BindView(R.id.mypage_textview_memberNickname)
    TextView textViewMemberNickname;
    @BindView(R.id.mypage_linearLayout_null)
    LinearLayout linearLayoutNull;


    private boolean flag = true;

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
        if (!flag) {
            SharedPreferences userInfo = getActivity().getSharedPreferences("user", MODE_PRIVATE);
            String userNickname = userInfo.getString("nickname", "");
            textViewMemberNickname.setText(userNickname);
            recyclerViewMyLandmark.setAdapter(myPageAdapter);
            if (MyVisitList.isEmpty()) {
                recyclerViewMyLandmark.setVisibility(View.GONE);
                linearLayoutNull.setVisibility(View.VISIBLE);
            }
            bindClickListener();
            return view;
        }
        flag = false;

        SharedPreferences userInfo = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        String userNickname = userInfo.getString("nickname", "");
        textViewMemberNickname.setText(userNickname);
        getVisitList();
        bindClickListener();
        return view;
    }

    private void initRecyclerView() {
        recyclerViewMyLandmark.setHasFixedSize(false);
        myPageLayoutManager = new LinearLayoutManager(getContext());
        myPageLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMyLandmark.setLayoutManager(myPageLayoutManager);
        recyclerViewMyLandmark.setNestedScrollingEnabled(false);
    }

    private void setAdapter() {
        myPageAdapter = new MyPageAdapter(getContext(), MyVisitList);
        myPageAdapter.notifyDataSetChanged();
        recyclerViewMyLandmark.setAdapter(myPageAdapter);
    }


    public void getVisitList() {

        Call<MyLandmarkResult> getMyLandmarkList = service.getMyLandmarkList();

        getMyLandmarkList.enqueue(new Callback<MyLandmarkResult>() {
            @Override
            public void onResponse(Call<MyLandmarkResult> call, Response<MyLandmarkResult> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    switch (message) {
                        case "SUCCESS":
                            MyVisitList.addAll(response.body().getLandmarks());
                            if (MyVisitList.isEmpty()) {
                                recyclerViewMyLandmark.setVisibility(View.GONE);
                                linearLayoutNull.setVisibility(View.VISIBLE);
                            }
                            setAdapter();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<MyLandmarkResult> call, Throwable t) {

            }
        });

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
