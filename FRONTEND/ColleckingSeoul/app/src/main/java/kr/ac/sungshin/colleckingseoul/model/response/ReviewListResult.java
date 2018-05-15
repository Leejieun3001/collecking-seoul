package kr.ac.sungshin.colleckingseoul.model.response;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.Review.ReviewListItem;

/**
 * Created by LG on 2018-05-16.
 */

public class ReviewListResult {
    private String message;
    private ArrayList<ReviewListItem> boards;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBoards(ArrayList<ReviewListItem> boards) {
        this.boards = boards;
    }

    public String getMessage() {

        return message;
    }

    public ArrayList<ReviewListItem> getBoards() {
        return boards;
    }

    public ReviewListResult(String message, ArrayList<ReviewListItem> boards) {

        this.message = message;
        this.boards = boards;
    }
}
