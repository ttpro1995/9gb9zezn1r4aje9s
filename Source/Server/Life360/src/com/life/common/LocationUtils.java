package com.life.common;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class LocationUtils {

    public static boolean checkValidLonLat(Double lon, Double lat) {
        if (lon == null || lon > 180 || lon < -180 || Double.isNaN(lon)) {
            return false;
        }

        if (lat == null || lat > 90 || lat < -90 || Double.isNaN(lon)) {
            return false;
        }

        return true;
    }

}
