package com.firefly.faceEngine.dblib.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class Setting {
    @Id(autoincrement = true)
    private Long id;
    private Integer recognition;//人脸识别
    private Integer white;//白光
    private Integer brightness;//白光强度
    private Integer infrared;//红外光
    private String predicted;//预测
    private String recommended;//推荐
    private Integer goodsOpen;//商品推荐是否打开
    private Integer customerList;
    private String jumpInterval;
    @Generated(hash = 137075545)
    public Setting(Long id, Integer recognition, Integer white, Integer brightness,
            Integer infrared, String predicted, String recommended,
            Integer goodsOpen, Integer customerList, String jumpInterval) {
        this.id = id;
        this.recognition = recognition;
        this.white = white;
        this.brightness = brightness;
        this.infrared = infrared;
        this.predicted = predicted;
        this.recommended = recommended;
        this.goodsOpen = goodsOpen;
        this.customerList = customerList;
        this.jumpInterval = jumpInterval;
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
    public Integer getWhite() {
        return this.white;
    }
    public void setWhite(Integer white) {
        this.white = white;
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
    public String getPredicted() {
        return this.predicted;
    }
    public void setPredicted(String predicted) {
        this.predicted = predicted;
    }
    public String getRecommended() {
        return this.recommended;
    }
    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }
    public Integer getGoodsOpen() {
        return this.goodsOpen;
    }
    public void setGoodsOpen(Integer goodsOpen) {
        this.goodsOpen = goodsOpen;
    }
    public Integer getCustomerList() {
        return this.customerList;
    }
    public void setCustomerList(Integer customerList) {
        this.customerList = customerList;
    }
    public String getJumpInterval() {
        return this.jumpInterval;
    }
    public void setJumpInterval(String jumpInterval) {
        this.jumpInterval = jumpInterval;
    }
}
