package com.chuxiao.lbstest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public LocationClient mLocationClient;

    private TextView positionText;

    private MapView mapView;

    private BaiduMap baiduMap;

    private boolean isFirstLocate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        // 初始化百度地图SDK
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        // 让地图显示出来（在mapView.onResume()的时候）
        mapView = findViewById(R.id.baidu_mapView);
        // 初始化BaiduMap
        baiduMap = mapView.getMap();
        // 开启“显示我在地图上的位置”功能
        baiduMap.setMyLocationEnabled(true);
        positionText = findViewById(R.id.position_tv);
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        } else {
            requestLocation();
        }
    }

    /**
     * 在地图上定位到某个位置
     */
    private void navigateTo(BDLocation location) {
        // 只需要在第一次定位时调用此判断里面的内容
        if (isFirstLocate) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        // 显示“我”在地图上的位置
        baiduMap.setMyLocationData(locationData);
    }

    /**
     * 开始定位地理位置
     */
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        // 设置时间间隔，1秒钟更新一下
        option.setScanSpan(1000);
        // 默认模式，即高精确度模式：优先使用GPS定位，无法接收GPS信号时使用网络定位
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        // 将定位模式设定为传感器模式，强制只使用GPS进行定位
//        option.setLocationMode(LocationClientOption.LocationMode.Device_Sensors);
//        // 节电模式：只使用网络定位
//        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        // 获取当前位置的详细地址信息
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    // 走到此步，说明用户已同意所有权限，可以执行下一步：获取位置
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 活动销毁时一定要停止定位，否则会严重持续消耗电量
        mLocationClient.stop();
        // 释放MapView
        mapView.onDestroy();
        // 关闭“显示我在地图上的位置”功能
        baiduMap.setMyLocationEnabled(false);
    }

    /**
     * 用于获取位置信息的内部类
     */
    public class MyLocationListener implements BDLocationListener {

        /**
         * 接收到位置信息的时候回调
         */
        @Override
        public void onReceiveLocation(final BDLocation location) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuilder currentPosition = new StringBuilder();
                    currentPosition.append("纬度：").append(location.getLatitude()).append("\n");
                    currentPosition.append("经度：").append(location.getLongitude()).append("\n");
                    currentPosition.append("国家：").append(location.getCountry()).append("\n");
                    currentPosition.append("省：").append(location.getProvince()).append("\n");
                    currentPosition.append("市：").append(location.getCity()).append("\n");
                    currentPosition.append("区：").append(location.getDistrict()).append("\n");
                    currentPosition.append("街道：").append(location.getStreet()).append("\n");
                    currentPosition.append("定位方式：");
                    if (location.getLocType() == BDLocation.TypeGpsLocation) {
                        currentPosition.append("GPS");
                    } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                        currentPosition.append("网络");
                    }
                    positionText.setText(currentPosition);
                }
            });
            // 定位到获取到的地址
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                navigateTo(location);
            }

        }

    }
}
