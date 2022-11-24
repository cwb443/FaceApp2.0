package com.firefly.faceApi.V2.Keep;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.lang.ref.WeakReference;


public class KeepLiveManager {
    private static final KeepLiveManager ourInstance = new KeepLiveManager();

    public static KeepLiveManager getInstance() {
        return ourInstance;
    }

    private KeepLiveManager() {
    }

    //Weak references prevent memory leaks
    private WeakReference<KeepLiveActivity> reference;

    private KeepLiveReceiver receiver;

    public void setKeepLiveActivity(KeepLiveActivity activity) {
        reference = new WeakReference<>(activity);
    }

    //Enable transparent activities
    public void startKeepLiveActivity(Context context) {
        Intent intent = new Intent(context, KeepLiveActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    //Close transparent activities
    public void finishKeepLiveActivity() {
        if (reference != null && reference.get() != null) {
            reference.get().finish();
        }
    }

    //Register for Broadcasting
    public void registerKeepLiveReceiver(Context context) {
        receiver = new KeepLiveReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        context.registerReceiver(receiver, filter);
    }

    public void unregisterKeepLiveReceiver(Context context){
        if(receiver != null){
            context.unregisterReceiver(receiver);
        }
    }
}
