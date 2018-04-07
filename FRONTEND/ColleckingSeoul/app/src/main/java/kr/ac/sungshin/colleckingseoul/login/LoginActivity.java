package kr.ac.sungshin.colleckingseoul.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.MainActivity;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.model.request.Login;
import kr.ac.sungshin.colleckingseoul.model.response.LoginResult;
import kr.ac.sungshin.colleckingseoul.model.response.User;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.login_button_kakao)
    Button kakaoButton;
    @BindView(R.id.login_button_facebook)
    Button facebookButton;
    @BindView(R.id.login_TextView_join)
    TextView joinTextView;
    @BindView(R.id.login_button_login)
    Button loginButton;
    @BindView(R.id.login_editText_id)
    EditText idEditText;
    @BindView(R.id.login_editText_password)
    EditText passwordEditText;
    @BindView(R.id.login_TextView_findInfo)
    TextView findInfoTextView;


    private NetworkService service;
    private final String TAG = "LoginActivity";
    private CallbackManager facebookCallbackManager;
    SessionCallback callbackForKakao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        service = ApplicationController.getInstance().getNetworkService();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = idEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                final SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
                final SharedPreferences.Editor editor = userInfo.edit();

                if (!checkValid(id, password)) return;

                Login info = new Login(id,password);
                Call<LoginResult> checkLogin = service.getLoginResult(info);
                checkLogin.enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if(response.isSuccessful()){
                            String message = response.body().getMessage();
                            if(message.equals("EMPTY_VALUE")){
                                Toast.makeText(getBaseContext(), "입력하신 값이 없습니다.", Toast.LENGTH_SHORT).show();
                            }else if(message.equals("NULL_VALUE")){
                                Toast.makeText(getBaseContext(), "받아야할 값이 없습니다.", Toast.LENGTH_SHORT).show();
                            }else if(message.equals("NOT_SIGN_UP")){
                                Toast.makeText(getBaseContext(), "가입하신 정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }else if(message.equals("INCORRECT_PASSWORD")){
                                Toast.makeText(getBaseContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                            }else if(message.equals("IS_SNS_ACCOUNT")){
                                Toast.makeText(getBaseContext(), "SNS계정 입니다.", Toast.LENGTH_SHORT).show();
                            }else if(message.equals("FAILURE")){
                                Toast.makeText(getBaseContext(), "서버의 알수없는 에러입니다. 죄송합니다", Toast.LENGTH_SHORT).show();
                            }else if(message.equals("SUCCESS")){
                                User user = response.body().getUser();

                                editor.putString("idx", user.getIdx());
                                editor.putString("id", user.getId());
                                editor.putString("nickname", user.getNikname());
                                editor.putString("phone", user.getPhone());
                                editor.putString("birth", user.getBirth());
                                editor.apply();

                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {

                    }
                });


            }
        });

        kakaoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginOnKakao();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onclick 메소드");
                loginOnFacebook(view);
            }
        });

        joinTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 페이지로 이동
                Log.i(TAG, "클릭");
                Intent intent = new Intent(getBaseContext(), JoinActivity.class);
                startActivity(intent);
            }
        });
        findInfoTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 페이지로 이동
                Log.i(TAG, "클릭");
                Intent intent = new Intent(getBaseContext(), FindInfoActivity.class);
                startActivity(intent);
            }
        });
    }

    //유효성 체크
    public boolean checkValid(String id, String password) {
        if (id.equals("") || password.equals("")) {
            Toast.makeText(getBaseContext(), "아이디와 패스워드를 입력해 주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (id.equals("") || !id.matches("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$")) {
            Toast.makeText(getApplicationContext(), "이메일 형식이 맞지 않습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
            return true;
    }

    private void loginOnKakao() {
        // 카카오 세션을 오픈한다
        callbackForKakao = new SessionCallback();
        Session.getCurrentSession().addCallback(callbackForKakao);
        Session.getCurrentSession().checkAndImplicitOpen();
        Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginActivity.this);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d("TAG", "세션 오픈됨");
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            KakaorequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.d("TAG", exception.getMessage());
            }
        }
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void KakaorequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("TAG", "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.e(TAG, userProfile.toString());
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
            }
        });
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e("name not found", e.toString());
        }
    }

    //facebook Login
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        facebookCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //facebook Login
    public void loginOnFacebook(View v) {
        FacebookSdk.sdkInitialize(v.getContext());
        facebookCallbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                Arrays.asList("public_profile", "email"));
        LoginManager.getInstance().registerCallback(facebookCallbackManager, new FacebookCallback<com.facebook.login.LoginResult>() {

            @Override
            public void onSuccess(final com.facebook.login.LoginResult result) {

                GraphRequest request;
                request = GraphRequest.newMeRequest(result.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject user, GraphResponse response) {
                        if (response.getError() != null) {

                        } else {
                            Log.i("TAG", "user: " + user.toString());

                            Log.i("TAG", "AccessToken: " + result.getAccessToken().getToken());
                            setResult(RESULT_OK);

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("test", "Error: " + error);
                //finish();
            }

            @Override
            public void onCancel() {
                //finish();
            }
        });
    }
}
