package kr.ac.sungshin.colleckingseoul.model.response;

/**
 * Created by LG on 2018-04-07.
 */

public class User {
    private String idx;
    private String id;
    private String nickname;
    private String phone;
    private String birth;
    private String url;
    private int sex;


    public User(String idx, String id, String nickname, String phone, String birth) {
        this.idx = idx;
        this.id = id;
        this.nickname = nickname;
        this.phone = phone;
        this.birth = birth;
    }

    public User(String idx, String id, String nickname, String phone, String birth, String url, int sex) {
        this.idx = idx;
        this.id = id;
        this.nickname = nickname;
        this.phone = phone;
        this.birth = birth;
        this.url = url;
        this.sex = sex;
    }

    public String getIdx() {
        return idx;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirth() {
        return birth;
    }

    public String getUrl() {
        return url;
    }

    public int getSex() {
        return sex;
    }
}
