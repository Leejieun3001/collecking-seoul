package kr.ac.sungshin.colleckingseoul.network;


import kr.ac.sungshin.colleckingseoul.model.request.BasicLogin;
import kr.ac.sungshin.colleckingseoul.model.request.FindPassWord;
import kr.ac.sungshin.colleckingseoul.model.response.DefaultResult;
import kr.ac.sungshin.colleckingseoul.model.response.FindInfoResult;
import kr.ac.sungshin.colleckingseoul.model.request.Login;
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
import kr.ac.sungshin.colleckingseoul.model.request.FindId;
import kr.ac.sungshin.colleckingseoul.model.response.LandmarkListResult;
import kr.ac.sungshin.colleckingseoul.model.response.LoginResult;
import kr.ac.sungshin.colleckingseoul.model.response.VerificationCodeResult;
import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;
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
                                   @Part("sex") RequestBody sex,
                                   @Part MultipartBody.Part photo);

    @GET("/join/check_dupplicate")
    Call<BaseResult> getDuplicatedResult(@Query("id") String id);
    @POST("/login/sns")
    Call<LoginResult> getSnsLoginResult(@Body Login login);

    @GET("/join/verificationCode")
    Call<VerificationCodeResult> getVerifiCodeResult(@Query("tempEmail") String id);

    @POST("/login/find_id")
    Call<FindInfoResult> getFindIdResult(@Body FindId Info);

    @POST("/login/find_password")
    Call<FindInfoResult> getFindPwResult(@Body FindPassWord Info);

    @GET("/landmark/landmark_list")
    Call<LandmarkListResult> getLandmarkList();

    @POST("/landmark")
    Call<BaseResult> getInsertResult(@Query("sql") String sql);

    @POST("/board/write")
    @Multipart
    Call<DefaultResult> getWritingBoardResult(@Part("title") RequestBody title,
                                              @Part("content") RequestBody content,
                                              @Part("landmark_idx") RequestBody landmark_idx,
                                              @Part MultipartBody.Part photo);


}
