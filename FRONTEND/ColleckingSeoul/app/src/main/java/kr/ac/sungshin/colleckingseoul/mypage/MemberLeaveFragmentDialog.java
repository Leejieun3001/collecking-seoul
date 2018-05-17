package kr.ac.sungshin.colleckingseoul.mypage;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;

public class MemberLeaveFragmentDialog extends DialogFragment {
    @BindView(R.id.memberleavefragmentdialog_button_cancle)
    Button buttonCancle;
    @BindView(R.id.memberleavefragmentdialog_button_leave)
    Button buttonLeave;

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
        Bundle args = getArguments();
        String value = args.getString("key");


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


            }
        });
        return view;
    }
}

