package kr.ac.sungshin.colleckingseoul.model.response.rank;

import java.util.ArrayList;

/**
 * Created by kwonhyeon-a on 2018. 5. 16..
 */

public class UserRankResult {
    private String message;
    private ArrayList<UserRank> userRankList;

    public UserRankResult(String message, ArrayList<UserRank> userRankList) {
        this.message = message;
        this.userRankList = userRankList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<UserRank> getUserRankList() {
        return userRankList;
    }

    public void setUserRankList(ArrayList<UserRank> userRankList) {
        this.userRankList = userRankList;
    }
}
