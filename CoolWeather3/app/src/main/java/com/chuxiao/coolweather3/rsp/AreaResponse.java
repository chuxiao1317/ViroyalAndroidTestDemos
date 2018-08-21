package com.chuxiao.coolweather3.rsp;

import com.google.gson.annotations.SerializedName;

public class AreaResponse /*extends BaseResponse*/ {

    /**
     * 城市编号，用于查询下属信息时用到，此处为了方便区分本地db存储的id
     */
    @SerializedName("id")
    public int code;

    public String name;

    @SerializedName("weather_id")
    public String weatherId;

}
