package com.life.es.entities;

import com.life.common.JsonUtils;
import java.io.Serializable;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class PlaceResult implements Serializable {

    private static final long serialVersionUID = -4215788044595029741L;

    private int error;
    private Place value;

    public PlaceResult() {
    }

    public PlaceResult(int error) {
        this.error = error;
    }

    public PlaceResult(int error, Place value) {
        this.error = error;
        this.value = value;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public Place getValue() {
        return value;
    }

    public void setValue(Place value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
