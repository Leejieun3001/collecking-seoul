package kr.ac.sungshin.colleckingseoul.model.response;

/**
 * Created by LG on 2018-04-07.
 */

public class User {
    private String idx;
    private String id;
    private String nikname;
    private String phone;
    private String birth;

    public User(String idx, String id, String nikname, String phone, String birth) {
        this.idx = idx;
        this.id = id;
        this.nikname = nikname;
        this.phone = phone;
        this.birth = birth;
    }

    public String getIdx() {
        return idx;
    }

    public String getId() {
        return id;
    }

    public String getNikname() {
        return nikname;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirth() {
        return birth;
    }
}
