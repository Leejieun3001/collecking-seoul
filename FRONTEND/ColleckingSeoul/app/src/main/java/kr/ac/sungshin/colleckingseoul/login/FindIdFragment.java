package kr.ac.sungshin.colleckingseoul.login;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.model.request.FindId;
import kr.ac.sungshin.colleckingseoul.model.response.FindInfoResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FindIdFragment extends Fragment {

    @BindView(R.id.findid_editText_birth)
    EditText editTextBirth;
    @BindView(R.id.findid_editText_phone)
    EditText editTextPhone;
    @BindView(R.id.findid_button_find_id)
    Button buttinFindId;

    private NetworkService service;
    private final String TAG = "FindIdFragment";

    public FindIdFragment() {
        // Required empty public constructor
        super();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_find_id, container, false);
        service = ApplicationController.getInstance().getNetworkService();
        ButterKnife.bind(this, view);
        ButterKnife.bind(this, view);

        RelativeLayout Layout = (RelativeLayout) inflater.inflate(R.layout.fragment_find_id, container, false);


        buttinFindId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = editTextPhone.getText().toString();
                String birth = editTextBirth.getText().toString();
                if (!checkValid(birth, phone)) return;
                FindId info = new FindId(birth, phone);
                Call<FindInfoResult> findId = service.getFindIdResult(info);
                findId.enqueue(new Callback<FindInfoResult>() {
                    @Override
                    public void onResponse(Call<FindInfoResult> call, Response<FindInfoResult> response) {
                        if (response.isSuccessful()) {
                            Log.d(TAG, response.body().toString());
                            String message = response.body().getMessage();
                            if (message.equals("EXIST_MEMBER")) {
                                Intent intent = new Intent(getContext(), ShowIdActivity.class);
                                intent.putExtra("info", response.body().getId());

                                //activity stack 비우고 새로 시작하기
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                                    //안드로이드 버전이 진저브레드가 아니면,
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                } else {
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                }

                                startActivity(intent);
                            } else if (message.equals("NOT_SIGN_UP")) {
                                Toast.makeText(getContext(), "입력하신 회원정보는 존재하지 않습니다. 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                            } else if (message.equals("FAILURE")) {
                                Toast.makeText(getContext(), "회원정보를 찾을 수 없습니다 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }

                    @Override
                    public void onFailure(Call<FindInfoResult> call, Throwable t) {

                    }
                });


            }
        });
        return view;
    }

    public boolean checkValid(String birth, String phone) {
        if (birth.equals("")) {
            Toast.makeText(getContext(), "생년월일을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.contains("-")) {
            Toast.makeText(getContext(), "핸드폰 번호는 - 없이 숫자만 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (phone.equals("")) {
            Toast.makeText(getContext(), "핸드폰 번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}


