package kr.ac.sungshin.colleckingseoul.model.request;

/**
 * Created by LG on 2018-04-18.
 */

public class FindPassWord {
    String id;
    String phone;

    public FindPassWord(String id, String phone) {
        this.id = id;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }
}
