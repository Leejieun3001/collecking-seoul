package kr.ac.sungshin.colleckingseoul.mypage;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.util.exception.KakaoException;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.home.HomeActivity;
import kr.ac.sungshin.colleckingseoul.login.LoginActivity;
import kr.ac.sungshin.colleckingseoul.login.SessionCallback;

import static android.content.Context.MODE_PRIVATE;

public class LogoutFragmentDialog extends DialogFragment {
    @BindView(R.id.logoutfragmentdialog_button_cancle)
    Button buttonCancel;
    @BindView(R.id.logoutfragmentdialog_button_logout)
    Button buttonLogout;

    SessionCallback callbackForKakao;
    int snsCategory;
    final int NOMAL     = 0;
    final int FACEBOOK  = 1;
    final int KAKAO     = 2;

    private static String TAG = "LogoutFragmentDialog";

    public LogoutFragmentDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logout_fragment_dialog, container, false);
        ButterKnife.bind(this, view);

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
        final SharedPreferences userInfo = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        this.snsCategory = userInfo.getInt("snsCategory", 0);
        return dialog;
    }

    public void bindClickListener() {

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag("logout");
                if (fragment != null) {
                    DialogFragment dialogFragment = (DialogFragment) fragment;
                    dialogFragment.dismiss();
                }

            }
        });
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteInfo();
                switch (snsCategory) {
                    case FACEBOOK: LoginManager.getInstance().logOut(); goLogin(); break;
                    case KAKAO:    logoutKakao(); break;
                    default:       goLogin(); break;
                }
            }
        });
    }

    private void logoutKakao() {
        callbackForKakao = SessionCallback.getInstance();
        callbackForKakao.setInfo(getActivity(), "LOGOUT");
        callbackForKakao.setDialog(this);
        Session.getCurrentSession().addCallback(callbackForKakao);
        Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, getActivity());
    }

    public void goLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        getActivity().finish();
        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
    }

    public void deleteInfo() {
        SharedPreferences userInfo = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        userInfo.edit().clear().commit();
    }

}
