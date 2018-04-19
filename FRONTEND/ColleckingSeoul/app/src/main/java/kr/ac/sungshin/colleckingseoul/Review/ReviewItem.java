package kr.ac.sungshin.colleckingseoul.Review;

/**
 * Created by gwonhyeon-a on 2018. 4. 19..
 */

public class ReviewItem {
    private String url;
    private String title;
    private String content;
    private int idx;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }
}
