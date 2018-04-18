package kr.ac.sungshin.colleckingseoul.home;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kwonhyeon-a on 2018. 4. 18..
 */

public class Landmark {
    private int idx;
    private String name;
    private LatLng latlng;

    public Landmark(String name, double lat, double lng) {
        this.name = name;
        this.latlng = new LatLng(lat, lng);
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

    public double getLat () {
        return latlng.latitude;
    }

    public double getLng () {
        return latlng.longitude;
    }

    public void setLat (double lat) {
        latlng = new LatLng(lat, latlng.longitude);
    }

    public void setLng (double lng) {
        latlng = new LatLng(latlng.latitude, lng);
    }

    public void setLatLng (double lat, double lng) {
        latlng = new LatLng(lat, lng);
    }
}
