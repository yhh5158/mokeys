package com.room.mokeys.maps.presenters.iviews;

import com.amap.api.maps2d.model.MarkerOptions;

/**
 * Created by andforce on 15/7/19.
 */
public interface IGaoDeMapsView {

    public void addMarker(MarkerOptions marker);

//    public void addMarkers(ArrayList<MarkerOptions> markers);

    public void removeMarker(int markerId);

    public void changeMyLocationMode(int mode);

    public void stopFollowMode();

    public void changeMapStyle(int style);

}
