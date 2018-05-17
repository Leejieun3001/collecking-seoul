package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Context;
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

public class LogoutFragmentDialog extends DialogFragment {
    @BindView(R.id.logoutfragmentdialog_button_cancle)
    Button buttonCancle;
    @BindView(R.id.logoutfragmentdialog_button_logout)
    Button buttonLogout;
    public LogoutFragmentDialog() {
        // Required empty public constructor
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

            }
        });


        return view;
    }
}
