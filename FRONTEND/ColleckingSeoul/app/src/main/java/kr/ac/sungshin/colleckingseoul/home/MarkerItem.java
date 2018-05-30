package kr.ac.sungshin.colleckingseoul.home;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import kr.ac.sungshin.colleckingseoul.R;

/**
 * Created by kwonhyeon-a on 2018. 5. 14..
 */

public class MarkerItem implements ClusterItem {
    private final LatLng position;
    private String title;
    private int idx;
    private int visited;
    private int photo;

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

    public MarkerItem(double lat, double lng, String title, int idx, int visited) {
        this.position = new LatLng(lat, lng);
        this.title = title;
        this.idx = idx;
        this.visited = visited;
        this.photo = this.visited == 0 ? R.drawable.main_place_normal : R.drawable.main_place_visit;
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

    public int getVisited() {
        return visited;
    }

    public int getPhoto() {
        return photo;
    }
}
