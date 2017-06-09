package com.life.es;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ActionResult {

    private int error;
    private String message;

    public ActionResult() {
    }

    public ActionResult(int error) {
        this.error = error;
    }

    public ActionResult(int error, String message) {
        this.error = error;
        this.message = message;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
