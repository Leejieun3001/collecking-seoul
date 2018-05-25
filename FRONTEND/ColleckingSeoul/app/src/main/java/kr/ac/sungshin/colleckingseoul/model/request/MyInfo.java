package kr.ac.sungshin.colleckingseoul.model.request;

/**
 * Created by LG on 2018-05-24.
 */

public class MyInfo {
    String nickname;
    String phone;
    int sex;
    String birth;

    public MyInfo(String nickname, String phone, int sex, String birth) {
        this.nickname = nickname;
        this.phone = phone;
        this.sex = sex;
        this.birth = birth;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public int getSex() {
        return sex;
    }

    public String getBirth() {
        return birth;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}
