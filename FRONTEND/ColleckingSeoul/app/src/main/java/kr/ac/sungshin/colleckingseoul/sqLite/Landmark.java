package kr.ac.sungshin.colleckingseoul.sqLite;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kwonhyeon-a on 2018. 4. 18..
 */

public class Landmark {
    private int idx;
    private String name;
    private String content;
    private LatLng latlng;
    private String category;

    public Landmark(int idx, String name, double lat, double lng) {
        this.idx = idx;
        this.name = name;
        this.latlng = new LatLng(lat, lng);
    }

    public Landmark(int idx, String name, String content, double lat, double lng, String category) {
        this.idx = idx;
        this.name = name;
        this.content = content;
        this.latlng = new LatLng(lat, lng);
        this.category = category;
    }

    public Landmark(String name, double lat, double lng, String category) {

        this.name = name;
        this.latlng = new LatLng(lat, lng);
        this.category = category;
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
        return latlng.latitude;
    }

    public double getLng() {
        return latlng.longitude;
    }

    public void setLat(double lat) {
        latlng = new LatLng(lat, latlng.longitude);
    }

    public void setLng(double lng) {
        latlng = new LatLng(latlng.latitude, lng);
    }

    public void setLatLng(double lat, double lng) {
        latlng = new LatLng(lat, lng);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setLatlng(LatLng latlng) {
        this.latlng = latlng;
    }


}
