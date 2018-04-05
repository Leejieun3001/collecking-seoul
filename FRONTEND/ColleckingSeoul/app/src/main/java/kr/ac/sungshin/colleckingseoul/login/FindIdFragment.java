package kr.ac.sungshin.colleckingseoul.login;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;


public class FindIdFragment extends Fragment {
    private NetworkService service;
    private final String TAG = "FindIdFragment";

    public FindIdFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_find_id, container, false);
        service = ApplicationController.getInstance().getNetworkService();
        ButterKnife.bind(this, view);
        RelativeLayout Layout = (RelativeLayout) inflater.inflate(R.layout.fragment_find_id, container, false);

        // Inflate the layout for this fragment
        return Layout;
    }
}


