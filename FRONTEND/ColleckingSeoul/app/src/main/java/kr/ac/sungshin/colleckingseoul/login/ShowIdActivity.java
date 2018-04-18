package kr.ac.sungshin.colleckingseoul.login;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;

public class ShowIdActivity extends AppCompatActivity {

    @BindView(R.id.showid_textview_message)
    TextView TextViewMessage;
    @BindView(R.id.showid_button_login)
    Button buttonLogin;
    @BindView(R.id.showid_button_findpw)
    TextView buttonFindPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_id);
        ButterKnife.bind(this);

        Intent reciveIntent = getIntent();
        String email = reciveIntent.getStringExtra("info");
        if (email.equals("SUCCESS")) { TextViewMessage.setText("회원님의 이메일로 임시 비밀번호를 전송했습니다. 이메일을 확인하시고 다시 로그인해주시기 바랍니다."); }
        else { TextViewMessage.setText("회원님의 이메일은 \n " + email + "\n 입니다. 전체 이메일이 기억나지 않으신다면 고객센터로 문의해주시기 바랍니다."); }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);

                //activity stack 비우고 새로 시작하기
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    //안드로이드 버전이 진저브레드가 아니면,
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
            }
        });

        buttonFindPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), FindInfoActivity.class);

                //activity stack 비우고 새로 시작하기
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                    //안드로이드 버전이 진저브레드가 아니면,
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                } else {
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                }
                startActivity(intent);
            }
        });
    }
}
