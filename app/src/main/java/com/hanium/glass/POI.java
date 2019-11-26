package com.hanium.glass;

import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;


public class POI {
    TMapPOIItem item;

    public POI(TMapPOIItem item){
        this.item = item;
    }

    @Override
    public String toString() {
        return item.getPOIName();
    }

    public TMapPoint getPoint(){
        return item.getPOIPoint();
    }


}
