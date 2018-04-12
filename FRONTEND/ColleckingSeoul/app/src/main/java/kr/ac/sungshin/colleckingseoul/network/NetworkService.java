package kr.ac.sungshin.colleckingseoul.network;

<<<<<<< Updated upstream
import kr.ac.sungshin.colleckingseoul.model.request.Join;
import kr.ac.sungshin.colleckingseoul.model.request.Login;
import kr.ac.sungshin.colleckingseoul.model.response.LoginResult;
import kr.ac.sungshin.colleckingseoul.model.response.Message;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
=======
import kr.ac.sungshin.colleckingseoul.model.request.Login;
import kr.ac.sungshin.colleckingseoul.model.response.LoginResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
>>>>>>> Stashed changes

/**
 * Created by kwonhyeon-a on 2018. 3. 28..
 */

public interface NetworkService {
<<<<<<< Updated upstream
    @POST("/login")
    Call<LoginResult> getLoginResult(@Body Login login);
    @POST("/join")
    Call<Message> getJoinResult(@Body Join join);
    @GET("/join/check_dupplicate")
    Call<Message> getDuplicatedResult (@Query("id") String id);
=======
    @POST("/login/sns")
    Call<LoginResult> getLoginResult(@Body Login login);
>>>>>>> Stashed changes
}
