package kr.ac.sungshin.colleckingseoul.model.response;

/**
 * Created by LG on 2018-04-18.
 */

public class FindInfoResult {
    private String message;
    private String id;

    public FindInfoResult(String message, String id) {
        this.message = message;
        this.id = id;
    }

    public FindInfoResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }
}
