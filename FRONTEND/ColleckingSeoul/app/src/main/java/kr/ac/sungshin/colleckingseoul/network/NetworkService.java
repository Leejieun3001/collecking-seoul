package kr.ac.sungshin.colleckingseoul.network;

import kr.ac.sungshin.colleckingseoul.model.request.Login;
import kr.ac.sungshin.colleckingseoul.model.request.Join;
import kr.ac.sungshin.colleckingseoul.model.response.LoginResult;
<<<<<<< Updated upstream
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
=======
import kr.ac.sungshin.colleckingseoul.model.response.Message;
import kr.ac.sungshin.colleckingseoul.model.response.VerificationCodeResult;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
>>>>>>> Stashed changes
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
    Call<LoginResult> getLoginResult(@Body Login login);

    @POST("/join")
<<<<<<< Updated upstream
    Call<BaseResult> getJoinResult(@Body Join join);
    @GET("/join/check_dupplicate")
    Call<BaseResult> getDuplicatedResult (@Query("id") String id);
    @POST("/login/sns")
    Call<LoginResult> getSnsLoginResult(@Body Login login);
=======
    @Multipart
    Call<Message> getJoinResult(@Part("id") RequestBody id,
                                @Part("password1") RequestBody password1,
                                @Part("password2") RequestBody password2,
                                @Part("phone") RequestBody phone,
                                @Part("nickname") RequestBody nickname,
                                @Part("birth") RequestBody birth,
                                @Part("sex") int sex,
                                @Part MultipartBody.Part photo);

    @GET("/join/check_dupplicate")
    Call<Message> getDuplicatedResult(@Query("id") String id);

    @GET("/join/verificationCode")
    Call<VerificationCodeResult> getVerifiCodeResult(@Query("tempEmail") String id);
>>>>>>> Stashed changes
}
