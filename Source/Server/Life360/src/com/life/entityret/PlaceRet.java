package com.life.entityret;

import com.life.common.JsonUtils;
import com.life.entity.base.LatLon;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class PlaceRet {

    public int id;
    public String name;
    public LatLon latlon;
    public int zone;
    
     @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }
}
