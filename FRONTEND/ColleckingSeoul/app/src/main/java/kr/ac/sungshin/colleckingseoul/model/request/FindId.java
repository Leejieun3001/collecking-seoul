package kr.ac.sungshin.colleckingseoul.model.request;

/**
 * Created by LG on 2018-04-18.
 */

public class FindId {
    String birth;
    String phone;

    public FindId(String birth, String phone) {
        this.birth = birth;
        this.phone = phone;
    }

    public String getBirth() {

        return birth;
    }

    public String getPhone() {
        return phone;
    }
}
