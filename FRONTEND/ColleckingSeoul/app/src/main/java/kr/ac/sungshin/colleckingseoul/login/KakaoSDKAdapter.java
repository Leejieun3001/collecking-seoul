package kr.ac.sungshin.colleckingseoul.login;

import android.app.Activity;
import android.content.Context;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;

import kr.ac.sungshin.colleckingseoul.network.ApplicationController;

/**
 * Created by kwonhyeon-a on 2018. 3. 28..
 */

public class KakaoSDKAdapter extends KakaoAdapter {
    public KakaoSDKAdapter() {
        super();
    }

    @Override
    public ISessionConfig getSessionConfig() {
        return new ISessionConfig() {
            @Override
            public AuthType[] getAuthTypes() {
                return new AuthType[] {AuthType.KAKAO_ACCOUNT};
            }

            @Override
            public boolean isUsingWebviewTimer() {
                return false;
            }

            @Override
            public boolean isSecureMode() {
                return false;
            }


            @Override
            public ApprovalType getApprovalType() {
                return ApprovalType.INDIVIDUAL;
            }

            @Override
            public boolean isSaveFormData() {
                return true;
            }
        };
    }

    @Override
    public IApplicationConfig getApplicationConfig() {
        return new IApplicationConfig() {

            @Override
            public Context getApplicationContext() {
                return ApplicationController.getGlobalApplicationContext();
            }
        };
    }

}
