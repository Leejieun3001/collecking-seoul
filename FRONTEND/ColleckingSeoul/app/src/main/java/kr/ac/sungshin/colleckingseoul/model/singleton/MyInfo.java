package kr.ac.sungshin.colleckingseoul.model.singleton;

/**
 * Created by kwonhyeon-a on 2018. 4. 6..
 */

public class MyInfo {
    private String idx;
    private String id;
    private String nickname;
    private String phone;
    private String birth;
    private String photo;
    private int sex;

    public MyInfo(String idx, String id, String nickname, String phone, String birth, String photo, int sex) {
        this.idx = idx;
        this.id = id;
        this.nickname = nickname;
        this.phone = phone;
        this.birth = birth;
        this.photo = photo;
        this.sex = sex;
    }

    public MyInfo() {
    }

    public String getIdx() {
        return idx;
    }

    public void setIdx(String idx) {
        this.idx = idx;
    }

    public String getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }
}
