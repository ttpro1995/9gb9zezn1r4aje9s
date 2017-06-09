package com.life.es.entities;

import com.life.common.JsonUtils;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class PlaceSearchResult implements Serializable {

    private static final long serialVersionUID = 5753644820812247869L;

    public int error;
    public List<Place> value;

    public long total;

    public PlaceSearchResult() {
    }

    public PlaceSearchResult(int error) {
        this.error = error;
    }

    public PlaceSearchResult(int error, List<Place> value) {
        this.error = error;
        this.value = value;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<Place> getValue() {
        return value;
    }

    public void setValue(List<Place> value) {
        this.value = value;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
