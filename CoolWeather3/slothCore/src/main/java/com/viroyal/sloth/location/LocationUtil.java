//package com.viroyal.sloth.location;
//
//import android.content.Context;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.geocoder.GeocodeResult;
//import com.amap.api.services.geocoder.GeocodeSearch;
//import com.amap.api.services.geocoder.RegeocodeAddress;
//import com.amap.api.services.geocoder.RegeocodeQuery;
//import com.amap.api.services.geocoder.RegeocodeResult;
//import com.viroyal.sloth.util.Slog;
//
///**
// * Created by qjj on 2016/8/17.
// */
//public class LocationUtil implements AMapLocationListener {
//
//    private static final String TAG = LocationUtil.class.getSimpleName();
//
//    private Context context;
//    private AMapLocationClient mlocationClient;
//    private AMapLocationClientOption mLocationOption;
//
//    public LocationUtil(Context context, SlothLocationListener listener) {
//        this.context = context;
//        this.listener = listener;
//    }
//
//    @Override
//    public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
//            SlothLocation loc = new SlothLocation();
//            loc.latitude = aMapLocation.getLatitude();
//            loc.longitude = aMapLocation.getLongitude();
//            queryAddress(context, loc);
//        } else {
//            String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
//            Slog.d(TAG, errText);
//            listener.onLocationChanged(null);
//        }
//    }
//    public void stopLocation(){
//        if (mlocationClient == null) {
//            mlocationClient.stopLocation();
//            mlocationClient.onDestroy();
//        }
//    }
//
//    public void startLocation(boolean highAccuracy) {
//        if (mlocationClient == null) {
//            mlocationClient = new AMapLocationClient(context);
//            mLocationOption = new AMapLocationClientOption();
//            //设置定位监听
//            mlocationClient.setLocationListener(this);
//            //设置为高精度定位模式
//            if (highAccuracy) {
//                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//            } else {
//                mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
//            }
//            mLocationOption.setOnceLocation(true);
//            //设置定位参数
//            mlocationClient.setLocationOption(mLocationOption);
//
//            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//            // 在定位结束后，在合适的生命周期调用onDestroy()方法
//            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//            mlocationClient.startLocation();
//        }
//    }
//
//    public void startLocation() {
//        startLocation(true);
//    }
//
//    GeocodeSearch mGeocoderSearch;
//    private SlothLocationListener listener;
//
//    private void queryAddress(final Context context, final SlothLocation oldLoc) {
//        Slog.d(TAG, "queryAddress");
//        mGeocoderSearch = new GeocodeSearch(context);
//        mGeocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
//            @Override
//            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
//                if (rCode == 1000 && listener != null) {
//                    if (result != null && result.getRegeocodeAddress() != null) {
//                        RegeocodeAddress regeocodeAddress = result.getRegeocodeAddress();
//                        Slog.d(TAG, "address :" + regeocodeAddress.getFormatAddress());
//                        Slog.d(TAG, "province :" + regeocodeAddress.getProvince()
//                                + " city:" + regeocodeAddress.getCity());
//                        oldLoc.address = regeocodeAddress.getFormatAddress();
//                        oldLoc.country = "中国";
//                        oldLoc.province = regeocodeAddress.getProvince();
//                        oldLoc.city = regeocodeAddress.getCity();
//                        listener.onLocationChanged(oldLoc);
//                    }
//                } else {
//                    listener.onLocationChanged(null);
//                }
//            }
//
//            @Override
//            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {
//
//            }
//        });
//        if (oldLoc != null) {
//            LatLonPoint point = new LatLonPoint(oldLoc.latitude, oldLoc.longitude);
//            Slog.d(TAG, "GeocodeSearch point :" + point);
//            // 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
//            RegeocodeQuery query = new RegeocodeQuery(point, 100, GeocodeSearch.AMAP);
//            mGeocoderSearch.getFromLocationAsyn(query);
//        }
//    }
//}
