package kr.ac.sungshin.colleckingseoul.model.request;

/**
 * Created by kwonhyeon-a on 2018. 4. 6..
 */

public class Login {
    private String id = "";
    private String password = "";
    private String accessToken = "";
    private String nickname = "";
    private String birth = "";
    private String phone = "00000000000";
    private String photo = "";
    private int snsCategory = 0;

    public Login(String id , String password) {
        this.id = id;
        this.password = password;
    }

    public Login(String id, String accessToken, String nickname, String photo, int snsCategory) {
        this.id = id;
        this.accessToken = accessToken;
        this.nickname = nickname;
        this.photo = photo;
        this.snsCategory = snsCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getSnsCategory() {
        return snsCategory;
    }

    public void setSnsCategory(int snsCategory) {
        this.snsCategory = snsCategory;
    }
}
