package com.life.entityret;

import com.life.common.JsonUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ListPlaceRet {

    public long total;
    public List<PlaceRet> places = new ArrayList<>();

    public ListPlaceRet() {

    }

    public void add(PlaceRet placeRet) {
        places.add(placeRet);
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

}
