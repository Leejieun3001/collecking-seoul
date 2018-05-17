package kr.ac.sungshin.colleckingseoul.mypage;

import android.app.DialogFragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.zip.Inflater;

import kr.ac.sungshin.colleckingseoul.R;

public class LogoutFragmentDialog extends DialogFragment {
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
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_logout_fragment_dialog, container, false);
        Bundle args = getArguments();
        String value = args.getString("key");


        return view;
    }
}
