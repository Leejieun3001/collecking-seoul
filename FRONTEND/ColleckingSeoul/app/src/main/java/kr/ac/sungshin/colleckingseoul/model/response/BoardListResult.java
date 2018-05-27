package kr.ac.sungshin.colleckingseoul.model.response;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.Review.BoardItem;

/**
 * Created by LG on 2018-05-16.
 */

public class BoardListResult {
    private String message;
    private int hasDone;
    private ArrayList<BoardItem> boards;

    public BoardListResult(String message, ArrayList<BoardItem> boards) {

        this.message = message;
        this.boards = boards;
    }

    public BoardListResult(String message, int hasDone, ArrayList<BoardItem> boards) {
        this.message = message;
        this.hasDone = hasDone;
        this.boards = boards;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setBoards(ArrayList<BoardItem> boards) {
        this.boards = boards;
    }

    public String getMessage() {

        return message;
    }

    public ArrayList<BoardItem> getBoards() {
        return boards;
    }

    public int getHasDone() {
        return hasDone;
    }

    public void setHasDone(int hasDone) {
        this.hasDone = hasDone;
    }
}
