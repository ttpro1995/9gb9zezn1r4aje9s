package vn.edu.hcmus.familylocator.models;

import android.graphics.Bitmap;

/**
 * Created by quangcat on 4/23/17.
 */

public class Message {

    public boolean mine;
    public String content;
    public Bitmap avatar;

    public Message(boolean mine, String content, Bitmap avatar) {
        this.mine = mine;
        this.content = content;
        this.avatar = avatar;
    }

}
