package kr.ac.sungshin.colleckingseoul.model.response.rank;

/**
 * Created by kwonhyeon-a on 2018. 5. 16..
 */

public class UserRank {
    private String nickname;
    private String url;
    private String id;

    public UserRank(String nickname, String url, String id) {
        this.nickname = nickname;
        this.url = url;
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
