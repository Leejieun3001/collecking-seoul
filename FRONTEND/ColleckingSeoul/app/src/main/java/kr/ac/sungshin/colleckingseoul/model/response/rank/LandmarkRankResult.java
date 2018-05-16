package kr.ac.sungshin.colleckingseoul.model.response.rank;

import java.util.ArrayList;

/**
 * Created by kwonhyeon-a on 2018. 5. 16..
 */

public class LandmarkRankResult {
    private String message;
    private ArrayList<LandmarkRank> landmarkRankList;

    public LandmarkRankResult(String message, ArrayList<LandmarkRank> landmarkRankList) {
        this.message = message;
        this.landmarkRankList = landmarkRankList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<LandmarkRank> getLandmarkRankList() {
        return landmarkRankList;
    }

    public void setLandmarkRankList(ArrayList<LandmarkRank> landmarkRankList) {
        this.landmarkRankList = landmarkRankList;
    }
}
