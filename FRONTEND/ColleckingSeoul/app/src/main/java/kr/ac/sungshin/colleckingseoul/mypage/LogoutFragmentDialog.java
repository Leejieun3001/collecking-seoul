package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.login.LoginActivity;
import kr.ac.sungshin.colleckingseoul.model.response.User;
import kr.ac.sungshin.colleckingseoul.model.singleton.InfoManager;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;

import static android.content.Context.MODE_PRIVATE;

public class LogoutFragmentDialog extends DialogFragment {
    @BindView(R.id.logoutfragmentdialog_button_cancle)
    Button buttonCancle;
    @BindView(R.id.logoutfragmentdialog_button_logout)
    Button buttonLogout;

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

        buttonCancle.setOnClickListener(new View.OnClickListener() {
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
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    public void deleteInfo() {
        SharedPreferences.Editor prefs = getActivity().getSharedPreferences("user", MODE_PRIVATE).edit();
        prefs.remove("user");
        prefs.commit();
    }

}
