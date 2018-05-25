package kr.ac.sungshin.colleckingseoul.model.response;
import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;


/**
 * Created by LG on 2018-05-23.
 */

public class MyLandmarkResult {
    String message;
    ArrayList<Landmark> landmarks;

    public MyLandmarkResult(String message, ArrayList<Landmark> landmarks) {
        this.message = message;
        this.landmarks = landmarks;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<Landmark> getLandmarks() {
        return landmarks;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setLandmarks(ArrayList<Landmark> landmarks) {
        this.landmarks = landmarks;
    }
}
