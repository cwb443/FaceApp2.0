package com.firefly.faceApi.V2.Keep;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class KeepLiveReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {

            //The screen is closed and the transparent Activity is enabled
            KeepLiveManager.getInstance().startKeepLiveActivity(context);

        } else if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {

            //The screen opens and closes the transparent Activity
            KeepLiveManager.getInstance().finishKeepLiveActivity();

        }
    }
}
