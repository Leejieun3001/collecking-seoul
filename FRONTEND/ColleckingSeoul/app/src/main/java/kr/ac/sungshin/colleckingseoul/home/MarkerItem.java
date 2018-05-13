package kr.ac.sungshin.colleckingseoul.home;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by gwonhyeon-a on 2018. 5. 14..
 */

public class MarkerItem implements ClusterItem {
    private final LatLng position;

    public MarkerItem(double lat, double lng) {
        this.position = new LatLng(lat, lng);
    }

    @Override
    public LatLng getPosition() {
        return null;
    }
}
