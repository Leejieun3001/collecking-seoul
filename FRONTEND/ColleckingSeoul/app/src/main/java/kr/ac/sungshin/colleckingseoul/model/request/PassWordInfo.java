package kr.ac.sungshin.colleckingseoul.model.request;

/**
 * Created by LG on 2018-05-24.
 */

public class PassWordInfo {
    String currentPW;
    String newPW1;
    String newPW2;

    public PassWordInfo(String currentPW, String newPW1, String newPW2) {
        this.currentPW = currentPW;
        this.newPW1 = newPW1;
        this.newPW2 = newPW2;
    }

    public String getCurrentPW() {
        return currentPW;
    }

    public String getNewPW1() {
        return newPW1;
    }

    public String getNewPW2() {
        return newPW2;
    }

    public void setCurrentPW(String currentPW) {
        this.currentPW = currentPW;
    }

    public void setNewPW1(String newPW1) {
        this.newPW1 = newPW1;
    }

    public void setNewPW2(String newPW2) {
        this.newPW2 = newPW2;
    }

}
