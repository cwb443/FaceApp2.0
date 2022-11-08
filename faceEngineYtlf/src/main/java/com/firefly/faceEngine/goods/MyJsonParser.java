package com.firefly.faceEngine.goods;



import com.firefly.faceEngine.goods.bean.Goods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyJsonParser {
    public static ArrayList<Goods> getGoods(String str) throws Exception {


        ArrayList<Goods> arrlist = new ArrayList<Goods>();
        //获取json对象
        JSONObject json = new JSONObject(str);
        //获取daily数组
        JSONArray AllGoods = json.getJSONArray("data");
        for(int i=0;i<AllGoods.length();i++) {
            //准备一个weather对象
            Goods goods = new Goods();
            JSONObject item = (JSONObject) AllGoods.get(i);

            goods.setId(item.optInt("id"));
            goods.setPicture(item.optString("picture"));
            goods.setName(item.optString("name"));
            goods.setDescription(item.optString("description"));
            goods.setPrice(item.optDouble("price"));

            goods.setQuantity(item.optInt("quantity"));

            arrlist.add(goods);
        }
        return arrlist;
    }
}
