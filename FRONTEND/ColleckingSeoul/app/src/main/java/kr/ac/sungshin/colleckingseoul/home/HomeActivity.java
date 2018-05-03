package kr.ac.sungshin.colleckingseoul.home;

import android.content.SharedPreferences;
import android.support.v4.app.AppLaunchChecker;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.OnMapReadyCallback;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;

import kr.ac.sungshin.colleckingseoul.R;
import kr.ac.sungshin.colleckingseoul.Review.ReviewListActivity;

import kr.ac.sungshin.colleckingseoul.model.response.LandmarkListResult;
import kr.ac.sungshin.colleckingseoul.network.ApplicationController;
import kr.ac.sungshin.colleckingseoul.network.NetworkService;
import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;
import retrofit2.Call;
import retrofit2.Callback;

import kr.ac.sungshin.colleckingseoul.model.response.BaseResult;

import retrofit2.Response;


public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    //안드로이드 SQLite3
    DBHelper helper;
    String dbName = "colleckingDB";
    // 데이터 베이스 버전
    int dbVersion = 1;
    private SQLiteDatabase db;

    String TAG = "HomeActivity";
    private NetworkService service;
    private ArrayList<Landmark> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        service = ApplicationController.getInstance().getNetworkService();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.home_fragment_map);
        mapFragment.getMapAsync(this);

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
            finish();
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

//
//        LandmarkDataArry data = new LandmarkDataArry();
//        String[] TraditionalCulture = data.getLandmarkTraditionName();
//        String[] HistoricSite = data.getLandmarkHistoricName();
//        String[] Museum = data.getLandmarkMuseumName();
//        String[] ArtMuseum = data.getLandmarkArtName();
//        String[] Landmark = data.getLandmarkLandmarkName();
        //안드로이드 SQLite3
              helper = new DBHelper(
                this, dbName, null, dbVersion
        );
        try {
            // 읽고  쓸수 있는 DB
            db = helper.getWritableDatabase();
            Log.d(TAG, "데이터 베이스 읽어오기 성공");
        } catch (SQLiteException e) {
            e.printStackTrace();
            Log.e(TAG, "데이터 베이스 읽어 올수 없음");
            finish();
        }


        insertDatabase(TraditionalCulture);
       // insertDatabase(HistoricSite);
       // insertDatabase(Museum);
       // insertDatabase(ArtMuseum);
        // insertDatabase(Landmark);


      //  select();

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
        Log.d("쿼리문",sql);
         Call<BaseResult> insertQuery = service.getInsertResult(sql);
        Log.d("레트로핏","실행");
        insertQuery.enqueue(new Callback<BaseResult>() {
            @Override
            public void onResponse(Call<BaseResult> call, Response<BaseResult> response) {
                Log.d("레트로핏","통신");
                if (response.isSuccessful()) {
                    Log.d("레트로핏","성공");
                    String message = response.body().getMessage();
                    switch (message) {
                        case "SUCCESS":
                            Toast.makeText(getBaseContext(), "insert성공", Toast.LENGTH_SHORT).show();
                            break;
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


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.getTag();
                Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
                intent.putExtra("lat", 37.576183);
                intent.putExtra("lng", 126.976926);
                startActivity(intent);
                return false;
            }
        });

        LatLng latLng = new LatLng(37.576183, 126.976926);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("현아"));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.getTag();
                Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
                intent.putExtra("lat", 37.576183);
                intent.putExtra("lng", 126.976926);
                startActivity(intent);
                return false;
            }
        });

        loadLandmark();
        createMarkers();
    }

//    private void loadLandmark() {
//        SqLiteController helper = new SqLiteController(this);
//        ArrayList<Landmark> landmarks = helper.getAllLandmark(helper.getWritableDatabase());
//        for (final Landmark landmark : landmarks) {
//            LatLng latLng = new LatLng(landmark.getLat(), landmark.getLat());
//            googleMap.addMarker(new MarkerOptions().position(latLng).title(landmark.getName()));
//            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(Marker marker) {
//                    marker.getTag();
//                    Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
//                    intent.putExtra("lat", landmark.getLat());
//                    intent.putExtra("lng", landmark.getLat());
//                    startActivity(intent);
//                    return false;
//                }
//            });
//        }
//
//        // icon 으로 마커 생성하는 방법
////        Marker melbourne = mMap.addMarker(new MarkerOptions().position(MELBOURNE)
////                .icon(BitmapDescriptorFactory
////                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
//    }

    private void loadLandmark () {
        Call<LandmarkListResult> getLandmarkList = service.getLandmarkList();
        getLandmarkList.enqueue(new Callback<LandmarkListResult>() {
            @Override
            public void onResponse(Call<LandmarkListResult> call, Response<LandmarkListResult> response) {
                Log.d(TAG, "onResponse");
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();

                    switch (message) {
                        case "SUCCESS": list.addAll(response.body().getLandmarkList()); break;
                    }
                }
            }

            @Override
            public void onFailure(Call<LandmarkListResult> call, Throwable t) {
                Log.d(TAG, "onFailure");
            }
        });
    }

    private void createMarkers () {
        int length = list.size();
        for (int i = 0; i < length; i++) {
            final Landmark landmark = list.get(i);
            LatLng latLng = new LatLng(landmark.getLat(), landmark.getLng());
            googleMap.addMarker(new MarkerOptions().position(latLng).title(landmark.getName()));
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    marker.getTag();
                    Intent intent = new Intent(getBaseContext(), ReviewListActivity.class);
                    intent.putExtra("lat", landmark.getLat());
                    intent.putExtra("lng", landmark.getLat());
                    startActivity(intent);
                    return false;
                }
            });
        }
    }

}
