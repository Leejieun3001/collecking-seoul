package kr.ac.sungshin.colleckingseoul.sqLite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsertDataActivity extends AppCompatActivity {
    //안드로이드 SQLite3
    DBHelper helper;
    String dbName = "colleckingDB";
    // 데이터 베이스 버전
    int dbVersion = 1;
    private SQLiteDatabase db;

    private NetworkService service;
    String TAG = "InsertLandmark.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        service = ApplicationController.getInstance().getNetworkService();

        LandmarkDataArry data = new LandmarkDataArry();
        String[] TraditionalCulture = data.getLandmarkTraditionName();
        String[] HistoricSite = data.getLandmarkHistoricName();
        String[] Museum = data.getLandmarkMuseumName();
        String[] ArtMuseum = data.getLandmarkArtName();
        String[] Landmark = data.getLandmarkLandmarkName();
        //안드로이드 SQLite3
        helper = new DBHelper(this, dbName, null, dbVersion);
        try {
            // 읽고  쓸수 있는 DB
            db = helper.getWritableDatabase();
            Log.d(TAG, "데이터 베이스 읽어오기 성공");
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, "데이터 베이스 읽어 올수 없음");
        }

//        /* 입력 완료
        try {
            insertDatabase(TraditionalCulture);
            insertDatabase(HistoricSite);
            insertDatabase(Museum);
            insertDatabase(ArtMuseum);
            insertDatabase(Landmark);
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        select();
    }


    void insertDatabase(String[] Data) {

        final Geocoder mGeoCoder = new Geocoder(this);
        List<Address> mResultLocation = null;

        for (int i = 1; i < Data.length; i++) {
            try {
                Log.d("위도  경도 : ", Data[i]);
                mResultLocation = mGeoCoder.getFromLocationName(Data[i], 1);
                if (mResultLocation != null) {
                    if (mResultLocation.size() == 0) {
                        Log.d("위도  경도 : ", "해당 값이 없습니다 ");
                    } else {
                        double mLat = mResultLocation.get(0).getLatitude();
                        double mLng = mResultLocation.get(0).getLongitude();
                        insert(Data[i].toString(), mLat, mLng, Data[0]);
                        Log.d("위도  경도 : ", "변환 값 " + mLat + mLng);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("위도  경도 : ", "변환 실패 ");
            }
        }
    }

    void select() {
        Cursor c = db.rawQuery("select * from Landmark;", null);
        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            double lang = c.getDouble(2);
            double let = c.getDouble(3);
            String category = c.getString(4);
            Log.d(TAG, "id: " + id + ", name: " + name + " ,lang : " + lang + ", let : " + let + ", category: " + category);
        }
    }

    void insert(String name, double lat, double lng, String category) {
        String sql = "INSERT INTO Landmark (name, lat, lng, category) VALUES('" + name + "'," + lat + "," + lng + ",'" + category + "')";
        Log.d("쿼리문", sql);
        Call<BaseResult> insertResult = service.getInsertResult(sql);
        insertResult.enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                if (response.isSuccessful()) {

                    Log.d(TAG, response.body().toString());
                    if (response.body().getMessage().equals("SUCCESS")) {
                        //Toast.makeText(getApplicationContext(), "insert 성공.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<BaseResult> call, Throwable t) {

            }
        });
        db.execSQL(sql);
        Log.d(TAG, "insert 완료");
    }

}

