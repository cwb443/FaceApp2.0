package com.firefly.faceEngine.dblib.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Setting {
    @Id(autoincrement = true)
    private Long id;
    private Integer recognition;
    private Integer red;
    private Integer brightness;
    private Integer infrared;
    private String forecast;
    private String products;
    @Generated(hash = 1872241443)
    public Setting(Long id, Integer recognition, Integer red, Integer brightness,
            Integer infrared, String forecast, String products) {
        this.id = id;
        this.recognition = recognition;
        this.red = red;
        this.brightness = brightness;
        this.infrared = infrared;
        this.forecast = forecast;
        this.products = products;
    }
    @Generated(hash = 909716735)
    public Setting() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Integer getRecognition() {
        return this.recognition;
    }
    public void setRecognition(Integer recognition) {
        this.recognition = recognition;
    }
    public Integer getRed() {
        return this.red;
    }
    public void setRed(Integer red) {
        this.red = red;
    }
    public Integer getBrightness() {
        return this.brightness;
    }
    public void setBrightness(Integer brightness) {
        this.brightness = brightness;
    }
    public Integer getInfrared() {
        return this.infrared;
    }
    public void setInfrared(Integer infrared) {
        this.infrared = infrared;
    }
    public String getForecast() {
        return this.forecast;
    }
    public void setForecast(String forecast) {
        this.forecast = forecast;
    }
    public String getProducts() {
        return this.products;
    }
    public void setProducts(String products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", recognition=" + recognition +
                ", red=" + red +
                ", brightness=" + brightness +
                ", infrared=" + infrared +
                ", forecast='" + forecast + '\'' +
                ", products='" + products + '\'' +
                '}';
    }
}
