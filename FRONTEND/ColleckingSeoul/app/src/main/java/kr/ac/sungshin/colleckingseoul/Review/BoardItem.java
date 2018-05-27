package kr.ac.sungshin.colleckingseoul.Review;

/**
 * Created by LG on 2018-05-16.
 */

public class BoardItem {
    private int idx;
    private int bIdx;
    private int uIdx;
    private String title;
    private String content;
    private String date;
    private String nickname;
    private float grade;
    private String url;

    public BoardItem(int idx, int bIdx, int uIdx, String title, String content, String date, String nickname, float grade, String url) {
        this.idx = idx;
        this.bIdx = bIdx;
        this.uIdx = uIdx;
        this.title = title;
        this.content = content;
        this.date = date;
        this.nickname = nickname;
        this.grade = grade;
        this.url = url;
    }


    public int getIdx() {
        return idx;
    }

    public int getbIdx() {
        return bIdx;
    }

    public int getuIdx() {
        return uIdx;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getNickname() {
        return nickname;
    }

    public float getGrade() {
        return grade;
    }

    public String getUrl() {
        return url;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public void setbIdx(int bIdx) {
        this.bIdx = bIdx;
    }

    public void setuIdx(int uIdx) {
        this.uIdx = uIdx;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
