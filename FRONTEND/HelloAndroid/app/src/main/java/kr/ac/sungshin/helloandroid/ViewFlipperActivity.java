package kr.ac.sungshin.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

public class ViewFlipperActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_flipper);

        Button btnPrev, btnNext;
        final ViewFlipper vFlipper;

        btnPrev = (Button) findViewById(R.id.btn11);
        btnNext = (Button) findViewById(R.id.btn22);

        vFlipper = (ViewFlipper) findViewById(R.id.flipper);

        btnPrev.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vFlipper.showPrevious();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                vFlipper.showNext();
            }
        });
    }
}
