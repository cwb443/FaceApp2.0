package com.firefly.faceApi.V2.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.firefly.faceApi.V2.Event.FaceDetectEventClass;
import com.firefly.faceApi.V2.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class HomeFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        SettingFragment settingFragment=new SettingFragment();

        Boolean flag = settingFragment.getFlag();
        if (flag){
            Runnable runnable = new Runnable() {

                @Override
                public void run() {

                }
            };
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getFaceDetectEvent(FaceDetectEventClass event){
        if(event == null){
            return;
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_pho,null);
        return LayoutInflater.from(getContext()).inflate(R.layout.fragment_home,container,false);

    }
}
