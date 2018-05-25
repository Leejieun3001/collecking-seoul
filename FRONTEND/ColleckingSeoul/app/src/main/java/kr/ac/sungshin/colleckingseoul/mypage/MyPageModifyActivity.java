package kr.ac.sungshin.colleckingseoul.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
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
import kr.ac.sungshin.colleckingseoul.home.HomeActivity;
import kr.ac.sungshin.colleckingseoul.model.request.Join;
import kr.ac.sungshin.colleckingseoul.model.request.MyInfo;
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
import kr.ac.sungshin.colleckingseoul.model.response.User;
import kr.ac.sungshin.colleckingseoul.model.singleton.InfoManager;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class MyPageModifyActivity extends AppCompatActivity {
    @BindView(R.id.mypage_button_cancel)
    Button buttonCancel;
    @BindView(R.id.mypage_button_store)
    Button buttonStore;
    @BindView(R.id.mypage_edittext_nickname)
    EditText editTextNickname;
    @BindView(R.id.mypage_edittext_phone)
    EditText editTextPhone;
    @BindView(R.id.mypage_radioGroup_sex)
    RadioGroup radioGroupSex;
    @BindView(R.id.mypage_datepicker_birth)
    DatePicker datePickerBirth;
    @BindView(R.id.mypage_radiobutton_man)
    RadioButton radioButtonMan;
    @BindView(R.id.mypage_radiobutton_woman)
    RadioButton radioButtonWoman;

    private NetworkService service;
    private final String TAG = "MyPageModifyActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page_modify);
        ButterKnife.bind(this);
        service = ApplicationController.getInstance().getNetworkService();

        SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        String userNickname = userInfo.getString("nickname", "");
        String userPhone = userInfo.getString("phone", "");
        String userPhoto = userInfo.getString("url", "");
        int userSex = userInfo.getInt("sex", 0);
        editTextNickname.setText(userNickname);
        editTextPhone.setText(userPhone);
        if (userSex == 1) {
            radioGroupSex.check(R.id.mypage_radiobutton_woman);
        } else {
            radioGroupSex.check(R.id.mypage_radiobutton_man);
        }
        bindClickListener();
    }

    public void bindClickListener() {

        //취소
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //저장
        buttonStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!checkValid(editTextNickname.getText().toString(), editTextPhone.getText().toString(), Integer.toString(datePickerBirth.getYear()) + Integer.toString(datePickerBirth.getMonth()) + Integer.toString(datePickerBirth.getDayOfMonth())))
                    return;

                int typeId = radioGroupSex.getCheckedRadioButtonId();

                RadioButton radionbuttonSex = (RadioButton) findViewById(typeId);
                String type = radionbuttonSex.getText().toString();
                int intType = 1;
                if (type.equals("남자")) intType = 0;
                else if (type.equals("여자")) intType = 1;


                final MyInfo myinfo = new MyInfo(editTextNickname.getText().toString(), editTextPhone.getText().toString(), intType,
                        Integer.toString(datePickerBirth.getYear()) + Integer.toString(datePickerBirth.getMonth()) + Integer.toString(datePickerBirth.getDayOfMonth()));

                Log.d("MyPage", editTextNickname.getText().toString() + editTextPhone.getText().toString() +
                        Integer.toString(datePickerBirth.getYear()) + Integer.toString(datePickerBirth.getMonth()) + Integer.toString(datePickerBirth.getDayOfMonth()));
                Call<BaseResult> getUpdateInfo = service.getUpdateUserInfoResult(myinfo);


                getUpdateInfo.enqueue(new Callback<BaseResult>() {
                    @Override
                    public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                        Log.d("MyPage", "서버에 요청");
                        if (response.isSuccessful()) {
                            String message = response.body().getMessage();
                            Log.d("MyPage", message);
                            switch (message) {
                                case "EMPTY_VALUE":
                                    Toast.makeText(getBaseContext(), "입력하신 값이 없습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "NULL_VALUE":
                                    Toast.makeText(getBaseContext(), "받아야할 값이 없습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "NOT_MATCH_REGULATION":
                                    Toast.makeText(getBaseContext(), "정규식이 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "FAILURE":
                                    Toast.makeText(getBaseContext(), "서버 에러입니다. 빠른 시일내에 개선하겠습니다.", Toast.LENGTH_SHORT).show();
                                    break;
                                case "SUCCESS":
                                    Toast.makeText(getBaseContext(), "정보수정 완료.", Toast.LENGTH_SHORT).show();
                                    changeInfo(myinfo);
                                    goHome();
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

    public boolean checkValid(String name, String phone, String birth) {

        if (name.equals("") || name.length() > 10) {
            Toast.makeText(getBaseContext(), "닉네임을 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show();
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

        return true;
    }

    public void goHome() {
        Intent intent = new Intent(getBaseContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void changeInfo(MyInfo myinfo) {
        final SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        final SharedPreferences.Editor editor = userInfo.edit();

        editor.putString("nickname", myinfo.getNickname());
        editor.putString("phone", myinfo.getPhone());
        editor.putString("birth", myinfo.getBirth());
        editor.putInt("sex", myinfo.getSex());
        editor.apply();
        editor.commit();
    }


}

