package kr.ac.sungshin.colleckingseoul.model.request;

/**
 * Created by kwonhyeon-a on 2018. 5. 24..
 */

public class RefreshToken {
    String idx;

    public RefreshToken(String idx) {
        this.idx = idx;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }
}
