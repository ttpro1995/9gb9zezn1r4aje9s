package vn.edu.hcmus.familylocator.core;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by quangcat on 5/20/17.
 */

public class JSONStringBuilder {

    private JSONObject jsonObject;

    public JSONStringBuilder() {
        jsonObject = new JSONObject();
    }

    public JSONStringBuilder put(String key, Object value) {
        try {
            jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String create() {
        return jsonObject.toString();
    }

}
