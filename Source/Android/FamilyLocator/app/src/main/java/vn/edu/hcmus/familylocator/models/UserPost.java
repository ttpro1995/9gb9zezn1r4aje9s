package vn.edu.hcmus.familylocator.models;

/**
 * Created by quangcat on 5/21/17.
 */

public class UserPost {

    public long id = 0;
    public String content;
    public double lat = 0;
    public double lon = 0;
    public long from = 0;
    public long to = 0;
    public int zone = 0;
    public int type = 0;
    public boolean completed = false;

    // Extra
    public String address = "";

}
