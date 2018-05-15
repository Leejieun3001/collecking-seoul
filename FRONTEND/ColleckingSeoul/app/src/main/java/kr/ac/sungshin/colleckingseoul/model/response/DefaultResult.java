package kr.ac.sungshin.colleckingseoul.model.response;

/**
 * Created by kwonhyeon-a on 2018. 5. 15..
 */

public class DefaultResult {
    private String message;
    private String code;

    public DefaultResult(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
