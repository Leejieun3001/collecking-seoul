package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.home.HomeActivity;

/**
 * Created by kwonhyeon-a on 2018. 5. 15..
 */

public class MyPageFragment extends Fragment {
    @BindView(R.id.mypage_textview_modify)
    TextView textViewModify;
    @BindView(R.id.mypage_textview_logout)
    TextView textViewLogout;
    @BindView(R.id.mypage_textview_secession)
    TextView textSecession;

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


            }
        });

        return view;
    }
}
