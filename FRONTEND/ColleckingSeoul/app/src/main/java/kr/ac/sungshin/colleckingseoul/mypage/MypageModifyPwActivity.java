package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.home.HomeActivity;
import kr.ac.sungshin.colleckingseoul.login.LoginActivity;
import kr.ac.sungshin.colleckingseoul.model.request.PassWordInfo;
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class MypageModifyPwActivity extends AppCompatActivity {
    @BindView(R.id.mypage_pw_button_cancel)
    Button buttonCancel;
    @BindView(R.id.mypage_pw_button_store)
    Button buttonStore;
    @BindView(R.id.mypage_pw_edittext_now_password)
    EditText editTextNowPassword;
    @BindView(R.id.mypage_pw_edittext_new_password)
    EditText editTextNewPassword;
    @BindView(R.id.mypage_pw_edittext_new_repassword)
    EditText editTextNewRepassword;

    private NetworkService service;
    private final String TAG = "MyPageModifyPWActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage_modify_pw);

        ButterKnife.bind(this);
        service = ApplicationController.getInstance().getNetworkService();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(actionBar.getDisplayOptions() | ActionBar.DISPLAY_SHOW_CUSTOM);
        ImageView actionBarimageView = new ImageView(actionBar.getThemedContext());
        actionBarimageView.setScaleType(ImageView.ScaleType.CENTER);
        actionBarimageView.setImageResource(R.drawable.main_title_cloeckingseoul);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        actionBarimageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(actionBarimageView);

        bindClickListener();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //클릭 이벤트 바인딩
    public void bindClickListener() {
        //취소
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //변경
        buttonStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkValid(editTextNowPassword.getText().toString(), editTextNewPassword.getText().toString(), editTextNewRepassword.getText().toString()))
                    return;

                PassWordInfo info = new PassWordInfo(editTextNowPassword.getText().toString(),
                        editTextNewPassword.getText().toString(), editTextNewRepassword.getText().toString());

                Call<BaseResult> getUpdatePWResult = service.getUpdatePWResult(info);
                getUpdatePWResult.enqueue(new Callback<BaseResult>() {
                    @Override
                    public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            Log.d("MyPage", message);
                            switch (message) {
                                case "EMPTY_VALUE":
                                    Toast.makeText(getBaseContext(), "입력하신 값이 없습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "NULL_VALUE":
                                    Toast.makeText(getBaseContext(), "받아야할 파라메터가 없습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "INCORRECT_PASSWORD":
                                    Toast.makeText(getBaseContext(), "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "NOT_CORRESPOND":
                                    Toast.makeText(getBaseContext(), "새로운 두 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "SUCCESS":
                                    Toast.makeText(getBaseContext(), "비밀번호가 변경되었습니다. 다시 로그인 해 주세요", Toast.LENGTH_SHORT).show();
                                    goLogin();
                                    deleteInfo();
                                    break;

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResult> call, Throwable t) {

                    }
                });


            }
        });
    }

    //유효성 체크
    public boolean checkValid(String password, String newpassword, String newrepassword) {
        // 빈칸 체크
        if (password.equals("") || newpassword.equals("") || newrepassword.equals("")) {
            Toast.makeText(getBaseContext(), "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        //비밀번호 일치 체크
        if (!newpassword.equals(newrepassword)) {
            Toast.makeText(getBaseContext(), "비밀번호와 비밀번호확인이 일치하지 않습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        //비밀번호 형식 체크
        if (!newpassword.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")) {
            Toast.makeText(getBaseContext(), "비밀번호는 8자리이상 영문 숫자 조합입니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void goLogin() {
        Intent intent = new Intent(getBaseContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void deleteInfo() {
        SharedPreferences.Editor prefs = getSharedPreferences("user", MODE_PRIVATE).edit();
        prefs.remove("user");
        prefs.commit();
    }
}
