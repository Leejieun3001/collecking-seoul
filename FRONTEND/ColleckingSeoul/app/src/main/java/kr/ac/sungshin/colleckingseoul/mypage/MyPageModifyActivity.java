package kr.ac.sungshin.colleckingseoul.mypage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.home.HomeActivity;
import kr.ac.sungshin.colleckingseoul.model.request.Join;
import kr.ac.sungshin.colleckingseoul.model.request.MyInfo;
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
import kr.ac.sungshin.colleckingseoul.model.response.User;
import kr.ac.sungshin.colleckingseoul.model.singleton.InfoManager;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class MyPageModifyActivity extends AppCompatActivity {
    @BindView(R.id.mypage_button_cancel)
    Button buttonCancel;
    @BindView(R.id.mypage_button_store)
    Button buttonStore;
    @BindView(R.id.mypage_button_profile)
    Button buttonPofile;
    @BindView(R.id.mypage_image_profile)
    CircleImageView imageViewProfile;
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
    private static final int REQ_CODE_SELECT_IMAGE = 100;
    private final String TAG = "MyPageModifyActivity";

    String imgUrl = "";
    Uri data;

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
        String[] birth = userInfo.getString("birth", "").split("-");


        datePickerBirth.updateDate(Integer.parseInt(birth[0]), Integer.parseInt(birth[1]) - 1, Integer.parseInt(birth[2].substring(0, 2)));
        int userSex = userInfo.getInt("sex", 0);
        editTextNickname.setText(userNickname);
        editTextPhone.setText(userPhone);
        if (userSex == 1) {
            radioGroupSex.check(R.id.mypage_radiobutton_woman);
        } else {
            radioGroupSex.check(R.id.mypage_radiobutton_man);
        }

        if (!userPhoto.equals("")) {
            Glide.with(getApplicationContext())
                    .load(userPhoto)
                    .into(imageViewProfile);
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
        //프로필 사진
        buttonPofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
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
                String birthYear = Integer.toString(datePickerBirth.getYear());
                String birthMonth;
                String birthDate;

                if (datePickerBirth.getMonth() < 9)
                    birthMonth = "0" + Integer.toString(datePickerBirth.getMonth() + 1);
                else if (datePickerBirth.getMonth() == 9) birthMonth = "10";
                else birthMonth = Integer.toString(datePickerBirth.getMonth() + 1);

                if (datePickerBirth.getDayOfMonth() < 10)
                    birthDate = "0" + Integer.toString(datePickerBirth.getDayOfMonth());
                else birthDate = Integer.toString(datePickerBirth.getDayOfMonth());


                final MyInfo myinfo = new MyInfo(editTextNickname.getText().toString(), editTextPhone.getText().toString(), intType,
                        birthYear + "-" + birthMonth + "-" + birthDate);

                Call<BaseResult> getUpdateInfo = service.getUpdateUserInfoResult(myinfo);
                getUpdateInfo.enqueue(new Callback<BaseResult>() {
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

    public void changePhoto() {
        final SharedPreferences userInfo = getSharedPreferences("user", MODE_PRIVATE);
        final SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("url", imgUrl);
        editor.apply();
        editor.commit();
    }

    // 선택된 이미지 데이터 받아오기
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            //이미지를 성공적으로 가져왔을 경우
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    imageViewProfile.setImageBitmap(image_bitmap);
                    getImageNameToUri(data.getData());
                    this.data = data.getData();
                    StoreProfile();
                    changePhoto();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getImageNameToUri(Uri data) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(data, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String imgPath = cursor.getString(column_index);
        String imgName = imgPath.substring(imgPath.lastIndexOf("/") + 1);
        imgUrl = imgPath;

        return imgName;
    }

    public void StoreProfile() {
        MultipartBody.Part body;

        if (imgUrl == "") {
            Bitmap Img = BitmapFactory.decodeResource(getResources(), R.drawable.avatar_male);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Img.compress(Bitmap.CompressFormat.JPEG, 20, baos);
            //imgUrl = "temp";
            // File photo = new File(imgUrl);
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
            body = MultipartBody.Part.createFormData("photo", Img.toString(), photoBody);
        } else {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            InputStream in = null;
            try {
                in = getContentResolver().openInputStream(data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bitmap = BitmapFactory.decodeStream(in, null, options);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);

            File photo = new File(imgUrl);
            RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
            body = MultipartBody.Part.createFormData("photo", photo.getName(), photoBody);
        }

        Call<BaseResult> getUpdatePhotoResult = service.getUpdatePhotoResult(body);
        getUpdatePhotoResult.enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                if (response.isSuccessful()) {

                    Log.d(TAG, response.body().getMessage());
                    switch (response.body().getMessage()) {
                        case "SUCCESS":
                            Toast.makeText(getBaseContext(), "프로필 사진을 성공적으로 변경 하였습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case "NO_IMAGE":
                            Toast.makeText(getBaseContext(), "프로필 사진을 없습니다.", Toast.LENGTH_SHORT).show();
                            break;
                        case "NOT_LOGIN":
                            Toast.makeText(getBaseContext(), "로그인 하지 않은 사용자 입니다.", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {

            }
        });

    }

}

