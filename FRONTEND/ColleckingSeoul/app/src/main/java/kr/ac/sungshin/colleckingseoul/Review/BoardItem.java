package kr.ac.sungshin.colleckingseoul.Review;

/**
 * Created by LG on 2018-05-16.
 */

public class ReviewListItem {
    private int idx;
    private String title;
    private String content;
    private String date;
    private String nickname;
    private String url;

    public int getIdx() {
        return idx;
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

    public String getUrl() {
        return url;
    }

    public void setIdx(int idx) {
        this.idx = idx;
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

    public void setUrl(String photo) {
        this.url = photo;
    }

    public ReviewListItem(int idx, String title, String content, String date, String nickname, String url) {
        this.idx = idx;
        this.title = title;
        this.content = content;
        this.date = date;
        this.nickname = nickname;
        this.url = url;
    }
}
