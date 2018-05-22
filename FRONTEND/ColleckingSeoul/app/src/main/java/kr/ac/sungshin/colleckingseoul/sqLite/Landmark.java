package kr.ac.sungshin.colleckingseoul.sqLite;

/**
 * Created by kwonhyeon-a on 2018. 4. 18..
 */

public class Landmark {
    private int idx;
    private String name;
    private String content;
    private double lat;
    private double lng;
    private String category;
    private int isVisit;

    public Landmark(int idx, String name, double lat, double lng) {
        this.idx = idx;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public Landmark(int idx, String name, String content, double lat, double lng, String category) {
        this.idx = idx;
        this.name = name;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
        this.category = category;
    }

    public Landmark(String name, double lat, double lng, String category) {
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.category = category;
    }

    public Landmark(int idx, String name, String content, double lat, double lng, String category, int isVisit) {
        this.idx = idx;
        this.name = name;
        this.content = content;
        this.lat = lat;
        this.lng = lng;
        this.category = category;
        this.isVisit = isVisit;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getIsVisit() {
        return isVisit;
    }

    public void setIsVisit(int isVisit) {
        this.isVisit = isVisit;
    }
}
