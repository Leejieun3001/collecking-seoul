package kr.ac.sungshin.colleckingseoul.network;


import kr.ac.sungshin.colleckingseoul.model.request.BasicLogin;
import kr.ac.sungshin.colleckingseoul.model.request.Login;
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
import kr.ac.sungshin.colleckingseoul.model.response.LoginResult;
import kr.ac.sungshin.colleckingseoul.model.response.VerificationCodeResult;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by kwonhyeon-a on 2018. 3. 28..
 */

public interface NetworkService {
    @POST("/login")
    Call<LoginResult> getLoginResult(@Body BasicLogin login);

    @POST("/join")
    @Multipart
    Call<BaseResult> getJoinResult(@Part("id") RequestBody id,
                                   @Part("password1") RequestBody password1,
                                   @Part("password2") RequestBody password2,
                                   @Part("phone") RequestBody phone,
                                   @Part("nickname") RequestBody nickname,
                                   @Part("birth") RequestBody birth,
                                   @Part("sex") int sex,
                                   @Part MultipartBody.Part photo);

    @GET("/join/check_dupplicate")
    Call<BaseResult> getDuplicatedResult(@Query("id") String id);
    @POST("/login/sns")
    Call<LoginResult> getSnsLoginResult(@Body Login login);

    @GET("/join/verificationCode")
    Call<VerificationCodeResult> getVerifiCodeResult(@Query("tempEmail") String id);
}
