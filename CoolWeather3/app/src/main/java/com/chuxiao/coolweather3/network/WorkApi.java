package com.chuxiao.coolweather3.network;

import com.chuxiao.coolweather3.rsp.AreaResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface WorkApi {

    @GET("china/{provinceId}/{cityId}")
    Observable<List<AreaResponse>> getAreaResponse(@Path("provinceId") String provinceId,
                                                @Path("cityId") String cityId);
}
