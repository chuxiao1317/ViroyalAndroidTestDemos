package com.chuxiao.coolweather3.util;

import android.text.TextUtils;

import com.chuxiao.coolweather3.db.City;
import com.chuxiao.coolweather3.db.County;
import com.chuxiao.coolweather3.db.Province;
import com.chuxiao.coolweather3.gson.Weather;
import com.chuxiao.coolweather3.rsp.AreaResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJDB;

import java.util.List;

public class Utility {

    /**
     * 将返回的json数据解析成Weather实体类
     */
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent, Weather.class);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

//    /**
//     * 解析和处理服务器返回的省级数据
//     */
//    public static boolean handleProvinceResponse(String response) {
//        if (!TextUtils.isEmpty(response)) {
//            try {
//                JSONArray allProvince = new JSONArray(response);
//                for (int i = 0; i < allProvince.length(); i++) {
//                    JSONObject provinceObject = allProvince.getJSONObject(i);
//                    Province province = new Province();
//                    province.setProvinceName(provinceObject.getString("name"));
//                    province.setProvinceCode(provinceObject.getInt("id"));
//                    province.save();
//                }
//                return true;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 解析和处理服务器返回的市级数据
//     */
//    public static boolean handleCityResponse(String response, int provinceId) {
//        if (!TextUtils.isEmpty(response)) {
//            try {
//                JSONArray allCities = new JSONArray(response);
//                for (int i = 0; i < allCities.length(); i++) {
//                    JSONObject cityObject = allCities.getJSONObject(i);
//                    City city = new City();
//                    city.setCityName(cityObject.getString("name"));
//                    city.setCityCode(cityObject.getInt("id"));
//                    city.setProvinceId(provinceId);
//                    city.save();
//                }
//                return true;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 解析和处理服务器返回的县级数据
//     */
//    public static boolean handleCountyResponse(String response, int cityId) {
//        if (!TextUtils.isEmpty(response)) {
//            try {
//                JSONArray allCounties = new JSONArray(response);
//                for (int i = 0; i < allCounties.length(); i++) {
//                    JSONObject countyObject = allCounties.getJSONObject(i);
//                    County county = new County();
//                    county.setCountyName(countyObject.getString("name"));
//                    county.setWeatherId(countyObject.getString("weather_id"));
//                    county.setCityId(cityId);
//                    county.save();
//                }
//                return true;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return false;
//    }

    /**
     * 解析和处理服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(List responseList) {
        if (!responseList.isEmpty()) {
            try {
                JSONArray allProvince = new JSONArray();
                for (Object object : responseList) {
                    allProvince.put(object);
                }
                for (int i = 0; i < allProvince.length(); i++) {
                    AreaResponse areaResponse = (AreaResponse) allProvince.get(i);
                    Province province = new Province();
                    province.setProvinceName(areaResponse.name);
                    province.setProvinceCode(areaResponse.code);
                    province.save();
//                    KJDB.getDefaultInstance().save(province);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的市级数据
     */
    public static boolean handleCityResponse(List responseList, int provinceId) {
        if (!responseList.isEmpty()) {
            try {
                JSONArray allCities = new JSONArray();
                for (Object object : responseList) {
                    allCities.put(object);
                }
                for (int i = 0; i < allCities.length(); i++) {
                    AreaResponse areaResponse = (AreaResponse) allCities.get(i);
                    City city = new City();
                    city.setCityName(areaResponse.name);
                    city.setCityCode(areaResponse.code);
                    city.setProvinceId(provinceId);
                    city.save();
//                    KJDB.getDefaultInstance().save(city);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * 解析和处理服务器返回的县级数据
     */
    public static boolean handleCountyResponse(List responseList, int cityId) {
        if (!responseList.isEmpty()) {
            try {
                JSONArray allCounties = new JSONArray();
                for (Object object : responseList) {
                    allCounties.put(object);
                }
                for (int i = 0; i < allCounties.length(); i++) {
                    AreaResponse areaResponse = (AreaResponse) allCounties.get(i);
                    County county = new County();
                    county.setCountyName(areaResponse.name);
                    county.setWeatherId(areaResponse.weatherId);
                    county.setCityId(cityId);
                    county.save();
//                    KJDB.getDefaultInstance().save(county);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
