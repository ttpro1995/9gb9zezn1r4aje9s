package com.life.es.entities;

import com.life.common.JsonUtils;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class MessageSearchResult implements Serializable {

    private static final long serialVersionUID = 3719874078656454825L;

    private int error;
    private List<Message> value;

    private long total;

    public MessageSearchResult() {
    }

    public MessageSearchResult(int error) {
        this.error = error;
    }

    public MessageSearchResult(int error, List<Message> value) {
        this.error = error;
        this.value = value;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<Message> getValue() {
        return value;
    }

    public void setValue(List<Message> value) {
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
