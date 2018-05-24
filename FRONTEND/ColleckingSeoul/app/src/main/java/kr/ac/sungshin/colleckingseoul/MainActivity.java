package kr.ac.sungshin.colleckingseoul;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class MainActivity extends AppCompatActivity {

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

        Log.d("MainActivity", new Date().toString());
        Log.d("MainActivity", new Date(new Date().toString()).getMonth() + "");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -6);
        Date date1 = simpleDateFormat.format(calendar.getTime());
        Date date2 = new Date("2018-05-17");

        Log.d("MainActivity", date1 < date2 ? "yes":"no");

    }



}
