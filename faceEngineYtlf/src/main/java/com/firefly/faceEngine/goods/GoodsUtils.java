package com.firefly.faceEngine.goods;

import android.util.Log;

import com.firefly.faceEngine.App;
import com.firefly.faceEngine.dblib.SettingManage;
import com.firefly.faceEngine.dblib.bean.Setting;

import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoodsUtils {

    private static String goods;


    //预测
    public static String getPredictGoods(Long userId) {
        try {
            SettingManage settingManage = App.getInstance().getSettingManage();

            FormBody.Builder params = new FormBody.Builder();
            params.add("userId",userId.toString());

            List<Setting> settings = settingManage.getSaveInformation();
            String url  = settings.get(0).getPredicted();

            Log.e("", "getPredictGoods: "+url);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://"+url+"/goods/predict")
                    .post(params.build())
                    .build();
            Response response = client.newCall(request).execute();
            goods = response.body().string();
        } catch (Exception e) {
            goods = "Network connection failure";
        }
        return goods;
    }

    //推荐
    public static String getRecommendGoods(Long userId) {
        try {
            SettingManage settingManage = App.getInstance().getSettingManage();

            FormBody.Builder params = new FormBody.Builder();
            params.add("userId",userId.toString());

            List<Setting> settings = settingManage.getSaveInformation();
            String url  = settings.get(0).getRecommended();

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://"+url+"/goods/recommend")
                    .post(params.build())
                    .build();
            Response response = client.newCall(request).execute();
            goods = response.body().string();
        } catch (Exception e) {
            goods = "Network connection failure";
        }
        return goods;
    }
}
