package com.chuxiao.coolweather3.db;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Property;
import org.kymjs.kjframe.database.annotate.Table;
import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

//@Table(name = "province")
public class Province extends DataSupport {

    @Id
    private int id;

    @Property(column = "province_name")
    @Column(unique = true)
    private String provinceName;

    @Property(column = "province_code")
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

}
