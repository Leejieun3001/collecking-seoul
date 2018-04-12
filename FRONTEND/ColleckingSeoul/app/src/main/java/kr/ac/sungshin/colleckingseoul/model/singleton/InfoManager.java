package kr.ac.sungshin.colleckingseoul.model.singleton;

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

    private MyInfo userObject;

    public MyInfo getUserInfo() {
        return userObject;
    }

    public void setUserInfo(MyInfo userObject) {
        this.userObject = userObject;
    }
}
