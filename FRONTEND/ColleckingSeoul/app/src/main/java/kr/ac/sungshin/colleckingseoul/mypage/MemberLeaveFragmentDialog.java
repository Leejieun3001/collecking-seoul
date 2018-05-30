package kr.ac.sungshin.colleckingseoul.mypage;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.MainActivity;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.login.LoginActivity;
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MemberLeaveFragmentDialog extends DialogFragment {
    @BindView(R.id.memberleavefragmentdialog_button_cancle)
    Button buttonCancle;
    @BindView(R.id.memberleavefragmentdialog_button_leave)
    Button buttonLeave;
    private NetworkService service;
    private final String TAG = "memberLeave";

    public MemberLeaveFragmentDialog() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_member_leave_fragment_dialog, container, false);
        ButterKnife.bind(this, view);
        service = ApplicationController.getInstance().getNetworkService();
        Bundle args = getArguments();
        String value = args.getString("key");

        bindClickListener();
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // request a window without the title
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void bindClickListener() {

        buttonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("leave");
                if (fragment != null) {
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }

            }
        });
        buttonLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<BaseResult> getwithdrawResult = service.getwithdrawResult();
                getwithdrawResult.enqueue(new Callback<BaseResult>() {
                    @Override
                    public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            Log.d("MyPage", message);
                            switch (message) {
                                case "NOT_LOGIN":
                                    Toast.makeText(getContext(), "로그인 하지 않은 사용자 입니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "SUCCESS":
                                    Toast.makeText(getContext(), "회원 탈퇴처리가 성공적으로 되었습니다.", Toast.LENGTH_SHORT).show();
                                    goLogin();
                                    deleteInfo();
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResult> call, Throwable t) {

                    }
                });
            }
        });

    }

    public void goLogin() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }

    public void deleteInfo() {
        SharedPreferences userInfo = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        userInfo.edit().clear().commit();
    }
}

