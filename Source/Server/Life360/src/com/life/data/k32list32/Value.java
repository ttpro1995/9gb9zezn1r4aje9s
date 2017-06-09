package com.life.data.k32list32;

import com.life.common.JsonUtils;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class Value implements Serializable {

    private static final long serialVersionUID = 2024140298858672947L;

    public int error;
    public int total;
    public List<Integer> value;

    public Value(int error) {
        this.error = error;
    }

    public Value(int error, int total, List<Integer> value) {
        this.error = error;
        this.total = total;
        this.value = value;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Integer> getValue() {
        return value;
    }

    public void setValue(List<Integer> value) {
        this.value = value;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return JsonUtils.Instance.toJson(this);
    }

}
