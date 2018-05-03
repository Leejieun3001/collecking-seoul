package kr.ac.sungshin.colleckingseoul.model.response;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.sqLite.Landmark;

/**
 * Created by gwonhyeon-a on 2018. 5. 3..
 */

public class LandmarkListResult {
    private String message;
    private ArrayList<Landmark> landmarkList;

    public LandmarkListResult() {
    }

    public LandmarkListResult(String message, ArrayList<Landmark> landmarkList) {
        this.message = message;
        this.landmarkList = landmarkList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Landmark> getLandmarkList() {
        return landmarkList;
    }

    public void setLandmarkList(ArrayList<Landmark> landmarkList) {
        this.landmarkList = landmarkList;
    }
}
