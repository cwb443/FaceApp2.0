package com.firefly.faceEngine.goods;

import android.graphics.Bitmap;

public class GoodsMessage {

    private Bitmap bitmap;
    private String name;
    private Double price;
    private String description;
    private Integer quantity;

    public GoodsMessage() {
    }

    public GoodsMessage(Bitmap bitmap, String name, Double price, String description, Integer quantity) {
        this.bitmap = bitmap;
        this.name = name;
        this.price = price;
        this.description = description;
        this.quantity = quantity;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
