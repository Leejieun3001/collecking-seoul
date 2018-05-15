package kr.ac.sungshin.colleckingseoul.model.response;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.Review.BoardItem;

/**
 * Created by LG on 2018-05-16.
 */

public class BoardResult {
    private String message;
    private int isMine;
    private ArrayList<BoardItem> board;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIsMine(int isMine) {
        this.isMine = isMine;
    }

    public void setBoard(ArrayList<BoardItem> board) {
        this.board = board;
    }

    public String getMessage() {

        return message;
    }

    public int getIsMine() {
        return isMine;
    }

    public ArrayList<BoardItem> getBoard() {
        return board;
    }

    public BoardResult(String message, int isMine, ArrayList<BoardItem> board) {

        this.message = message;
        this.isMine = isMine;
        this.board = board;
    }
}
