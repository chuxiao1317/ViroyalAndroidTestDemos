package com.chuxiao.coolweather3.rsp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class BaseResponse {
    public int error_code;
    public String error_msg;

    public BaseResponse() {
    }

    public BaseResponse(int error_code, String error_msg) {
        this.error_code = error_code;
        this.error_msg = error_msg;
    }

    public String toString() {
        return "error_code:" + error_code + ",error_msg:" + error_msg;
    }
}
