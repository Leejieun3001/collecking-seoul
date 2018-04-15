package kr.ac.sungshin.colleckingseoul.model.request;

import java.io.File;

/**
 * Created by LG on 2018-04-05.
 */

public class Join {
    String id;
    String password1;
    String password2;
    String phone;
    String name;
    String birth;
    int sex;


    public Join(String id, String password1, String password2, String phone, String name, String birth, int sex) {
        this.id = id;
        this.password1 = password1;
        this.password2 = password2;
        this.phone = phone;
        this.name = name;
        this.birth = birth;
        this.sex = sex;
    }


    public String getId() {
        return id;
    }

    public String getPassword1() {
        return password1;
    }

    public String getPassword2() {
        return password2;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public String getBirth() {
        return birth;
    }

    public int getSex() {
        return sex;
    }
}
