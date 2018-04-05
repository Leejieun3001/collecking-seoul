package kr.ac.sungshin.colleckingseoul.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.model.request.Join;
import kr.ac.sungshin.colleckingseoul.model.response.Message;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {
    @BindView(R.id.join_button_back)
    Button buttonBack;
    @BindView(R.id.join_edittext_id)
    EditText editTextId;
    @BindView(R.id.join_button_duplication)
    Button buttonDuplication;
    @BindView(R.id.join_button_requestcode)
    Button buttonRequestCode;
    @BindView(R.id.join_edittext_checkcode)
    EditText editTextcheckCode;
    @BindView(R.id.join_button_checkcode)
    Button buttonCheckCode;
    @BindView(R.id.join_edittext_password)
    EditText editTextPassword;
    @BindView(R.id.join_edittext_repassword)
    EditText editTextRepassword;
    @BindView(R.id.join_edittext_nikname)
    EditText editTextNikname;
    @BindView(R.id.join_edittext_phone)
    EditText editTextPhone;
    @BindView(R.id.join_datepicker_birth)
    DatePicker Datepickerbirth;
    @BindView(R.id.join_button_join)
    Button buttonJoin;

    private NetworkService service;
    private final String TAG = "JoinActivity";

    private boolean isDuplicate = false;
    private boolean isCheckEmail = false;
    private String verificationCode = "";

    //Back 키 두번 클릭 여부 확인
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        service = ApplicationController.getInstance().getNetworkService();
        ButterKnife.bind(this);

        bindClickListener();
    }

    //클릭 이벤트 바인딩
    public void bindClickListener() {
        //email 중복 체크
        buttonDuplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();
                if (id.equals("") || !id.matches("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$")) {
                    Toast.makeText(getApplicationContext(), "이메일 형식이 맞지 않습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } /*else {
                    Call<Message> checkId = service.getDupCheckResult(id);
                    checkId.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            if (response.isSuccessful()) {
                                Log.d(TAG, response.body().toString());
                                if (response.body().getMessage().equals("no duplication")) {
                                    Toast.makeText(getApplicationContext(), "사용가능한 이메일 입니다.", Toast.LENGTH_SHORT).show();
                                    isDuplicate = true;
                                }
                                if (response.body().getMessage().equals("duplicated")) {
                                    Toast.makeText(getApplicationContext(), "이미 사용중인 이메일이 존재합니다. 다른 이메일로 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                    isDuplicate = false;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {

                        }
                    });
                }*/
            }
        });
        //이메일 인증번호 요청
        buttonRequestCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();
                if (!isDuplicate) {
                    Toast.makeText(getApplicationContext(), "이메일 중복 체크를 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } /*else {
                    final Call<VerificationCodeResult> checkNumber = service.getVerifiCodeResult(id);
                    checkNumber.enqueue(new Callback<VerificationCodeResult>() {
                        @Override
                        public void onResponse(Call<VerificationCodeResult> call, Response<VerificationCodeResult> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getMessage().equals("duplicated")) {
                                    Toast.makeText(getApplicationContext(), "이미 사용중인 이메일이 존재합니다. 다른 이메일로 시도해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                                if (response.body().getMessage().equals("failuire")) {
                                    Toast.makeText(getApplicationContext(), "알수 없는 오류 입니다.", Toast.LENGTH_SHORT).show();
                                }
                                if (response.body().getMessage().equals("email success")) {
                                    verificationCode = response.body().getVerificationCode();
                                    Toast.makeText(getApplicationContext(), "인증번호가 메일로 발송되었습니다. 확인후 입력해 주세요.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<VerificationCodeResult> call, Throwable t) {

                        }
                    });
                }*/
            }
        });

        //인증번호 확인
        buttonCheckCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = editTextcheckCode.getText().toString();
                if (code.equals(verificationCode)) {
                    isCheckEmail = true;
                    Toast.makeText(getApplicationContext(), "인증 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //회원가입 완료
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();
                String password = editTextPassword.getText().toString();
                final String repassword = editTextRepassword.getText().toString();
                String nikname = editTextNikname.getText().toString();
                String phone = editTextPhone.getText().toString();
                String birth = Integer.toString(Datepickerbirth.getYear()) + Integer.toString(Datepickerbirth.getMonth()) + Integer.toString(Datepickerbirth.getDayOfMonth());


                if (!checkValid(id, password, repassword, nikname, phone, birth))
                    return;
                Join Info = new Join(id, password,repassword, nikname, phone, birth );
                Call<Message> getJoinResult = service.getJoinResult(Info);

                getJoinResult.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        if(response.isSuccessful()){
                            if(response.body().getMessage().equals("signup success")){
                                Toast.makeText(getApplicationContext(), "회원가입이 성공적으로 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(getApplicationContext(), "죄송합니다. 오류가 발생하였습니다. 빠른시일 내에 개선하겠습니다.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {

                    }
                });
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    //유효성 체크

    public boolean checkValid(String id, String password, String repassword, String name, String phone, String birth) {
        // 빈칸 체크
        if (id.equals("")) {
            Toast.makeText(getBaseContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.equals("") || repassword.equals("")) {
            Toast.makeText(getBaseContext(), "패스워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (name.equals("") || name.length() > 10) {
            Toast.makeText(getBaseContext(), "이름을 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (phone.equals("") || !phone.matches("^[0-9]{11}+$")) {
            Toast.makeText(getBaseContext(), "전화번호를 올바르게 입력해주세요. - 없이 번호만 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (birth.equals("")) {
            Toast.makeText(getBaseContext(), "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        //비밀번호 일치 체크
        if (!password.equals(repassword)) {
            Toast.makeText(getBaseContext(), "비밀번호와 비밀번호확인이 일치하지 않습니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            return false;
        }
        //이메일 유효성 체크
        if (!id.matches("^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$")) {
            Toast.makeText(getBaseContext(), "이메일 형식이 맞지 않습니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        //비밀번호 체크
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{8,}$")) {
            Toast.makeText(getBaseContext(), "비밀번호는 8자리이상 영문 숫자 조합입니다. 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isDuplicate) {
            Toast.makeText(getBaseContext(), "이메일 중복 체크를 하지 않으셨습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isCheckEmail) {
            Toast.makeText(getBaseContext(), "이메일 인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    //뒤로가기 버튼 클릭
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            this.backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 가기 키를 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

}




