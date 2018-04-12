package kr.ac.sungshin.colleckingseoul.network;

import android.app.Activity;
import android.app.Application;

import com.kakao.auth.KakaoSDK;

import java.io.IOException;

import kr.ac.sungshin.colleckingseoul.login.KakaoSDKAdapter;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kwonhyeon-a on 2018. 3. 28..
 */

public class ApplicationController extends Application {
    private static ApplicationController instance;
    private static String baseUrl = "http://52.78.38.162:3000";

    private static volatile Activity currentActivity = null;
    private NetworkService networkService;
    private static Retrofit retrofit;



    public static ApplicationController getInstance() {
        return instance;
    }

    public NetworkService getNetworkService() {
        return networkService;
    }    // 네트워크서비스 객체 반환


    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationController.instance = this; //인스턴스 객체 초기화
        buildService();
        KakaoSDK.init(new KakaoSDKAdapter());
    }

    public void buildService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        networkService = retrofit.create(NetworkService.class);
    }

    public void setTokenOnHeader (String value) {
        final String token = value;
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.addInterceptor(new Interceptor() {
            @Override public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder().addHeader("token", token).build();
                return chain.proceed(request);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder.build())
                .build();

        networkService = retrofit.create(NetworkService.class);
    }

    public static ApplicationController getGlobalApplicationContext() {
        return instance;
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    // Activity가 올라올때마다 Activity의 onCreate에서 호출해줘야한다.
    public static void setCurrentActivity(Activity currentActivity) {
        instance.currentActivity = currentActivity;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
