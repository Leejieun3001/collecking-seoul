package kr.ac.sungshin.colleckingseoul.Review;

/**
 * Created by LG on 2018-05-16.
 */

public class BoardItem {
    private int idx;
    private int bidx;
    private int uidx;
    private String title;
    private String content;
    private String date;
    private String nickname;
    private String url;

    public BoardItem(int idx, int bidx, int uidx, String title, String content, String date, String nickname, String url) {
        this.idx = idx;
        this.bidx = bidx;
        this.uidx = uidx;
        this.title = title;
        this.content = content;
        this.date = date;
        this.nickname = nickname;
        this.url = url;
    }

    public void setBidx(int bidx) {
        this.bidx = bidx;
    }

    public int getBidx() {

        return bidx;
    }

    public BoardItem(int bidx) {

        this.bidx = bidx;
    }


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

    public void setUidx(int uidx) {
        this.uidx = uidx;
    }

    public int getUidx() {

        return uidx;
    }
}
