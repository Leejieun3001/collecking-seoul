package kr.ac.sungshin.colleckingseoul.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.kakao.auth.ISessionCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.util.exception.KakaoException;

import kr.ac.sungshin.colleckingseoul.mypage.LogoutFragmentDialog;
import retrofit2.http.PUT;

/**
 * Created by gwonhyeon-a on 2018. 6. 6..
 */

public class SessionCallback implements ISessionCallback {
    private static SessionCallback instance = null;
    private final String TAG = "SessionCallback";
    private String activityName = "";
    private Context context;

    private SessionCallback () {

    }

    public static SessionCallback getInstance () {
        if (instance == null) instance = new SessionCallback();
        return instance;
    }

    private LogoutFragmentDialog logoutFragmentDialog;

    public void setInfo (Context context, String activityName) {
        this.context = context;
        this.activityName = activityName;
    }

    public void setDialog (LogoutFragmentDialog logoutFragmentDialog) {
        this.logoutFragmentDialog = logoutFragmentDialog;
    }

    @Override
    public void onSessionOpened() {
        Log.d(TAG, "세션 오픈됨 : ");
        // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
        switch (activityName) {
            case "LOGIN":  ((LoginActivity) context).requestMeOnKakao(); break;
            case "LOGOUT": logout(); break;
        }

    }

    public void logout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                Log.d(TAG, "로그아웃 완료");
                if (activityName.equals("LOGOUT")) {
                    logoutFragmentDialog.goLogin();
                }
            }
        });
    }

    @Override
    public void onSessionOpenFailed(KakaoException exception) {
        if (exception != null) {
            Log.d(TAG, exception.getMessage());
        }
    }
}
