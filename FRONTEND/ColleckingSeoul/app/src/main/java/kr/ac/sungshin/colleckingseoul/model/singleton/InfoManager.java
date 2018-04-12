package kr.ac.sungshin.colleckingseoul.model.singleton;

import kr.ac.sungshin.colleckingseoul.model.response.User;

/**
 * Created by gwonhyeon-a on 2018. 4. 6..
 */

public class InfoManager {
    private static InfoManager instance = null;

    private InfoManager() {

    }

    public static synchronized InfoManager getInstance() {
        if (instance == null) {
            instance = new InfoManager();
        }
        return instance;
    }

    private User userObject;

    public User getUserInfo() {
        return userObject;
    }

    public void setUserInfo(User userObject) {
        this.userObject = userObject;
    }
}
