package kr.ac.sungshin.colleckingseoul.Review;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.login.LoginActivity;
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
import kr.ac.sungshin.colleckingseoul.model.response.DefaultResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterReviewActivity extends AppCompatActivity {
    @BindView(R.id.registerreview_edittext_title)
    EditText titleEditText;
    @BindView(R.id.registerreview_edittext_content)
    EditText contentEditText;
    @BindView(R.id.registerreview_imageview_photo)
    ImageView photoImageView;
    @BindView(R.id.registerreview_button_findphoto)
    Button photoButton;
    @BindView(R.id.registerreview_button_write)
    Button writeButton;

    private NetworkService service;
    private final String TAG = "RegisterReviewActivity";
    private static final int GALLERY_CODE = 1112;
    private String idx = "";

    String imgUrl = "";
    Uri imgUri;
    private Uri data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_review);
        ButterKnife.bind(this);

        service = ApplicationController.getInstance().getNetworkService();
        Intent gettingIntent = getIntent();
        idx = gettingIntent.getStringExtra("idx");

        bindClickListeners();
    }

    private void bindClickListeners () {
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkValid()) return;

                RequestBody title = RequestBody.create(MediaType.parse("multipart/form-data"), titleEditText.getText().toString());
                RequestBody content = RequestBody.create(MediaType.parse("multipart/form-data"), titleEditText.getText().toString());
                RequestBody landmark_idx = RequestBody.create(MediaType.parse("multipart/form-data"), idx);

                MultipartBody.Part body;
                if (imgUrl.equals("")) {
                    body = null;
                } else {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    InputStream in = null;
                    try {
                        in = getContentResolver().openInputStream(imgUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(in, null, options); // InputStream 으로부터 Bitmap 을 만들어 준다.
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos); // 압축 옵션( JPEG, PNG ) , 품질 설정 ( 0 - 100까지의 int형 ),

                    File photo = new File(imgUrl); // 그저 블러온 파일의 이름을 알아내려고 사용.
                    RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
                    body = MultipartBody.Part.createFormData("photo", photo.getName(), photoBody);
                }

                Call<DefaultResult> getWritingBoardResult = service.getWritingBoardResult(title, content, landmark_idx, body);
                getWritingBoardResult.enqueue(new Callback<DefaultResult>() {
                    @Override
                    public void onResponse(Call<DefaultResult> call, Response<DefaultResult> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getMessage().equals("SUCCESS")) {
                                Toast.makeText(getApplicationContext(), "후기 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                setResult(RESULT_OK, intent);
                                finish();
                            } else {
                                Toast.makeText(getBaseContext(), "후기 등록에 에러가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultResult> call, Throwable t) {
                        Log.d(TAG, "onFailure");
                    }
                });
            }
        });
    }

    // 선택된 이미지 데이터 받아오기
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_CODE) {
            //이미지를 성공적으로 가져왔을 경우
            if (resultCode == Activity.RESULT_OK) {
                try {
                    this.data = data.getData();
                    //이미지 데이터를 비트맵으로 받아온다.
                    Bitmap image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    photoImageView.setImageBitmap(image_bitmap);
                    imgUri = data.getData();
                    imgUrl = this.imgUri.getPath();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean checkValid() {
        if (titleEditText.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "제목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (contentEditText.getText().toString().equals("")) {
            Toast.makeText(getBaseContext(), "내용을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}