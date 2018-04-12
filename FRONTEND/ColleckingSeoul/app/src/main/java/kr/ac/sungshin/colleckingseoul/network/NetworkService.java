package kr.ac.sungshin.colleckingseoul.network;

import kr.ac.sungshin.colleckingseoul.model.request.Login;
import kr.ac.sungshin.colleckingseoul.model.request.Join;
import kr.ac.sungshin.colleckingseoul.model.response.LoginResult;
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by kwonhyeon-a on 2018. 3. 28..
 */

public interface NetworkService {
    @POST("/login")
    Call<LoginResult> getLoginResult(@Body Login login);
    @POST("/join")
    Call<BaseResult> getJoinResult(@Body Join join);
    @GET("/join/check_dupplicate")
    Call<BaseResult> getDuplicatedResult (@Query("id") String id);
    @POST("/login/sns")
    Call<LoginResult> getSnsLoginResult(@Body Login login);
}
