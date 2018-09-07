package com.chuxiao.coolweather3.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.chuxiao.coolweather3.R;
import com.chuxiao.coolweather3.activity.MainActivity;
import com.chuxiao.coolweather3.db.City;
import com.chuxiao.coolweather3.db.County;
import com.chuxiao.coolweather3.db.Province;
import com.chuxiao.coolweather3.network.Api;
import com.chuxiao.coolweather3.network.BaseRspObserver;
import com.chuxiao.coolweather3.network.WorkApi;
import com.chuxiao.coolweather3.util.Utility;
import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;

import org.kymjs.kjframe.KJDB;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog progressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    // 省列表
    private List<Province> provinceList;
    // 市列表
    private List<City> cityList;
    // 县列表
    private List<County> countyList;

    // 选中的省
    private Province selectedProvince;
    // 选中的市
    private City selectedCity;

    // 当前选中的级别
    public static int currentLevel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area_fragment, container, false);
        titleText = (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.back_btn);
        listView = (ListView) view.findViewById(R.id.list_view);
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);
        RxBus.get().register(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            if (currentLevel == LEVEL_PROVINCE) {
                selectedProvince = provinceList.get(position);
                queryCities();
            } else if (currentLevel == LEVEL_CITY) {
                selectedCity = cityList.get(position);
                queryCounties();
            }
        });
        backButton.setOnClickListener(v -> {
            // 返回上一级
            backPreviousLevel();
        });
        queryProvinces();
    }

    /**
     * 返回键执行逻辑
     */
    public void backPreviousLevel() {
        if (currentLevel == LEVEL_COUNTY) {
            queryCities();
        } else if (currentLevel == LEVEL_CITY) {
            queryProvinces();
        }
    }

    /**
     * 查询全国所有省，优先从数据库查询，如果没有则从服务器查询
     */
    private void queryProvinces() {
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        provinceList = DataSupport.findAll(Province.class);
//        provinceList = KJDB.getDefaultInstance().findAll(Province.class);
        if (provinceList.size() > 0) {
            dataList.clear();
            for (Province province : provinceList) {
                dataList.add(province.getProvinceName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_PROVINCE;
        } else {
            // 从服务器查询省级数据
            queryFromServer("", "", "province");
        }
    }

    /**
     * 查询所有市，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    public void queryCities() {
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        cityList = DataSupport.where(
                "provinceid = ?",
                String.valueOf(selectedProvince.getId())).find(City.class);
//        cityList = null;
//        cityList = KJDB.getDefaultInstance().findAllByWhere(
//                City.class,
//                String.valueOf(selectedProvince.getId()));
        if (cityList != null && cityList.size() > 0) {
            dataList.clear();
            for (City city : cityList) {
                dataList.add(city.getCityName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY;
        } else {
            // 从服务器查询市级数据
            queryFromServer(
                    String.valueOf(selectedProvince.getProvinceCode()),
                    "",
                    "city");
        }
    }

    /**
     * 查询所有县，优先从数据库查询，如果没有查询到再去服务器上查询
     */
    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        countyList = DataSupport.where(
                "cityid = ?",
                String.valueOf(selectedCity.getId())).find(County.class);
//        countyList = null;
//        countyList = KJDB.getDefaultInstance().findAllByWhere(
//                County.class,
//                String.valueOf(selectedCity.getId()));
        if (countyList != null && countyList.size() > 0) {
            dataList.clear();
            for (County county : countyList) {
                dataList.add(county.getCountyName());
            }
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY;
        } else {
            // 从服务器查询县级数据
            queryFromServer(
                    String.valueOf(selectedProvince.getProvinceCode()),
                    String.valueOf(selectedCity.getCityCode()),
                    "county");
        }
    }

    /**
     * 根据省市区类型从服务器上查询数据
     */
    private void queryFromServer(String provinceId, String cityId, final String type) {
        showProgressDialog();
        MainActivity mainActivity = new MainActivity();
        mainActivity.addRxSubscription(
                Api.get().getApi(WorkApi.class)
                        .getAreaResponse(provinceId, cityId)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseRspObserver<>(List.class, new Action1<List>() {
                                    @Override
                                    public void call(List list) {
                                        boolean result = false;
                                        if ("province".equals(type)) {
                                            // 从服务获取的省级数据保存进本地数据库
                                            result = Utility.handleProvinceResponse(list);
                                        } else if ("city".equals(type)) {
                                            // 从服务获取的市级数据保存进本地数据库
                                            result = Utility.handleCityResponse(list, selectedProvince.getId());
                                        } else if ("county".equals(type)) {
                                            // 从服务获取的县级数据保存进本地数据库
                                            result = Utility.handleCountyResponse(list, selectedCity.getId());
                                        }
                                        if (result) {
                                            ChooseAreaFragment.this.closeProgressDialog();
                                            RxBus.get().post("queryFromServer", type);
                                        }
                                    }
                                })

//                            @Override
//                            public void onError(Throwable e) {
//                                closeProgressDialog();
//                                Toast.makeText(getContext(), "加载失败", Toast.LENGTH_SHORT).show();
//                            }
                        )
        );
    }

    /**
     * RxBus注册之后此处才能收到
     */
    @Subscribe(thread = EventThread.IMMEDIATE, tags = {@Tag("queryFromServer")})
    public void queryFromServerCallback(String type) {
        ChooseAreaFragment.this.closeProgressDialog();
        if ("province".equals(type)) {
            ChooseAreaFragment.this.queryProvinces();
        } else if ("city".equals(type)) {
            ChooseAreaFragment.this.queryCities();
        } else if ("county".equals(type)) {
            ChooseAreaFragment.this.queryCounties();
        }
    }

    /**
     * 显示查询进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载");
            // 设置不可取消
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
