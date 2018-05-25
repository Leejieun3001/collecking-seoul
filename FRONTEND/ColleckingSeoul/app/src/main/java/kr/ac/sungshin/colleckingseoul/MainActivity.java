package kr.ac.sungshin.colleckingseoul;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//            try {
//                PackageInfo info = getPackageManager().getPackageInfo("kr.ac.sungshin.colleckingseoul", PackageManager.GET_SIGNATURES);
//                for (Signature signature : info.signatures) {
//                    MessageDigest md = MessageDigest.getInstance("SHA");
//                    md.update(signature.toByteArray());
//                    Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
//                }
//            } catch (PackageManager.NameNotFoundException e) {
//                e.printStackTrace();
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            }

        File file = new File("https://s3.us-east-2.amazonaws.com/collecking-seoul/1524014394484.png");
        Log.d(TAG, file.getAbsolutePath());
        Log.d(TAG, file.getName());
        Log.d(TAG, file.getPath());

    }



}
