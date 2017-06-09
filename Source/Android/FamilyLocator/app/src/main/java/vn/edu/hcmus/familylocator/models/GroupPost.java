package vn.edu.hcmus.familylocator.models;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by quangcat on 5/28/17.
 */

public class GroupPost {

    public long id;
    public String firstName;
    public String lastName;
    public String avatar;
    public double lat;
    public double lon;
    public int battery;

    // Extra
    public String address;

    public String getFullname() {
        return firstName + " " + lastName;
    }

    public LatLng getLocation() {
        return new LatLng(lat, lon);
    }
}
