package kr.ac.sungshin.colleckingseoul.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.home.HomeActivity;
import kr.ac.sungshin.colleckingseoul.model.request.RefreshToken;
import kr.ac.sungshin.colleckingseoul.model.response.LoginResult;
import kr.ac.sungshin.colleckingseoul.model.response.User;
import kr.ac.sungshin.colleckingseoul.model.singleton.InfoManager;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.splash_progressbar_progressbar)
    ProgressBar progressBar;

    private static final String TAG = "Splash";
    private NetworkService service;
    private final SplashHandler handler = new SplashHandler(this);
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
            String savedToken = userInfo.getString("token", "");
            if (userInfo.getBoolean("autoLogin", false) && !savedToken.equals("")) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -6);
                Date limitDate = calendar.getTime();
                Date loginDate = new Date(userInfo.getString("loginDate", new Date().toString()));

                if (limitDate.compareTo(loginDate) < 0) {
                    ApplicationController.getInstance().setTokenOnHeader(savedToken);
                    User user = new User(userInfo.getString("idx", ""),
                            userInfo.getString("id", ""),
                            userInfo.getString("nickname", ""),
                            userInfo.getString("phone", ""),
                            userInfo.getString("birth", ""),
                            userInfo.getString("url", ""),
                            userInfo.getInt("sex", 0));
                    InfoManager.getInstance().setUserInfo(user);
                    goHome();
                } else {
                    Call<LoginResult> getRefreshToken = service.getRefreshTokenResult(new RefreshToken(userInfo.getString("idx", "")));

                    getRefreshToken.enqueue(new Callback<LoginResult>() {
                        @Override
                        public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                            if (response.isSuccessful()) {
                                String message = response.body().getMessage();
                                switch (message) {
                                    case "SUCCESS":
                                        User user = response.body().getUser();
                                        String token = response.body().getToken();
                                        saveInfo(user, token);
                                        goHome();
                                        break;
                                    default: goLogin(); break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<LoginResult> call, Throwable t) {
                            Log.d(TAG, "실패");
                        }
                    });
                }
            } else {
                goLogin();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            Drawable wrapDrawable = DrawableCompat.wrap(progressBar.getIndeterminateDrawable());
            DrawableCompat.setTint(wrapDrawable, ContextCompat.getColor(getBaseContext(), R.color.colorAccent));
            progressBar.setIndeterminateDrawable(DrawableCompat.unwrap(wrapDrawable));
        } else {
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getBaseContext(), android.R.color.holo_green_light), PorterDuff.Mode.SRC_IN);
        }

        service = ApplicationController.getInstance().getNetworkService();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        handler.postDelayed(runnable, 2000);
    }

    public void goHome() {
        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void goLogin() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveInfo(User user, String token) {
        final SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        final SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("idx", user.getIdx());
        editor.putString("id", user.getId());
        editor.putString("nickname", user.getNickname());
        editor.putString("phone", user.getPhone());
        editor.putString("birth", user.getBirth());
        editor.putInt("sex", user.getSex());
        editor.putString("url", user.getUrl());
        editor.putInt("snsCategory", user.getSnsCategory());
        editor.putString("token", token);
        editor.putString("loginDate", new Date().toString());
        editor.apply();

        InfoManager.getInstance().setUserInfo(user);
        ApplicationController.getInstance().setTokenOnHeader(token);
    }

    private static class SplashHandler extends Handler {
        private final WeakReference<SplashActivity> mActivity;

        public SplashHandler(SplashActivity activity) {
            mActivity = new WeakReference<SplashActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mActivity.get();
            if (activity != null) {
            }
        }
    }
}
