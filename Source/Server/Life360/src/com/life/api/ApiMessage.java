package com.life.api;

import com.life.common.JsonUtils;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 *
 * @author truongnguyenax@gmail.com
 */
public class ApiMessage {

    @JsonProperty("error_code")
    public int errorCode;

    @JsonProperty("error_message")
    public String errorMessage;

    @JsonProperty("data")
    public Object data;

    public ApiMessage(int error, String message) {
        errorCode = error;
        errorMessage = message;
    }

    public ApiMessage(int error, String message, Object data) {
        errorCode = error;
        errorMessage = message;
        this.data = data;
    }

    public ApiMessage(Object data) {
        errorCode = 0;
        errorMessage = "Successful.";
        this.data = data;
    }

    @JsonProperty("error_code")
    public int getError() {
        return errorCode;
    }

    @JsonProperty("error_message")
    public String getMessage() {
        return errorMessage;
    }

    @JsonProperty("data")
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;

    }

    @Override
    public String toString() {
        return ApiMessage.toJsonString(this);
    }

    public static final ApiMessage SUCCESS = new ApiMessage(0, "Successful.", "");
    public static final ApiMessage FAIL = new ApiMessage(1002, "Fail");
    //public static final ApiMessage MISSING_PARAM = new ApiMessage(1003, "Missing param.");
    //public static final ApiMessage ACCESS_DENY = new ApiMessage(1004, "Access deny.");
    // public static final ApiMessage INCORRENT_FORMAT_PARAM = new ApiMessage(1005, "Incorrect formatting parameters.");
    public static final ApiMessage UNKNOWN_EXCEPTION = new ApiMessage(1006, "Unkown exception");
    //public static final ApiMessage NOT_FOUND = new ApiMessage(1007, "Not found.");
    public static final ApiMessage INVALID_DATA = new ApiMessage(1008, "Invalid data.");
    public static final ApiMessage NOT_EXISTS = new ApiMessage(1009, "Not exists.");
    public static final ApiMessage ITEM_NOT_FOUND = new ApiMessage(1010, "Item not found.");
    public static final ApiMessage ALREADY_EXISTS = new ApiMessage(1011, "Already exists.");
    public static final ApiMessage IS_FRIEND = new ApiMessage(1012, "Is friend.");
    public static final ApiMessage NOT_FRIEND = new ApiMessage(1013, "Not friend.");
    public static final ApiMessage EXISTS_LOGIN = new ApiMessage(1014, "Exists login.");

    public static final ApiMessage EXCEED_LOGIN_TIMES = new ApiMessage(2001, "This account attempts to try many login failed. Close connection.");

    public static final ApiMessage SERVER_ERROR = new ApiMessage(5001, "Server error.");
    public static final ApiMessage PERMISSION_DENY = new ApiMessage(5002, "Permission Denied.");

    public static ApiMessage getSuccessMsg() {
        return new ApiMessage(0, "Successful.", "");
    }

    public static String toJsonString(ApiMessage apiMessage) {

        String jsonString = JsonUtils.Instance.toJson(apiMessage);
        if (jsonString != null) {
            return jsonString;
        }
        return "error";
    }

}
