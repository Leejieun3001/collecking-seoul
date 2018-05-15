package kr.ac.sungshin.colleckingseoul.Review;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.model.response.BoardResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    @BindView(R.id.review_textview_name)
    TextView textViewName;
    @BindView(R.id.review_textview_title)
    TextView textViewTitle;
    @BindView(R.id.review_textview_nickname)
    TextView textViewNickname;
    @BindView(R.id.review_textview_date)
    TextView textViewdate;
    @BindView(R.id.review_textview_idx)
    TextView textViewIdx;
    @BindView(R.id.review_textview_content)
    TextView textViewContent;
    @BindView(R.id.review_imageview_photo)
    ImageView imageViewPhoto;

    private NetworkService service;
    private final String TAG = "ReviewActivity";
    private int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        service = ApplicationController.getInstance().getNetworkService();
        ButterKnife.bind(this);

        Intent intent = getIntent();
        idx = intent.getIntExtra("idx", 0);

        Call<BoardResult> getBoardResult = service.getBoardResult(idx);
        getBoardResult.enqueue(new Callback<BoardResult>() {
            @Override
            public void onResponse(Call<BoardResult> call, Response<BoardResult> response) {
                if (response.isSuccessful()) {
                    textViewTitle.setText(response.body().getBoard().get(0).getTitle());
                    textViewNickname.setText(response.body().getBoard().get(0).getNickname());
                    textViewdate.setText(response.body().getBoard().get(0).getDate());
                    textViewIdx.setText(idx);
                    textViewContent.setText(response.body().getBoard().get(0).getContent());

                    if (response.body().getBoard().get(0).getUrl() != "") {
                        Glide.with(getApplicationContext())
                                .load(response.body().getBoard().get(0).getUrl())
                                .into(imageViewPhoto);

                    }
                    if (response.body().getMessage().equals("NULL_VALUE")) {
                        Toast.makeText(getApplicationContext(), "값을 입력해 주세요.", Toast.LENGTH_SHORT).show();

                    }
                    if (response.body().getMessage().equals("NOT_LOGIN")) {
                        Toast.makeText(getApplicationContext(), "로그인 하지 않은 사용자입니다.", Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<BoardResult> call, Throwable t) {


            }
        });


    }
}