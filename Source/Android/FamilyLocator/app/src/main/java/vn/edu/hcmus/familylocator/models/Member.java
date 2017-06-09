package vn.edu.hcmus.familylocator.models;

/**
 * Created by Quang Cat on 15/05/2017.
 */

public class Member {

    public long id;
    public String firstName;
    public String lastName;
    public String avatar;
    public boolean admin;

    public String getFullname() {
        return firstName + " " + lastName;
    }

}
