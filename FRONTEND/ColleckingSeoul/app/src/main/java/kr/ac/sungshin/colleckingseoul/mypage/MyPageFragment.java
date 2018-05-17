package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;

/**
 * Created by kwonhyeon-a on 2018. 5. 15..
 */

public class MyPageFragment extends android.support.v4.app.Fragment {
    @BindView(R.id.mypage_textview_modify)
    TextView textViewModify;
    @BindView(R.id.mypage_textview_logout)
    TextView textViewLogout;
    @BindView(R.id.mypage_textview_leave)
    TextView textLeave;

    public MyPageFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mypage, container, false);
        ButterKnife.bind(this, view);
        textViewModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPageModifyActivity.class);
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

        return view;
    }
}
