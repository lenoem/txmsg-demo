package com.linewell.msgdemo.common;

import com.alibaba.fastjson.JSONObject;

public class Result {
    public static final String SUCCESS = "1";
    public static final String FAILURE = "0";

    private String success;
    private String message;
    private JSONObject data ;

    public Result() {
        this.success = SUCCESS;
        data = new JSONObject();
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(Object success) {
        this.success = success.toString();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        JSONObject result = data;
        result.put("message", message);
        if ("false".equals(success) || "0".equals(success)) {
            result.put("success", 0);
        } else {
            result.put("success", 1);
        }
        return result.toJSONString();
    }

    public boolean isSuccess() {
        if (success == null || "false".equals(success) || "0".equals(success)) {
            return false;
        }
        return true;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }
}
