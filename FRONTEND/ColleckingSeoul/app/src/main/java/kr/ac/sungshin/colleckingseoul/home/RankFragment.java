package kr.ac.sungshin.colleckingseoul.home;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kr.ac.sungshin.colleckingseoul.R;

/**
 * Created by kwonhyeon-a on 2018. 5. 15..
 */

public class RankFragment extends Fragment {
    public RankFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        return view;
    }
}
