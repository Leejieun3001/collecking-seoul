package kr.ac.sungshin.colleckingseoul.network;

import android.util.Log;

import com.facebook.login.LoginResult;

import kr.ac.sungshin.colleckingseoul.model.request.Join;
import kr.ac.sungshin.colleckingseoul.model.request.Login;
import kr.ac.sungshin.colleckingseoul.model.response.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by kwonhyeon-a on 2018. 3. 28..
 */

public interface NetworkService {
    @POST("/login")
    Call<LoginResult> getLoginResult(@Body Login login);
    @POST("/join")
    Call<Message> getJoinResult(@Body Join join);
}
