package kr.ac.sungshin.colleckingseoul.home;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by kwonhyeon-a on 2018. 5. 14..
 */

public class MarkerItem implements ClusterItem {
    private final LatLng position;
    private String title;
    private int idx;

    public MarkerItem(double lat, double lng) {
        this.position = new LatLng(lat, lng);
    }

    public MarkerItem(double lat, double lng, String title) {
        this.position = new LatLng(lat, lng);
        this.title = title;
    }

    public MarkerItem(double lat, double lng, String title, int idx) {
        this.position = new LatLng(lat, lng);
        this.title = title;
        this.idx = idx;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    public String getTitle() {
        return title;
    }

    public int getIdx() {
        return idx;
    }

}
