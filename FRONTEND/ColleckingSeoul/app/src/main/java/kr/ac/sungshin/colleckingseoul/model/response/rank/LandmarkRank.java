package kr.ac.sungshin.colleckingseoul.model.response.rank;

/**
 * Created by kwonhyeon-a on 2018. 5. 16..
 */

public class LandmarkRank {
    private int idx;
    private String name;

    public LandmarkRank(String name) {
        this.name = name;
    }

    public LandmarkRank(int idx, String name) {
        this.idx = idx;
        this.name = name;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
