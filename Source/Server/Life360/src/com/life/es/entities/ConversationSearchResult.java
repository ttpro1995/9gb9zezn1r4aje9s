package com.life.es.entities;

import com.life.common.JsonUtils;
import java.io.Serializable;
import java.util.List;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ConversationSearchResult implements Serializable {

    private static final long serialVersionUID = -3698177469832666778L;

    private int error;
    private List<Conversation> value;

    private long total;

    public ConversationSearchResult() {
    }

    public ConversationSearchResult(int error) {
        this.error = error;
    }

    public ConversationSearchResult(int error, List<Conversation> value) {
        this.error = error;
        this.value = value;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public List<Conversation> getValue() {
        return value;
    }

    public void setValue(List<Conversation> value) {
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
