## 주요코드

### Retrofit2 + OkHttp3
`HTTP API 통신을 이용해 서버에게 요청을 보내기 위해 사용한 라이브러리`입니다.  

1. 먼저 **build.gradle** 파일에 아래의 코드를 추가합니다.
```
dependencies {
    ...
    implementation 'com.squareup.retrofit2:retrofit:2.1.0'
    implementation 'com.squareup.retrofit2:converter-gson:2.1.0'
    ...
}
```  

2. **AndroidManifest.xml** 파일에 **Application class**를 등록합니다. 저의 경우, .network 패키지에 있는 ApplicationController입니다.
```xml
<application
        android:name=".network.ApplicationController"
        ...>
        ...
</application>
```  

  
3. **ApplicationController.java** 파일에 Retrofit 객체를 생성합니다.
```java
public class ApplicationController extends Application {
    private static ApplicationController instance;
    private static String baseUrl = "http://ipAddress:portNumber";
    private NetworkService networkService;
    private static Retrofit retrofit;
    
    public static ApplicationController getInstance() { return instance; }

    public NetworkService getNetworkService() { return networkService; }


    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationController.instance = this; //인스턴스 객체 초기화
        buildService();
    }

    public void buildService() {
        Retrofit.Builder builder = new Retrofit.Builder();
        retrofit = builder
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build(); // 빌더 패턴

        networkService = retrofit.create(NetworkService.class);
    }
}
```  


4. API를 나타낼 **NetworkService 인터페이스**를 생성합니다.
```java
public interface NetworkService {
    @POST("/login")
    Call<LoginResult> getLoginResult(@Body BasicLogin login); // LoginResult는 response, BasicLogin은 requestbody 내용입니다.
    ...
}
```  


5. 실제로 **서버와 HTTP 통신**하는 코드 예제는 아래와 같습니다.
```java
loginButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String loginId = idEditText.getText().toString().trim();
        String loginPassword = passwordEditText.getText().toString().trim();

        BasicLogin info = new BasicLogin(loginId, loginPassword);
        Call<LoginResult> checkLogin = service.getLoginResult(info);

        checkLogin.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                if (response.isSuccessful()) {
                // 통신 성공했을 때 수행해야 할 코드를 작성합니다.
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                // 통신 실패 시 수행해야 할 코드를 작성합니다.
            }
        });
    }
});
```
