package kr.ac.sungshin.colleckingseoul.model.request;

/**
 * Created by LG on 2018-04-16.
 */

public class BasicLogin {
    private String id = "";
    private String password = "";

    public BasicLogin(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
}
