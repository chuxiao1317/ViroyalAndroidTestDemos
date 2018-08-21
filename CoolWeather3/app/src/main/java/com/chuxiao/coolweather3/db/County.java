package com.chuxiao.coolweather3.db;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Property;
import org.kymjs.kjframe.database.annotate.Table;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

//@Table(name = "county")
public class County extends DataSupport {

    @Id
    private int id;

    @Property(column = "county_name")
    @Column(unique = true)
    private String countyName;

    @Property(column = "weather_id")
    private String weatherId;

    @Property(column = "city_id")
    private int cityId;

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
