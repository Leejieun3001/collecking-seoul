package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.home.HomeActivity;
import kr.ac.sungshin.colleckingseoul.login.LoginActivity;

import static android.content.Context.MODE_PRIVATE;

public class LogoutFragmentDialog extends DialogFragment {
    @BindView(R.id.logoutfragmentdialog_button_cancle)
    Button buttonCancel;
    @BindView(R.id.logoutfragmentdialog_button_logout)
    Button buttonLogout;

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
                goLogin();
            }
        });
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
