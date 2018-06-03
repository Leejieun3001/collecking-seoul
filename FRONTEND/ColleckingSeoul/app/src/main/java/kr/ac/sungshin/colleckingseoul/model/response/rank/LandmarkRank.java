package kr.ac.sungshin.colleckingseoul.model.response.rank;

/**
 * Created by kwonhyeon-a on 2018. 5. 16..
 */

public class LandmarkRank {
    private String idx;
    private String name;

    public LandmarkRank(String name) {
        this.name = name;
    }

    public LandmarkRank(String idx, String name) {
        this.idx = idx;
        this.name = name;
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
