package kr.ac.sungshin.colleckingseoul.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;

import kr.ac.sungshin.colleckingseoul.login.LoginActivity;
import kr.ac.sungshin.colleckingseoul.mypage.MyPageFragment;
import kr.ac.sungshin.colleckingseoul.rank.RankFragment;


public class HomeActivity extends AppCompatActivity {
    @BindView(R.id.home_tablayout_tablayout)
    TabLayout tabLayout;
    @BindView(R.id.home_viewpager_viewpager)
    ViewPager viewPager;

    String TAG = "HomeActivity";
    //Back 키 두번 클릭 여부 확인
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private final int LANDMARK = 0;
    private final int RANKING = 1;
    private final int MYPAGE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int drawable;
                switch (tab.getPosition()) {
                    case LANDMARK:
                        drawable = R.drawable.navigation_button_landmark_on;
                        break;
                    case RANKING:
                        drawable = R.drawable.navigation_button_ranking_on;
                        break;
                    default:
                        drawable = R.drawable.navigation_button_mypage_on;
                        break;
                }
                tab.setIcon(drawable);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int drawable;
                switch (tab.getPosition()) {
                    case LANDMARK:
                        drawable = R.drawable.navigation_button_landmark_off;
                        break;
                    case RANKING:
                        drawable = R.drawable.navigation_button_ranking_off;
                        break;
                    default:
                        drawable = R.drawable.navigation_button_mypage_off;
                        break;
                }
                tab.setIcon(drawable);

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabReselected : " + tab.getPosition());

            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(LANDMARK).setIcon(R.drawable.navigation_button_landmark_on).setCustomView(R.layout.item_custom_icon);
        tabLayout.getTabAt(RANKING).setIcon(R.drawable.navigation_button_ranking_off).setCustomView(R.layout.item_custom_icon);
        tabLayout.getTabAt(MYPAGE).setIcon(R.drawable.navigation_button_mypage_off).setCustomView(R.layout.item_custom_icon);
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MapFragment());
        adapter.addFrag(new RankFragment());
        adapter.addFrag(new MyPageFragment());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            this.backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 키를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();

        private ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        private void addFrag(Fragment fragment) {
            fragmentList.add(fragment);
        }
    }
}
