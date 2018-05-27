package kr.ac.sungshin.colleckingseoul.model.response;

import java.util.ArrayList;

import kr.ac.sungshin.colleckingseoul.Review.BoardItem;

/**
 * Created by LG on 2018-05-16.
 */

public class BoardResult {
    private String message;
    private String isMine;
    private BoardItem board;
    private float grade;

    public BoardResult(String message, String isMine, BoardItem board) {
        this.message = message;
        this.isMine = isMine;
        this.board = board;
    }

    public BoardResult(String message, String isMine, BoardItem board, float grade) {
        this.message = message;
        this.isMine = isMine;
        this.board = board;
        this.grade = grade;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setIsMine(String isMine) {
        this.isMine = isMine;
    }

    public void setBoard(BoardItem board) {
        this.board = board;
    }

    public BoardItem getBoard() {

        return board;
    }

    public String getMessage() {

        return message;
    }

    public String getIsMine() {
        return isMine;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }
}
