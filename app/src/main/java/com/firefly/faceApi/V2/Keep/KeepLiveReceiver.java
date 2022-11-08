package com.firefly.faceApi.V2.Keep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * @ProjectName: FaceApiDemoExternal_mast29
 * @Package: Keep
 * @ClassName: KeepLiveReceiver
 * @Description: java类作用描述
 * @Author: SQL
 * @CreateDate: 2022/11/1 21:51
 * @UpdateUser: 更新者
 * @UpdateDate: 2022/11/1 21:51
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */

public class KeepLiveReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {

            //屏幕关闭，开启透明Activity
            KeepLiveManager.getInstance().startKeepLiveActivity(context);

        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {

            //屏幕开启，关闭透明Activity
            KeepLiveManager.getInstance().finishKeepLiveActivity();

        }
    }
}
