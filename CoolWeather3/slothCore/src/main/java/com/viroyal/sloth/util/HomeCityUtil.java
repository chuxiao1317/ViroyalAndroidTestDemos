//package com.viroyal.sloth.util;
//
//import android.content.Context;
//
//import com.viroyal.sloth.location.LocationUtil;
//import com.viroyal.sloth.location.SlothLocation;
//import com.viroyal.sloth.location.SlothLocationListener;
//
///**
// * 检测当前城市的工具类
// * 定位成功以后，把结果保存到preference中
// *
// * Created by LiGang on 2016/11/17.
// */
//
//public class HomeCityUtil {
//    private static final String KEY_CURRENT_CITY = "current_city";
//    private LocationUtil mLocationUtil;
//    /**
//     * 定位当前城市
//     * @param context context
//     */
//    public void detect(final Context context) {
//        if (mLocationUtil != null) {
//            return;
//        }
//
//        mLocationUtil = new LocationUtil(context, new SlothLocationListener() {
//            @Override
//            public void onLocationChanged(SlothLocation location) {
//                if (location != null) {
//                    String addr = location.province + "&" + location.city;
//                    SPUtils spUtils = SPUtils.getInstance(context);
//                    spUtils.put(KEY_CURRENT_CITY, addr);
//                    Slog.d("HomeCityUtil", "get city:" + addr);
//                }
//
//                mLocationUtil.stopLocation();
//                mLocationUtil = null;
//            }
//        });
//
//        mLocationUtil.startLocation(false);
//    }
//
//    /**
//     * 获得前面定位到当前城市
//     * @param context context
//     * @return 已符号"&"分割，形如 省&市
//     */
//    public String getCity(Context context) {
//        SPUtils sp = SPUtils.getInstance(context);
//        return sp.get(KEY_CURRENT_CITY);
//    }
//}
