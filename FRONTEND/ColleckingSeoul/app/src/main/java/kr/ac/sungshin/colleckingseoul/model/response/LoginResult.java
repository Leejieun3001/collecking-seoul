package kr.ac.sungshin.colleckingseoul.model.response;

/**
 * Created by LG on 2018-04-05.
 */
public class LoginResult {
    private String message;
    private User user;
    private String token;

    public LoginResult(String message, User user, String token) {
        this.message = message;
        this.user = user;
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }

    public String getToken() {
        return token;
    }
}

