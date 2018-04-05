
package kr.ac.sungshin.colleckingseoul.login;


import android.os.Bundle;

import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import kr.ac.sungshin.colleckingseoul.R;

public class FindInfoActivity extends AppCompatActivity {


    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_info);
        vp = (ViewPager) findViewById(R.id.findinfo_viewpager_viewpager);

        vp.setAdapter(new pagerAdapter(getSupportFragmentManager()));
        vp.setCurrentItem(0);

        Button buttonFindid = (Button)findViewById(R.id.findinfo_button_id);
        Button buttonFindPassword = (Button)findViewById(R.id.findinfo_button_password);


        buttonFindid.setOnClickListener(movePageListener);
        buttonFindid.setTag(0);
        buttonFindPassword.setOnClickListener(movePageListener);
        buttonFindPassword.setTag(1);

    }

    View.OnClickListener movePageListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int tag = (int) v.getTag();
            vp.setCurrentItem(tag);
        }
    };

    private class pagerAdapter extends FragmentStatePagerAdapter {
        public pagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new FindIdFragment();
                case 1:
                    return new FindPwFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}

