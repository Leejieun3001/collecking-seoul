package kr.ac.sungshin.colleckingseoul.sqLite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by kwonhyeon-a on 2018. 4. 19..
 */

public class SqLiteController extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "colleckingDB";
    private static final String DATABASE_DEFAULT_TABLE = "Landmark";
    private static final int DATABASE_VERSION = 1;

    public SqLiteController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<Landmark> getAllLandmark(SQLiteDatabase sqLiteDatabase) {
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * from Landmark", null);
        ArrayList<Landmark> landmarks = new ArrayList<>();

        while (cursor.moveToNext()) {
            Landmark landmark = new Landmark(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3));
            landmarks.add(landmark);
        }

        return landmarks;
    }
}
