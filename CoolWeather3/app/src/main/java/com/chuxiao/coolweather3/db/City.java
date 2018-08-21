package com.chuxiao.coolweather3.db;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Property;
import org.kymjs.kjframe.database.annotate.Table;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

//@Table(name = "city")
public class City extends DataSupport {

    @Id
    private int id;

    @Property(column = "city_name")
    @Column(unique = true)
    private String cityName;

    @Property(column = "city_code")
    private int cityCode;

    @Property(column = "province_id")
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
